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
					Logger.debug(TAG, String.format("Processing studentUuid:%s courseId:%s", studentUuid, courseId));
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
		final ResultSet resultSet = DbHelper.doSql(CourseRequestsTable.SELECT_APPROVED_REQUESTS_INFO);
		final List<String> dataPoints = new ArrayList<>();

		try {
			while (resultSet.next()) {
				String data = String.format("%d, %s, %d, %s", 
						resultSet.getInt(UsersTable.USER_ID_COLUMN),
						resultSet.getString(UsersTable.NAME_COLUMN),
						resultSet.getInt(CoursesTable.COURSE_ID_COLUMN),
						resultSet.getString(CoursesTable.COURSE_TITLE_COLUMN)
				);

				dataPoints.add(data);

				if (Log.isDebug()) {
					Logger.debug(TAG, data);
				}
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dataPoints;
	}

	public List<String> fetchCourseAssignmentsInfo() {
		final ResultSet resultSet = DbHelper.doSql(CourseAssignmentsTable.SELECT_CAPACITY_INFO);
		final List<String> dataPoints = new ArrayList<>();

		try {
			while (resultSet.next()) {
				String data = String.format("%d, %s, %d", 
						resultSet.getInt(CourseAssignmentsTable.COURSE_ID_COLUMN),
						resultSet.getString(CoursesTable.COURSE_TITLE_COLUMN),
						resultSet.getInt(CourseAssignmentsTable.CAPACITY_COLUMN)
				);

				dataPoints.add(data);

				if (Log.isDebug()) {
					Logger.debug(TAG, data);
				}
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataPoints;
	}

	public List<String> fetchAcademicRecordsInfo() {
		final ResultSet resultSet = DbHelper.doSql(AcademicRecordsTable.SELECT_SQL);
		final List<String> dataPoints = new ArrayList<>();

		try {
			while (resultSet.next()) {
				String data = String.format("%d, %d, %d, %s, %s", 
						resultSet.getInt(AcademicRecordsTable.STUDENT_ID_COLUMN),
						resultSet.getInt(AcademicRecordsTable.COURSE_ID_COLUMN),
						resultSet.getInt(AcademicRecordsTable.INSTRUCTOR_ID_COLUMN),
						resultSet.getString(AcademicRecordsTable.COMMENTS_COLUMN),
						LetterGrade.values()[resultSet.getInt(AcademicRecordsTable.LETTER_GRADE_COLUMN)]
				);

				dataPoints.add(data);

				if (Log.isDebug()) {
					Logger.debug(TAG, data);
				}
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataPoints;
	}
}
