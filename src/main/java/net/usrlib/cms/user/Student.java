package net.usrlib.cms.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.usrlib.cms.EnrollmentStatus;
import net.usrlib.cms.StudentAcademicRecord;
import net.usrlib.cms.course.Course;
import net.usrlib.cms.course.LetterGrade;
import net.usrlib.cms.course.StudentCourseCatalog;
import net.usrlib.cms.sql.AcademicRecordsTable;
import net.usrlib.cms.sql.UsersTable;
import net.usrlib.cms.util.DbHelper;

public class Student extends User {
	private EnrollmentStatus enrollmentStatus;
	private List<StudentAcademicRecord> studentRecords;
	private StudentCourseCatalog studentCourseCatalog;
	private List<Course> registeredCourseList;

//	public Student(int uuid, String name, String address, String phone) {
//		super(uuid, name, address, phone, UserRole.STUDENT);
//	}

	public Student(int uuid) {
		final String sqlString = UsersTable.SELECT_STUDENT_BY_ID.replaceFirst("\\?", String.valueOf(uuid));
		final ResultSet resultSet = DbHelper.doSql(sqlString);

		this.uuid = uuid;

		try {
			this.name = resultSet.getString(UsersTable.NAME_COLUMN);
			this.address = resultSet.getString(UsersTable.ADDRESS_COLUMN);
			this.phone = resultSet.getString(UsersTable.PHONE_COLUMN);
			this.userRole = UserRole.STUDENT;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//viewCourseCatalog
	//registerForCourse
	//withdrawFromCourse
	//hasAllCoursePrerequisites
	//canRegisterForCourse
	//isCourseSeatAvailable

	public void registerForCourse(final int courseId) {
//		final Course course = new Course();
//		course.setCourseId(courseId);

		// check pre-requisites

		try {
//			if (hasCoursePrerequisites(courseId)) {
//				System.out.println("checkCoursePrerequisites OK");
//				if (hasTakenCoursePreviously(courseId)) {
//					System.out.println("hasTakenCoursePreviously true. Deny this request");
//				} else {
//					System.out.println("hasTakenCoursePreviously false");
//					if (isCourseSeatAvailable(courseId)) {
//						System.out.println("isCourseSeatAvailable YEP!!!");
//					} else {
//						System.out.println("isCourseSeatAvailable NO SEATS!!!");
//					}
//				}
//			} else {
//				System.out.println("checkCoursePrerequisites NOPE!. Deny this request");
//			}

			Course course = new Course(courseId);

			if (hasCoursePrerequisites(course) && !hasTakenCoursePreviously(course) && course.isCourseSeatAvailable()) {
				System.out.println("Request is Valid");
				// process request and register the student
				// decrement course capacity
			} else {
				System.out.println("Deny this request");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// check if course was taken and passed
		// check available seats
	}

//	public boolean isCourseSeatAvailable(final int courseId) throws SQLException {
//		final String sqlString = CourseAssignmentsTable.SELECT_CAPACITY_BY_COURSE_ID
//				.replaceFirst("\\?", String.valueOf(courseId));
//		final ResultSet resultSet = DbHelper.doSql(sqlString);
//
//		int capacityCount = 0;
//
//		if (!resultSet.isBeforeFirst()) {
//			System.out.println("> No capacity found for this course. Done.");
//			resultSet.close();
//			return false;
//		}
//
//		while (resultSet.next()) {
//			capacityCount += resultSet.getInt(CourseAssignmentsTable.CAPACITY_COLUMN);
//		}
//
//		resultSet.close();
//		System.out.println("capacityCount: " + capacityCount);
//		return capacityCount > 0;
//	}

	public boolean hasTakenCoursePreviously(final Course course) throws SQLException {
		final String sqlString = AcademicRecordsTable.SELECT_COURSE
				.replaceFirst("\\?", String.valueOf(uuid))
				.replaceFirst("\\?", String.valueOf(course.getCourseId()));

		final ResultSet resultSet = DbHelper.doSql(sqlString);

		boolean result = false;

		if (!resultSet.isBeforeFirst()) {
			System.out.println("> No academic data found for this course. Done.");
			resultSet.close();
			return result;
		}

		while (resultSet.next()) {
			int letterGrade = resultSet.getInt(AcademicRecordsTable.LETTER_GRADE_COLUMN);
			System.out.println("hasTakenCoursePreviously letterGrade: " + letterGrade);
			if (letterGrade == LetterGrade.F.ordinal() || letterGrade == LetterGrade.W.ordinal()) {
				result = false;
			} else {
				result = true;
			}
		}

		resultSet.close();
		return result;
	}

	public boolean hasCoursePrerequisites(final Course course) throws SQLException {
		final List<Course> preRequisites = course.getPrerequisites();
		boolean result = true;

		if (preRequisites.size() == 0 || preRequisites.isEmpty()) {
			System.out.println("No prerequisite data found for this course. Done.");
			return result;
		}

		for(Course item : preRequisites) {
			System.out.println("> checking preReqCourseId: " + item.getCourseId());
			String sql = AcademicRecordsTable.SELECT_COURSE
					.replaceFirst("\\?", String.valueOf(uuid))
					.replaceFirst("\\?", String.valueOf(item.getCourseId()));

			System.out.println("SQL: " + sql);

			ResultSet academicRecordsResultSet = DbHelper.doSql(sql);

			if (!academicRecordsResultSet.isBeforeFirst()) {
				System.out.println("No academicRecordsResultSet found.");
				academicRecordsResultSet.close();
				result = false;
				break;
			}

			academicRecordsResultSet.next();

			int letterGrade = academicRecordsResultSet.getInt(AcademicRecordsTable.LETTER_GRADE_COLUMN);

			academicRecordsResultSet.close();

			System.out.println("LETTER_GRADE: " + letterGrade);

			if (letterGrade == LetterGrade.F.ordinal() || letterGrade == LetterGrade.W.ordinal()) {
				result = false;
			} else {
				result = true;
			}
		}

		return result;
	}

//	public boolean hasCoursePrerequisites(final int courseId) throws SQLException {
//		final String sqlString = CoursePrerequisitesTable.SELECT_PREREQUISITES.replaceFirst("\\?", String.valueOf(courseId));
//		final ResultSet reqResultSet = DbHelper.doSql(sqlString);
//		boolean result = true;
//
//		System.out.println("checkCoursePrerequisites: " + sqlString);
//
//		if (!reqResultSet.isBeforeFirst()) {
//			System.out.println("No prerequisite data found for this course. Done.");
//			reqResultSet.close();
//			return result;
//		}
//
//		while (reqResultSet.next()) {
//			System.out.println("> checking preReqCourseId: " + reqResultSet.getInt(CoursePrerequisitesTable.PREREQ_ID_COLUMN));
//			String sql = AcademicRecordsTable.SELECT_COURSE
//					.replaceFirst("\\?", String.valueOf(uuid))
//					.replaceFirst("\\?", reqResultSet.getString(CoursePrerequisitesTable.PREREQ_ID_COLUMN));
//
//			System.out.println("SQL: " + sql);
//
//			ResultSet academicRecordsResultSet = DbHelper.doSql(sql);
//
//			if (!academicRecordsResultSet.isBeforeFirst()) {
//				System.out.println("No academicRecordsResultSet found.");
//				academicRecordsResultSet.close();
//				result = false;
//				break;
//			}
//
//			academicRecordsResultSet.next();
//
//			int letterGrade = academicRecordsResultSet.getInt(AcademicRecordsTable.LETTER_GRADE_COLUMN);
//
//			academicRecordsResultSet.close();
//
//			System.out.println("LETTER_GRADE: " + letterGrade);
//
//			if (letterGrade == LetterGrade.F.ordinal() || letterGrade == LetterGrade.W.ordinal()) {
//				result = false;
//			} else {
//				result = true;
//			}
//		}
//
//		reqResultSet.close();
//		return result;
//	}
}
