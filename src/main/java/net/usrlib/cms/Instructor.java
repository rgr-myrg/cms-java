package net.usrlib.cms;

import java.util.List;

import net.usrlib.cms.course.Course;

public class Instructor extends User {
	protected List<Course> coursesTaught;

	public StudentAcademicRecord createAcademicRecordForStudentWithCourse(final Student student, final Course course) {
		return new StudentAcademicRecord(this, student, course);
	}
}
