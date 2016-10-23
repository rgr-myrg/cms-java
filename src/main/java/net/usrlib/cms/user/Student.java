package net.usrlib.cms.user;

import java.util.List;

import net.usrlib.cms.EnrollmentStatus;
import net.usrlib.cms.StudentAcademicRecord;
import net.usrlib.cms.course.Course;
import net.usrlib.cms.course.StudentCourseCatalog;

public class Student extends User {
	private EnrollmentStatus enrollmentStatus;
	private List<StudentAcademicRecord> studentRecords;
	private StudentCourseCatalog studentCourseCatalog;
	private List<Course> registeredCourseList;

	public Student(int uuid, String name, String address, String phone, UserRole userRole) {
		super(uuid, name, address, phone, userRole);
	}

	//viewCourseCatalog
	//registerForCourse
	//withdrawFromCourse
	//hasAllCoursePrerequisites
	//canRegisterForCourse
	//isCourseSeatAvailable

	public void registerForCourse(final int courseId) {
		final Course course = new Course();
		course.setCourseId(courseId);

		// check pre-requisites
		// check if course was taken and passed
		// check available seats
	}
}
