package net.usrlib.cms.digest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.usrlib.cms.course.Course;
import net.usrlib.cms.data.CourseAssignmentsData;
import net.usrlib.cms.data.CoursePrerequisitesData;
import net.usrlib.cms.data.CourseRequestsData;
import net.usrlib.cms.data.CoursesData;
import net.usrlib.cms.data.InstructorsData;
import net.usrlib.cms.data.RecordsData;
import net.usrlib.cms.data.StudentsData;
import net.usrlib.cms.sql.CoursePrerequisitesTable;
import net.usrlib.cms.sql.CourseRequestsTable;
import net.usrlib.cms.sql.CoursesTable;
import net.usrlib.cms.sql.RecordsTable;
import net.usrlib.cms.sql.UsersTable;
import net.usrlib.cms.user.Student;
import net.usrlib.cms.user.UserRole;
import net.usrlib.cms.util.DbHelper;

public class Digest {
	private static Connection connection;

	public static final void connectToDb() {
		if (connection == null) {
			connection = DbHelper.getConnection();
		}
	}

	public static final void disconnectFromDb() {
		if (connection == null) {
			return;
		}

		DbHelper.closeConnection(connection);
	}

	public static final void loadCsvData() {
		CoursesData.load();
		InstructorsData.load();
		RecordsData.load();
		StudentsData.load();
		CourseAssignmentsData.load();
		CoursePrerequisitesData.load();
		CourseRequestsData.load();
	}

	public static final int getNumberOfStudentRecords() {
		return DbHelper.getCountOf(connection, RecordsTable.SELECT_COUNT);
	}

	public static final int getNumberOfStudents() {
		return DbHelper.getCountOf(connection, UsersTable.SELECT_COUNT_BY_STUDENT_TYPE);
	}

	public static final int getNumberOfStudentsWithoutClasses() {
		return DbHelper.getCountOf(connection, UsersTable.SELECT_COUNT_BY_STUDENT_TYPE_WITHOUT_CLASSES);
	}

	public static final int getNumberOfInstructors() {
		return DbHelper.getCountOf(connection, UsersTable.SELECT_COUNT_BY_INSTRUCTOR_TYPE);
	}

	public static final int getNumberOfInstructorsNotTeaching() {
		return DbHelper.getCountOf(connection, UsersTable.SELECT_COUNT_BY_INSTRUCTOR_TYPE_WITHOUT_CLASSES);
	}

	public static final int getNumberOfCourses() {
		return DbHelper.getCountOf(connection, CoursesTable.SELECT_COUNT);
	}

	public static final int getNumberOfCoursesWithoutStudents() {
		return DbHelper.getCountOf(connection, CoursesTable.SELECT_COUNT_WITHOUT_STUDENTS);
	}

	public static final int getNumberOfFallSemesterCourses() {
		return DbHelper.getCountOf(connection, CoursesTable.SELECT_COUNT_FOR_FALL_COURSES);
	}

	public static final int getNumberOfSpringSemesterCourses() {
		return DbHelper.getCountOf(connection, CoursesTable.SELECT_COUNT_FOR_SPRING_COURSES);
	}

	public static final int getNumberOfSummerSemesterCourses() {
		return DbHelper.getCountOf(connection, CoursesTable.SELECT_COUNT_FOR_SUMMER_COURSES);
	}

	// Assignment 5 Getters
	public static final int getNumberOfCourseRequests() {
		return DbHelper.getCountOf(connection, CourseRequestsTable.SELECT_COUNT);
	}

	public static final int getNumberOfValidCourseRequests() {
		System.out.println("getNumberOfValidCourseRequests");
		// select from requests table
		ResultSet resultSet = DbHelper.doSql(connection, CourseRequestsTable.SELECT_ALL);

		try {
			while (resultSet.next()) {
				int studentUuid = resultSet.getInt(CourseRequestsTable.STUDENT_ID_COLUMN);
				int courseId = resultSet.getInt(CourseRequestsTable.COURSE_ID_COLUMN);

				System.out.println("-> studentUuid: " + studentUuid);
				System.out.println("-> courseId: " + courseId);

				ResultSet studentResultSet = DbHelper.doSql(connection, UsersTable.SELECT_STUDENT_BY_ID + studentUuid);

				Student student = new Student(
					studentUuid,
					studentResultSet.getString(UsersTable.NAME_COLUMN),
					studentResultSet.getString(UsersTable.ADDRESS_COLUMN),
					studentResultSet.getString(UsersTable.PHONE_COLUMN),
					UserRole.STUDENT
				);

				Course course = new Course();
				course.setCourseId(courseId);

				final ResultSet preReqResultSet = DbHelper.doSql(connection, CoursePrerequisitesTable.SELECT_PREREQUISITES + courseId);
				while (preReqResultSet.next()) {
					System.out.println("-> preReqResultSet: " + preReqResultSet.getInt(CoursePrerequisitesTable.PREREQ_ID_COLUMN));
				}
	
				student.registerForCourse(courseId);

				//System.out.println(studentUuid + ":" + courseId + ":" + student.getPhone());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// process each entry
		return 0;
	}
}

/*
(1) the total number of records in the records.csv file
(2) the total number of records in the students.csv file
(3) the number of students who haven’t taken any classes
(4) the total number of records in the instructors.csv file
(5) the number of instructors who haven’t taught any classes
(6) the total number of records in the courses.csv file
(7) the number of courses that haven’t been taken by any students
(8) the total number of courses being offered during the Fall semester
(9) the total number of courses being offered during the Spring semester
(10) the total number of courses being offered during the Summer semester
*/