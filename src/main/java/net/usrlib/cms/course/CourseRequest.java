package net.usrlib.cms.course;

public class CourseRequest {
	private int studentUuid;
	private int courseId;
	private CourseRequestStatus courseRequestStatus;
	private CourseRequestRemark courseRequestRemark;

	public CourseRequest(int studentUuid, int courseId) {
		this.studentUuid = studentUuid;
		this.courseId = courseId;
	}

	public void setCourseRequestStatus(CourseRequestStatus courseRequestStatus) {
		this.courseRequestStatus = courseRequestStatus;
	}

	public void setCourseRequestRemark(CourseRequestRemark courseRequestRemark) {
		this.courseRequestRemark = courseRequestRemark;
	}

	public int getStudentUuid() {
		return studentUuid;
	}

	public int getCourseId() {
		return courseId;
	}

	public CourseRequestStatus getCourseRequestStatus() {
		return courseRequestStatus;
	}

	public CourseRequestRemark getCourseRequestRemark() {
		return courseRequestRemark;
	}
}
