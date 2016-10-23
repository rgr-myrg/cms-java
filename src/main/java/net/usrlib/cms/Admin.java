package net.usrlib.cms;

import net.usrlib.cms.course.Course;

public class Admin extends User {
	//protected UserRole role = UserRole.ADMIN;

	public boolean enrollNewStudent(final Student student) {
		return true;
	}

	public boolean disenrollStudent(final Student student) {
		return true;
	}

	public boolean createNewCourse(final Course course) {
		return true;
	}

	public boolean updateCourse(final Course course) {
		return true;
	}

	public boolean removeCourse(final Course course) {
		return true;
	}
}
