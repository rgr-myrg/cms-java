package net.usrlib.cms.digest;

import java.sql.Connection;

import net.usrlib.cms.data.CourseAssignmentsData;
import net.usrlib.cms.data.CoursePrerequisitesData;
import net.usrlib.cms.data.CourseRequestsData;
import net.usrlib.cms.data.CoursesData;
import net.usrlib.cms.data.InstructorsData;
import net.usrlib.cms.data.RecordsData;
import net.usrlib.cms.data.StudentsData;
import net.usrlib.cms.sql.CoursesTable;
import net.usrlib.cms.sql.RecordsTable;
import net.usrlib.cms.sql.UsersTable;
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
		// TODO: Uncomment when csv files are available
//		CourseAssignmentsData.load();
//		CoursePrerequisitesData.load();
//		CourseRequestsData.load();
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