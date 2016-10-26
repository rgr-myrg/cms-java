package net.usrlib.cms.user;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.usrlib.cms.course.Course;
import net.usrlib.cms.course.CourseAssignment;
import net.usrlib.cms.logger.Log;
import net.usrlib.cms.logger.Logger;
import net.usrlib.cms.sql.AcademicRecordsTable;
import net.usrlib.cms.sql.CourseAssignmentsTable;
import net.usrlib.cms.student.StudentAcademicRecord;
import net.usrlib.cms.util.DbHelper;

public class Instructor extends User {
	public static final String TAG = Instructor.class.getSimpleName();

	private List<Course> courseListTaught;
	private Date lastTaughtDate;

	public List<Course> getCourseListTaught() {
		return courseListTaught;
	}

	public Date getLastTaughtDate() {
		return lastTaughtDate;
	}

	public StudentAcademicRecord insertAcademicRecord(final String data) {
		final StudentAcademicRecord academicRecord = new StudentAcademicRecord(data);

		// Ex: 15,8,2,nice job,A
		if (Log.isDebug()) {
			Logger.debug(TAG, "insertAcademicRecord: " + data);
		}

		try {
			PreparedStatement preparedStatement = DbHelper.getConnection()
					.prepareStatement(AcademicRecordsTable.INSERT_SQL);

			preparedStatement.setInt(1, academicRecord.getStudentUuid());
			preparedStatement.setInt(2, academicRecord.getCourseId());
			preparedStatement.setInt(3, academicRecord.getInstructorUuid());
			preparedStatement.setString(4, academicRecord.getInstructorComments());
			preparedStatement.setInt(5, academicRecord.getLetterGrade().ordinal());

			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return academicRecord;
	}

	public void setLetterGradeForStudentWithCourse() {
	}

	public void addCommentsForStudentWithCourse() {
	}

	public CourseAssignment addCourseCapacity(final String data) {
		final CourseAssignment courseAssignment = new CourseAssignment(data);
		final ResultSet resultSet = DbHelper.doSql(
				CourseAssignmentsTable.SELECT_CAPACITY_BY_COURSE_ID
					.replaceFirst("\\?", String.valueOf(courseAssignment.getCourseId())));

		try {
			final int newCapacity = courseAssignment.getCapacity() + resultSet.getInt(CourseAssignmentsTable.CAPACITY_COLUMN);
			final int rowId = resultSet.getInt(CourseAssignmentsTable.ID_COLUMN);

			resultSet.close();

			String updateSql = CourseAssignmentsTable.UPDATE_CAPACITY_BY_RECORD_ID
					.replaceFirst("\\?", String.valueOf(newCapacity))
					.replaceFirst("\\?", String.valueOf(rowId));

			if (Log.isDebug()) {
				Logger.debug(TAG, updateSql);
			}

			DbHelper.doUpdateSql(updateSql);

			courseAssignment.setCapacity(newCapacity);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return courseAssignment;
	}

	public void subtractCourseCapacity(final int courseId) throws SQLException {
		final String sqlString = CourseAssignmentsTable.SELECT_CAPACITY_BY_COURSE_ID
				.replaceFirst("\\?", String.valueOf(courseId));

		final ResultSet resultSet = DbHelper.doSql(sqlString);

		while (resultSet.next()) {
			int capacity = resultSet.getInt(CourseAssignmentsTable.CAPACITY_COLUMN);

			if (capacity > 0) {
				String updateSql = CourseAssignmentsTable.UPDATE_CAPACITY_BY_RECORD_ID
						.replaceFirst("\\?", String.valueOf(capacity - 1))
						.replaceFirst("\\?", resultSet.getString(CourseAssignmentsTable.ID_COLUMN));

				if (Log.isDebug()) {
					Logger.debug(TAG, sqlString);
				}

				DbHelper.doUpdateSql(updateSql);
				break;
			}
		}

		resultSet.close();
	}
}
