package net.usrlib.cms.student;

import java.util.List;

import net.usrlib.cms.course.Course;
import net.usrlib.cms.course.ProgramType;
import net.usrlib.cms.user.AcademicCouncelor;
import net.usrlib.cms.user.Student;

public class StudentCourseCatalog {
	protected long catalogId;
	protected long studentUuid;
	protected long academicCounselorUuid;
	protected List<Course> courseList;
	protected ProgramType programType;

	public StudentCourseCatalog(final Student student, final AcademicCouncelor academicCouncelor) {
		this.catalogId = -1;
		this.studentUuid = student.getUuid();
		this.academicCounselorUuid = academicCouncelor.getUuid();
	}

	public boolean addCourseToStudentCatalog(Course course) {
		courseList.add(course);
		return true;
	}

	public boolean removeCourseFromStudentCatalog(Course course) {
		courseList.remove(course);
		return true;
	}

	public boolean saveCourseCatalog() {
		//DbHelper.saveCourseCatalog(this);
		return true;
	}
}
