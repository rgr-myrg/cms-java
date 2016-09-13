package net.usrlib.cms;

public class DbHelper {
	public static final StudentCourseCatalog createStudentCatalogForStudent(final AcademicCouncelor academicCouncelor, final Student student) {
		return new StudentCourseCatalog(student, academicCouncelor);
	}

	public static final StudentCourseCatalog retrieveStudentCatalogForStudent(final AcademicCouncelor academicCouncelor, final Student student) {
		return new StudentCourseCatalog(student, academicCouncelor);
	}

	public static final StudentCourseCatalog saveCourseCatalog(final StudentCourseCatalog studentCourseCatalog) {
		return studentCourseCatalog;
	}

	public static final StudentAcademicRecord saveAcademicRecord(final StudentAcademicRecord studentAcademicRecord) {
		// should always return the object being operated on
		return studentAcademicRecord;
	}
}
