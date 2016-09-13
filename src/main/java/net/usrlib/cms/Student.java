package net.usrlib.cms;

import java.util.List;

public class Student extends User {
	//protected UserRole role = UserRole.STUDENT;
	protected EnrollmentStatus enrollmentStatus;
	protected List<StudentAcademicRecord> studentRecords;

	public long[] getCourseCatalogList() {
		return new long[1];
	}

	public boolean registerForCourseWithId(long courseId) {
		return true;
	}

	public boolean withdrawFromCourseWithId(long courseId) {
		return true;
	}

	public void viewStudentCourseCatalog() {
		
	}
}
