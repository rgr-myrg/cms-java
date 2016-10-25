package net.usrlib.cms.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.usrlib.cms.course.Course;
import net.usrlib.cms.course.CourseDeniedCategory;
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

	public int processRegistrationRequests() {
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
		boolean validRequest = false;

		try {
			if (student.hasCoursePrerequisites(course) && !student.hasTakenCoursePreviously(course) && course.isCourseSeatAvailable()) {
				// process request and register the student
				final String updateSql = CourseRequestsTable.UPDATE_REQUESTS_TO_PROCESSED
						.replaceFirst("\\?", String.valueOf(student.getUuid()))
						.replaceFirst("\\?", String.valueOf(course.getCourseId())
				);

				if (Log.isDebug()) {
					Logger.debug(TAG,"Request is Valid");
					Logger.debug(TAG, updateSql);
				}

				if (DbHelper.doUpdateSql(updateSql)) {
					course.decrementCourseCapacity();
				}

				validRequest = true;
			} else {
				if (Log.isDebug()) {
					Logger.debug(TAG, "Request is DENIED. Reason: " + student.getCourseDeniedReason());
				}

				final CourseDeniedCategory deniedReason = student.getCourseDeniedReason();

				switch (deniedReason) {
					case MISSING_PREREQUISITES:
						missingPreReqsCount++;
						break;
					case COURSE_ALREADY_TAKEN:
						coursesAlreadyTakenCount++;
						break;
					case NO_AVAILABLE_SEATS:
						noAvailableSeatsCount++;
						break;
				}

				validRequest = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
