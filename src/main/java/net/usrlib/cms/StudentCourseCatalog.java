package net.usrlib.cms;

import java.util.List;

public class StudentCourseCatalog {
	protected long catalogId;
	protected long studentUuid;
	protected long academicCounselorUuid;
	protected List<Course> courseList;

	public StudentCourseCatalog(final Student student, final AcademicCouncelor academicCouncelor) {
		this.catalogId = -1;
		this.studentUuid = student.uuid;
		this.academicCounselorUuid = academicCouncelor.uuid;
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
		DbHelper.saveCourseCatalog(this);
		return true;
	}
}
