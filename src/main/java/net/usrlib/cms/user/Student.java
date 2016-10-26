package net.usrlib.cms.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.usrlib.cms.course.Course;
import net.usrlib.cms.course.CourseRequestRemark;
import net.usrlib.cms.course.LetterGrade;
import net.usrlib.cms.logger.Log;
import net.usrlib.cms.logger.Logger;
import net.usrlib.cms.sql.AcademicRecordsTable;
import net.usrlib.cms.sql.UsersTable;
import net.usrlib.cms.student.StudentAcademicRecord;
import net.usrlib.cms.student.StudentCourseCatalog;
import net.usrlib.cms.student.StudentEnrollmentStatus;
import net.usrlib.cms.util.DbHelper;

public class Student extends User {
	public static final String TAG = Student.class.getSimpleName();

	private StudentEnrollmentStatus enrollmentStatus;
	private List<StudentAcademicRecord> studentRecords;
	private StudentCourseCatalog studentCourseCatalog;
	private List<Course> registeredCourseList;

	private CourseRequestRemark courseDeniedReason = CourseRequestRemark.NO_AVAILABLE_SEATS;

	public Student(int uuid) {
		final String sqlString = UsersTable.SELECT_STUDENT_BY_ID.replaceFirst("\\?", String.valueOf(uuid));
		final ResultSet resultSet = DbHelper.doSql(sqlString);

		if (Log.isDebug()) {
			Logger.debug(TAG, sqlString);
		}

		this.uuid = uuid;

		try {
			this.name = resultSet.getString(UsersTable.NAME_COLUMN);
			this.address = resultSet.getString(UsersTable.ADDRESS_COLUMN);
			this.phone = resultSet.getString(UsersTable.PHONE_COLUMN);
			this.userRole = UserRole.STUDENT;

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public StudentEnrollmentStatus getEnrollmentStatus() {
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

	public CourseRequestRemark getCourseDeniedReason() {
		return courseDeniedReason;
	}

	public boolean requestCourseRegistration(final Course course) {
		return true;
	}

	public boolean hasTakenCoursePreviously(final Course course) throws SQLException {
		final String sqlString = AcademicRecordsTable.SELECT_COURSE_BY_ID
				.replaceFirst("\\?", String.valueOf(uuid))
				.replaceFirst("\\?", String.valueOf(course.getCourseId()));

		final ResultSet resultSet = DbHelper.doSql(sqlString);

		if (Log.isDebug()) {
			Logger.debug(TAG, sqlString);
		}

		if (!resultSet.isBeforeFirst()) {
			if (Log.isDebug()) {
				Logger.debug(TAG, "No academic data found for this course");
			}

			resultSet.close();
			return false;
		}

		resultSet.next();

		int letterGrade = resultSet.getInt(AcademicRecordsTable.LETTER_GRADE_COLUMN);

		resultSet.close();

		if (Log.isDebug()) {
			Logger.debug(TAG, String.format("hasTakenCoursePreviously letterGrade: %s", letterGrade));
		}

		courseDeniedReason = CourseRequestRemark.COURSE_ALREADY_TAKEN;

		return !(letterGrade == LetterGrade.F.ordinal() || letterGrade == LetterGrade.W.ordinal());
	}

	public boolean hasCoursePrerequisites(final Course course) throws SQLException {
		final List<Course> preRequisites = course.fetchCoursePrerequisites();
		boolean result = true;

		if (preRequisites.size() == 0 || preRequisites.isEmpty()) {
			if (Log.isDebug()) {
				Logger.debug(TAG, "preRequisites list is empty");
			}

			return result;
		}

		for(Course item : preRequisites) {
			String sql = AcademicRecordsTable.SELECT_COURSE_BY_ID
					.replaceFirst("\\?", String.valueOf(uuid))
					.replaceFirst("\\?", String.valueOf(item.getCourseId()));

			if (Log.isDebug()) {
				Logger.debug(TAG, sql);
			}

			ResultSet academicRecordsResultSet = DbHelper.doSql(sql);

			if (!academicRecordsResultSet.isBeforeFirst()) {
				if (Log.isDebug()) {
					Logger.debug(TAG, "No academicRecordsResultSet found");
				}

				academicRecordsResultSet.close();

				result = false;
				break;
			}

			academicRecordsResultSet.next();

			int letterGrade = academicRecordsResultSet.getInt(AcademicRecordsTable.LETTER_GRADE_COLUMN);

			academicRecordsResultSet.close();

			if (Log.isDebug()) {
				Logger.debug(TAG, String.format("hasCoursePrerequisites letterGrade: %s", letterGrade));
			}

			result = !(letterGrade == LetterGrade.F.ordinal() || letterGrade == LetterGrade.W.ordinal());
		}

		courseDeniedReason = CourseRequestRemark.MISSING_PREREQUISITES;

		return result;
	}
}
