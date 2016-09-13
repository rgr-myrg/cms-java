package net.usrlib.cms;

public class Course {
	protected long courseId;
	protected String title;
	protected String description;
	protected String term;

	protected boolean isEnabled = true;

	// add field to lock record while editing

	public void enableCourse() {
		isEnabled = true;
	}

	public void disableCourse() {
		isEnabled = false;
	}

	public boolean saveCourse() {
		return true;
	}
}
