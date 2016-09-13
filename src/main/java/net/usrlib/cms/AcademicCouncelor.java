package net.usrlib.cms;

public class AcademicCouncelor extends User {
	protected StudentCourseCatalog studentCourseCatalog;
	//protected UserRole role = UserRole.ACADEMIC_COUNCELOR;

	public StudentCourseCatalog createStudentCatalogForStudent(final Student student) {
		studentCourseCatalog = DbHelper.createStudentCatalogForStudent(this, student);
		return studentCourseCatalog;
	}

	public StudentCourseCatalog retrieveStudentCatalogForStudent(final Student student) {
		studentCourseCatalog = DbHelper.retrieveStudentCatalogForStudent(this, student);
		return studentCourseCatalog;
	}

	public boolean addCourseToStudentCatalog(final Student student, final Course course) {
		studentCourseCatalog = retrieveStudentCatalogForStudent(student);
		studentCourseCatalog.addCourseToStudentCatalog(course);
		studentCourseCatalog.saveCourseCatalog();

		return true;
	}

	public boolean removeCourseFromStudentCatalog(final Student student, final Course course) {
		studentCourseCatalog = retrieveStudentCatalogForStudent(student);
		studentCourseCatalog.removeCourseFromStudentCatalog(course);
		studentCourseCatalog.saveCourseCatalog();

		return true;
	}
}
