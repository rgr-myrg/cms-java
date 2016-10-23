package net.usrlib.cms.course;

import java.util.List;

import net.usrlib.cms.sql.CoursePrerequisitesTable;
import net.usrlib.cms.util.DbHelper;

public class Course {
	private int courseId;
	private String title;
	private String description;
	private List<Semester> semestersOffered;
	private List<Course> prerequisites;
	private List<CourseAssignment> assignments;
	private CourseType courseType;
	private CourseStatus courseStatus;

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public List<Course> getPrerequisites() {
		return prerequisites;
	}
}
