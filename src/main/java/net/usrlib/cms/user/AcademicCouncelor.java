package net.usrlib.cms.user;

import net.usrlib.cms.course.Course;
import net.usrlib.cms.student.StudentCourseCatalog;

public class AcademicCouncelor extends User {
	public StudentCourseCatalog createCourseCatalogForStudent(final Student student) {
		return new StudentCourseCatalog(student, this);
	}

	public StudentCourseCatalog retrieveCourseCatalogForStudent(final Student student) {
		return new StudentCourseCatalog(student, this);
	}

	public StudentCourseCatalog addCourseToStudentCatalog(final Student student, final Course course) {
		StudentCourseCatalog studentCourseCatalog = retrieveCourseCatalogForStudent(student);
		studentCourseCatalog.addCourseToStudentCatalog(course);
		studentCourseCatalog.saveCourseCatalog();

		return studentCourseCatalog;
	}

	public StudentCourseCatalog removeCourseFromStudentCatalog(final Student student, final Course course) {
		StudentCourseCatalog studentCourseCatalog = retrieveCourseCatalogForStudent(student);
		studentCourseCatalog.removeCourseFromStudentCatalog(course);
		studentCourseCatalog.saveCourseCatalog();

		return studentCourseCatalog;
	}

	public void removeCourseCatalogForStudent(final Student student) {
		
	}
}
