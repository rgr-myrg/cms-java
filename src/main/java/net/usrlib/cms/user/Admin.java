package net.usrlib.cms.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.usrlib.cms.course.Course;
import net.usrlib.cms.course.CourseRequest;
import net.usrlib.cms.course.CourseRequestRemark;
import net.usrlib.cms.course.CourseRequestStatus;
import net.usrlib.cms.course.LetterGrade;
import net.usrlib.cms.logger.Log;
import net.usrlib.cms.logger.Logger;
import net.usrlib.cms.sql.AcademicRecordsTable;
import net.usrlib.cms.sql.CourseAssignmentsTable;
import net.usrlib.cms.sql.CourseRequestsTable;
import net.usrlib.cms.sql.CoursesTable;
import net.usrlib.cms.sql.UsersTable;
import net.usrlib.cms.util.DbHelper;

public class Admin extends User {
	public static final String TAG = Admin.class.getSimpleName();

	private int missingPreReqsCount = 0;
	private int coursesAlreadyTakenCount = 0;
	private int noAvailableSeatsCount = 0;

	private List<CourseRequest> courseRequests = new ArrayList<>();

	public int getMissingPreReqsCount() {
		return missingPreReqsCount;
	}

	public int getCoursesAlreadyTakenCount() {
		return coursesAlreadyTakenCount;
	}

	public int getNoAvailableSeatsCount() {
		return noAvailableSeatsCount;
	}

	public boolean enrollNewStudent(final Student student) {
		return true;
	}

	public boolean disenrollStudent(final Student student) {
		return true;
	}

	public boolean createNewCourse(final Course course) {
		return true;
	}

	public boolean updateCourse(final Course course) {
		return true;
	}

	public boolean removeCourse(final Course course) {
		return true;
	}

	public void setCurrentSemester() {	
	}

	public void addCourseAssignment() {
	}

	public void removeCourseAssignment() {
	}

