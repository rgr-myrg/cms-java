package net.usrlib.cms.user;

import net.usrlib.cms.course.StudentCourseCatalog;

public class AcademicCouncelor extends User {
	protected StudentCourseCatalog studentCourseCatalog;
	//protected UserRole role = UserRole.ACADEMIC_COUNCELOR;

//	public StudentCourseCatalog createCourseCatalogForStudent(final Student student) {
//		studentCourseCatalog = DbHelper.createStudentCatalogForStudent(this, student);
//		return studentCourseCatalog;
//	}
//
//	public StudentCourseCatalog retrieveCourseCatalogForStudent(final Student student) {
//		studentCourseCatalog = DbHelper.retrieveStudentCatalogForStudent(this, student);
//		return studentCourseCatalog;
//	}
//
//	public boolean addCourseToStudentCatalog(final Student student, final Course course) {
//		studentCourseCatalog = retrieveCourseCatalogForStudent(student);
//		studentCourseCatalog.addCourseToStudentCatalog(course);
//		studentCourseCatalog.saveCourseCatalog();
//
//		return true;
//	}
//
//	public boolean removeCourseFromStudentCatalog(final Student student, final Course course) {
//		studentCourseCatalog = retrieveCourseCatalogForStudent(student);
//		studentCourseCatalog.removeCourseFromStudentCatalog(course);
//		studentCourseCatalog.saveCourseCatalog();
//
//		return true;
//	}
}
