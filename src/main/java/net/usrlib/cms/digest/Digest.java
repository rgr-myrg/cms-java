package net.usrlib.cms.digest;

import net.usrlib.cms.data.CourseAssignmentsData;
import net.usrlib.cms.data.CoursePrerequisitesData;
import net.usrlib.cms.data.CourseRequestsData;
import net.usrlib.cms.data.CoursesData;
import net.usrlib.cms.data.InstructorsData;
import net.usrlib.cms.data.RecordsData;
import net.usrlib.cms.data.StudentsData;
import net.usrlib.cms.sql.AcademicRecordsTable;
import net.usrlib.cms.sql.CourseRequestsTable;
import net.usrlib.cms.sql.CoursesTable;
import net.usrlib.cms.sql.UsersTable;
import net.usrlib.cms.user.Admin;
import net.usrlib.cms.util.DbHelper;

public class Digest {
	public static final Admin admin = new Admin();

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
		return DbHelper.getCountOf(AcademicRecordsTable.SELECT_COUNT);
	}

	public static final int getNumberOfStudents() {
		return DbHelper.getCountOf(UsersTable.SELECT_COUNT_BY_STUDENT_TYPE);
	}

	public static final int getNumberOfStudentsWithoutClasses() {
		return DbHelper.getCountOf(UsersTable.SELECT_COUNT_BY_STUDENT_TYPE_WITHOUT_CLASSES);
	}

	public static final int getNumberOfInstructors() {
		return DbHelper.getCountOf(UsersTable.SELECT_COUNT_BY_INSTRUCTOR_TYPE);
	}

	public static final int getNumberOfInstructorsNotTeaching() {
		return DbHelper.getCountOf(UsersTable.SELECT_COUNT_BY_INSTRUCTOR_TYPE_WITHOUT_CLASSES);
	}

	public static final int getNumberOfCourses() {
		return DbHelper.getCountOf(CoursesTable.SELECT_COUNT);
	}

	public static final int getNumberOfCoursesWithoutStudents() {
		return DbHelper.getCountOf(CoursesTable.SELECT_COUNT_WITHOUT_STUDENTS);
	}

	public static final int getNumberOfFallSemesterCourses() {
		return DbHelper.getCountOf(CoursesTable.SELECT_COUNT_FOR_FALL_COURSES);
	}

	public static final int getNumberOfSpringSemesterCourses() {
		return DbHelper.getCountOf(CoursesTable.SELECT_COUNT_FOR_SPRING_COURSES);
	}

	public static final int getNumberOfSummerSemesterCourses() {
		return DbHelper.getCountOf(CoursesTable.SELECT_COUNT_FOR_SUMMER_COURSES);
	}

	// Assignment 5 Getters
	/*
	 * (1) the total number of records in the requests.csv file (don’t count blank lines, etc.)
	 * (2) the number of valid (granted) requests
	 * (3) the number of requests that were denied because of one or more missing prerequisites
	 * (4) the number of requests that were denied the course was already taken
	 * (5) the number of requests that were denied because of a lack of available seats
	 */
	public static final int getNumberOfCourseRequests() {
		return DbHelper.getCountOf(CourseRequestsTable.SELECT_COUNT);
	}

	public static final int getNumberOfValidCourseRequests() {
		return admin.processRegistrationRequests();
	}

	public static final int getNumberOfMissingPrerequisites() {
		return admin.getMissingPreReqsCount();
	}

	public static final int getNumberOfCourseAlreadyTaken() {
		return admin.getCoursesAlreadyTakenCount();
	}

	public static final int getNumberOfNoOfAvailableSeats() {
		return admin.getNoAvailableSeatsCount();
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