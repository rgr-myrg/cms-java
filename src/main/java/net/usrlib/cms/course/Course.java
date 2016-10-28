package net.usrlib.cms.course;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.usrlib.cms.logger.Log;
import net.usrlib.cms.logger.Logger;
import net.usrlib.cms.sql.CourseAssignmentsTable;
import net.usrlib.cms.sql.CoursePrerequisitesTable;
import net.usrlib.cms.util.DbHelper;

public class Course {
	public static final String TAG = Course.class.getSimpleName();

	private int courseId;
	private String title;
	private String description;
	private List<Semester> semestersOffered = new ArrayList<>();;
	private List<Course> prerequisites = new ArrayList<>();
	private List<CourseAssignment> assignments = new ArrayList<>();
	private CourseType courseType;
	private CourseStatus courseStatus = CourseStatus.REGISTRATION_OPEN;

	public Course(final int courseId) {
		this.courseId = courseId;
	}

	public int getCourseId() {
		return courseId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public List<Semester> getSemestersOffered() {
		return semestersOffered;
	}

	public List<Course> getPrerequisites() {
		return prerequisites;
	}

	public List<CourseAssignment> getAssignments() {
		return assignments;
	}

	public CourseType getCourseType() {
		return courseType;
	}

	public CourseStatus getCourseStatus() {
		return courseStatus;
	}

	public List<Course> fetchCoursePrerequisites() {
		final String sqlString = CoursePrerequisitesTable.SELECT_PREREQUISITES.replaceFirst("\\?", String.valueOf(courseId));
		final ResultSet resultSet = DbHelper.doSql(sqlString);

		if (Log.isDebug()) {
			Logger.debug(TAG, sqlString);
		}

		try {
			if (!resultSet.isBeforeFirst()) {
				if (Log.isDebug()) {
					Logger.debug(TAG, "No prerequisite data found for this course");
				}

				prerequisites = new ArrayList<>();
			} else {
				while (resultSet.next()) {
					prerequisites.add(new Course(resultSet.getInt(CoursePrerequisitesTable.PREREQ_ID_COLUMN)));
				}
			}

			resultSet.close();
		} catch (SQLException e) {
			//e.printStackTrace();
		}

		return prerequisites;
	}

	public boolean isCourseSeatAvailable() throws SQLException {
		final String sqlString = CourseAssignmentsTable.SELECT_CAPACITY_BY_COURSE_ID
				.replaceFirst("\\?", String.valueOf(courseId));

		final ResultSet resultSet = DbHelper.doSql(sqlString);
		int capacityCount = 0;

		if (!resultSet.isBeforeFirst()) {
			capacityCount = -1;
		} else {
			while (resultSet.next()) {
				capacityCount += resultSet.getInt(CourseAssignmentsTable.CAPACITY_COLUMN);
			}
		}

		resultSet.close();

		if (Log.isDebug()) {
			Logger.debug(TAG, sqlString);
			Logger.debug(TAG, "capacityCount: " + capacityCount);
		}

		return capacityCount > 0;
	}

//	public void decrementCourseCapacity() throws SQLException {
//		final String sqlString = CourseAssignmentsTable.SELECT_CAPACITY_BY_COURSE_ID
//				.replaceFirst("\\?", String.valueOf(courseId));
//
//		final ResultSet resultSet = DbHelper.doSql(sqlString);
//
//		while (resultSet.next()) {
//			int capacity = resultSet.getInt(CourseAssignmentsTable.CAPACITY_COLUMN);
//
//			if (capacity > 0) {
//				String updateSql = CourseAssignmentsTable.UPDATE_CAPACITY_BY_RECORD_ID
//						.replaceFirst("\\?", String.valueOf(capacity - 1))
//						.replaceFirst("\\?", resultSet.getString(CourseAssignmentsTable.ID_COLUMN));
//
//				if (Log.isDebug()) {
//					Logger.debug(TAG, sqlString);
//				}
//
//				DbHelper.doUpdateSql(updateSql);
//				break;
//			}
//		}
//	}

}
