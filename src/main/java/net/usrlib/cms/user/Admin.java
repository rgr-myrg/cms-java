package net.usrlib.cms.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.usrlib.cms.course.Course;
import net.usrlib.cms.course.CourseDeniedCategory;
import net.usrlib.cms.logger.Log;
import net.usrlib.cms.logger.Logger;
import net.usrlib.cms.sql.CourseRequestsTable;
import net.usrlib.cms.util.DbHelper;

public class Admin extends User {
	public static final String TAG = Admin.class.getSimpleName();

	private int missingPreReqsCount = 0;
	private int coursesAlreadyTakenCount = 0;
	private int noAvailableSeatsCount = 0;

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
					Logger.debug(TAG,"Request is DENIED. Reason: " + student.getCourseDeniedReason());
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

	public int getMissingPreReqsCount() {
		return missingPreReqsCount;
	}

	public int getCoursesAlreadyTakenCount() {
		return coursesAlreadyTakenCount;
	}

	public int getNoAvailableSeatsCount() {
		return noAvailableSeatsCount;
	}
}
