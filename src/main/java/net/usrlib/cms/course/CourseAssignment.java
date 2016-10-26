package net.usrlib.cms.course;

public class CourseAssignment {
	private int courseId;
	private int instructorUuid;
	private int capacity;
	private Semester semester;

	public CourseAssignment(final String data) {
		String[] parts = data.split(",");

		// Ex: 13,3
		if (parts.length < 2) {
			return;
		}

		this.courseId = Integer.valueOf(parts[0]);
		this.capacity = Integer.valueOf(parts[1]);
	}

	public int getCourseId() {
		return courseId;
	}

	public int getInstructorUuid() {
		return instructorUuid;
	}

	public int getCapacity() {
		return capacity;
	}

	public Semester getSemester() {
		return semester;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
