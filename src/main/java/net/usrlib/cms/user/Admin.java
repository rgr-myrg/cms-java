package net.usrlib.cms.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.usrlib.cms.course.Course;
import net.usrlib.cms.sql.CourseRequestsTable;
import net.usrlib.cms.util.DbHelper;

public class Admin extends User {
	public int processRegistrationRequests() {
		final ResultSet resultSet = DbHelper.doSql(CourseRequestsTable.SELECT_OPEN_REQUESTS);
		int validRequests = 0;

		try {
			while (resultSet.next()) {
				int studentUuid = resultSet.getInt(CourseRequestsTable.STUDENT_ID_COLUMN);
				int courseId = resultSet.getInt(CourseRequestsTable.COURSE_ID_COLUMN);

				System.out.println("-> studentUuid: " + studentUuid);
				System.out.println("-> courseId: " + courseId);

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
				System.out.println("Request is Valid");
				// process request and register the student
				// decrement course capacity
				validRequest = true;
			} else {
				System.out.println("Deny this request");
				validRequest = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return validRequest;
	}
}