	public int processCourseRequests() {
		final ResultSet resultSet = DbHelper.doSql(CourseRequestsTable.SELECT_OPEN_REQUESTS);
		int validRequests = 0;

		try {
			while (resultSet.next()) {
				int studentUuid = resultSet.getInt(CourseRequestsTable.STUDENT_ID_COLUMN);
				int courseId = resultSet.getInt(CourseRequestsTable.COURSE_ID_COLUMN);

				if (Log.isDebug()) {
					Logger.debug(TAG, String.format("Processing studentUuid: %s courseId: %s", studentUuid, courseId));
				}

				Student student = new Student(studentUuid);
				Course course = new Course(courseId);

				if (registerStudentForCourse(student, course)) {
					validRequests++;
				}
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return validRequests;
	}

	public boolean registerStudentForCourse(final Student student, final Course course) {
		CourseRequest courseRequest = new CourseRequest(student.getUuid(), course.getCourseId());
		boolean validRequest = false;

		try {
			if (student.hasCoursePrerequisites(course) 
					&& !student.hasTakenCoursePreviously(course) 
					&& course.isCourseSeatAvailable()) {

				final String updateSql = CourseRequestsTable.UPDATE_REQUESTS_TO_APPROVED
						.replaceFirst("\\?", String.valueOf(student.getUuid()))
						.replaceFirst("\\?", String.valueOf(course.getCourseId())
						.replaceFirst("\\?", String.valueOf(CourseRequestRemark.REQUEST_VALID.ordinal()))
				);

				courseRequest.setCourseRequestStatus(CourseRequestStatus.APPROVED);
				courseRequest.setCourseRequestRemark(CourseRequestRemark.REQUEST_VALID);

				if (Log.isDebug()) {
					Logger.debug(TAG,"Request is Valid");
					Logger.debug(TAG, updateSql);
				}

				if (DbHelper.doUpdateSql(updateSql)) {
					(new Instructor()).subtractCourseCapacity(course.getCourseId());
				}

				validRequest = true;
			} else {
				final CourseRequestRemark requestRemark = student.getCourseDeniedReason();

				final String updateSql = CourseRequestsTable.UPDATE_REQUESTS_WITH_DENIED_REASON
						.replaceFirst("\\?", String.valueOf(requestRemark.ordinal()))
						.replaceFirst("\\?", String.valueOf(student.getUuid()))
						.replaceFirst("\\?", String.valueOf(course.getCourseId())
				);

				if (Log.isDebug()) {
					Logger.debug(TAG, "Request is DENIED. Reason: " + requestRemark);
					Logger.debug(TAG, updateSql);
				}

				DbHelper.doUpdateSql(updateSql);

				switch (requestRemark) {
					case MISSING_PREREQUISITES:
						missingPreReqsCount++;
						break;
					case COURSE_ALREADY_TAKEN:
						coursesAlreadyTakenCount++;
						break;
					case NO_AVAILABLE_SEATS:
						noAvailableSeatsCount++;
						break;
					case REQUEST_VALID:
						break;
				}

				courseRequest.setCourseRequestStatus(CourseRequestStatus.DENIED);
				courseRequest.setCourseRequestRemark(requestRemark);

				validRequest = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		courseRequests.add(courseRequest);

		return validRequest;
	}

	public List<String> fetchApprovedRequestsInfo() {
		return fetchFromDbAndFormatResult(
				CourseRequestsTable.SELECT_APPROVED_REQUESTS_INFO,
				new String[] {
						UsersTable.USER_ID_COLUMN,
						UsersTable.NAME_COLUMN,
						CoursesTable.COURSE_ID_COLUMN,
						CoursesTable.COURSE_TITLE_COLUMN
				}
		);
	}

	public List<String> fetchCourseAssignmentsInfo() {
		return fetchFromDbAndFormatResult(
				CourseAssignmentsTable.SELECT_CAPACITY_INFO,
				new String[] {
						CourseAssignmentsTable.COURSE_ID_COLUMN,
						CoursesTable.COURSE_TITLE_COLUMN,
						CourseAssignmentsTable.CAPACITY_COLUMN
				}
		);
	}

	public List<String> fetchAcademicRecordsInfo() {
		return fetchFromDbAndFormatResult(
				AcademicRecordsTable.SELECT_SQL,
				new String[] {
						AcademicRecordsTable.STUDENT_ID_COLUMN, 
						AcademicRecordsTable.COURSE_ID_COLUMN, 
						AcademicRecordsTable.INSTRUCTOR_ID_COLUMN, 
						AcademicRecordsTable.COMMENTS_COLUMN, 
						AcademicRecordsTable.LETTER_GRADE_COLUMN
				}
		);
	}

	public String fetchDeniedRequestReason(final int studentUuid, final int courseId) {
		final String sqlString = CourseRequestsTable.SELECT_REQUEST_BY_STUDENT_ID_AND_COURSE_ID
				.replaceFirst("\\?", String.valueOf(studentUuid))
				.replaceFirst("\\?", String.valueOf(courseId));

		final ResultSet resultSet = DbHelper.doSql(sqlString);
		String deniedMessage = "";

		if (Log.isDebug()) {
			Logger.debug(TAG, sqlString);
		}

		try {
			while (resultSet.next()) {
				CourseRequestRemark deniedReason = CourseRequestRemark
						.values()[resultSet.getInt(CourseRequestsTable.REMARK_COLUMN)];
	
				switch (deniedReason) {
					case MISSING_PREREQUISITES:
						deniedMessage = "> student is missing one or more prerequisites";
						break;
					case COURSE_ALREADY_TAKEN:
						deniedMessage = "> student has already taken the course with a grade of C or higher";
						break;
					case NO_AVAILABLE_SEATS:
						deniedMessage = "> no remaining seats available for the course at this time";
						break;
					case REQUEST_VALID:
						deniedMessage = "> request is valid";
						break;
				}
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return deniedMessage;
	}

//	public void insertAcademicRecord(final String dataLine) {
//		String[] parts = dataLine.split(",");
//
//		// Ex: 15,8,2,nice job,A
//		if (Log.isDebug()) {
//			Logger.debug(TAG, "insertAcademicRecord: " + dataLine);
//		}
//
//		if (parts.length < 5) {
//			return;
//		}
//
//		try {
//			PreparedStatement preparedStatement = DbHelper.getConnection()
//					.prepareStatement(AcademicRecordsTable.INSERT_SQL);
//
//			preparedStatement.setInt(1, Integer.valueOf(parts[0]));
//			preparedStatement.setInt(2, Integer.valueOf(parts[1]));
//			preparedStatement.setInt(3, Integer.valueOf(parts[2]));
//			preparedStatement.setString(4, parts[3]);
//			preparedStatement.setInt(5, LetterGrade.valueOf(parts[4]).ordinal());
//
//			preparedStatement.execute();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

//	public void increaseAvailableSeatsForCourse(final String dataLine) {
//		String[] parts = dataLine.split(",");
//
//		// Ex: 13,3
//		if (parts.length < 2) {
//			return;
//		}
//
//		final int courseId = Integer.valueOf(parts[0]);
//		final int capacity = Integer.valueOf(parts[1]);
//
//		final ResultSet resultSet = DbHelper.doSql(
//				CourseAssignmentsTable.SELECT_CAPACITY_BY_COURSE_ID
//					.replaceFirst("\\?", String.valueOf(courseId)));
//
//		try {
//			final int newCapacity = capacity + resultSet.getInt(CourseAssignmentsTable.CAPACITY_COLUMN);
//			final int rowId = resultSet.getInt(CourseAssignmentsTable.ID_COLUMN);
//
//			resultSet.close();
//
//			String updateSql = CourseAssignmentsTable.UPDATE_CAPACITY_BY_RECORD_ID
//					.replaceFirst("\\?", String.valueOf(newCapacity))
//					.replaceFirst("\\?", String.valueOf(rowId));
//
//			if (Log.isDebug()) {
//				Logger.debug(TAG, updateSql);
//			}
//
//			DbHelper.doUpdateSql(updateSql);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	private List<String> fetchFromDbAndFormatResult(final String sql, final String[] columns) {
		final ResultSet resultSet = DbHelper.doSql(sql);
		final List<String> dataPoints = new ArrayList<>();
		final String joinByCommaAndSpace = ", ";

		try {
			while (resultSet.next()) {
				String data = "";

				for (String column : columns) {
					String columnValue = column.equals(AcademicRecordsTable.LETTER_GRADE_COLUMN)
								? LetterGrade.values()[resultSet.getInt(AcademicRecordsTable.LETTER_GRADE_COLUMN)].toString()
									: column.equals(CourseAssignmentsTable.CAPACITY_COLUMN)
										? String.valueOf(resultSet.getInt(column))
											: resultSet.getString(column);

					data += String.format("%s%s", columnValue, joinByCommaAndSpace);
				}

				// Trim trailing comma and space
				data = data.substring(0, data.length() - joinByCommaAndSpace.length());
				dataPoints.add(data);
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dataPoints;
	}
}
