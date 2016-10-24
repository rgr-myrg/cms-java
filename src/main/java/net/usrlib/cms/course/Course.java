package net.usrlib.cms.course;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.usrlib.cms.sql.CourseAssignmentsTable;
import net.usrlib.cms.sql.CoursePrerequisitesTable;
import net.usrlib.cms.util.DbHelper;

public class Course {
	private int courseId;
	private String title;
	private String description;
	private List<Semester> semestersOffered = new ArrayList<>();;
	private List<Course> prerequisites = new ArrayList<>();
	private List<CourseAssignment> assignments = new ArrayList<>();
	private CourseStatus courseStatus = CourseStatus.REGISTRATION_OPEN;

	public Course(final int courseId) {
		this.courseId = courseId;
	}

	public int getCourseId() {
		return courseId;
	}

	public List<Course> getPrerequisites() {
		final String sqlString = CoursePrerequisitesTable.SELECT_PREREQUISITES.replaceFirst("\\?", String.valueOf(courseId));
		final ResultSet resultSet = DbHelper.doSql(sqlString);

		System.out.println("getPrerequisites: " + sqlString);

		try {
			if (!resultSet.isBeforeFirst()) {
				//System.out.println("***** No prerequisite data found for this course. Done.");
				prerequisites = new ArrayList<>();
			} else {
				while (resultSet.next()) {
					prerequisites.add(new Course(resultSet.getInt(CoursePrerequisitesTable.PREREQ_ID_COLUMN)));
				}
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return prerequisites;
	}

	public boolean isCourseSeatAvailable() throws SQLException {
		final String sqlString = CourseAssignmentsTable.SELECT_CAPACITY_BY_COURSE_ID
				.replaceFirst("\\?", String.valueOf(courseId));

		final ResultSet resultSet = DbHelper.doSql(sqlString);
		int capacityCount = 0;

		if (!resultSet.isBeforeFirst()) {
			System.out.println("> No capacity found for this course. Done.");
			capacityCount = -1;
		} else {
			while (resultSet.next()) {
				capacityCount += resultSet.getInt(CourseAssignmentsTable.CAPACITY_COLUMN);
			}
		}

		resultSet.close();
		System.out.println("capacityCount: " + capacityCount);

		return capacityCount > 0;
	}
}
