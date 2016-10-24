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

	public EnrollmentStatus getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public List<StudentAcademicRecord> getStudentRecords() {
		return studentRecords;
	}

	public StudentCourseCatalog getStudentCourseCatalog() {
		return studentCourseCatalog;
	}

	public List<Course> getRegisteredCourseList() {
		return registeredCourseList;
	}

	public boolean hasTakenCoursePreviously(final Course course) throws SQLException {
		final String sqlString = AcademicRecordsTable.SELECT_COURSE_BY_ID
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
			String sql = AcademicRecordsTable.SELECT_COURSE_BY_ID
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
}
