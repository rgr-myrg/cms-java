package net.usrlib.cms;

import java.sql.Date;

import net.usrlib.cms.course.Course;

public class StudentAcademicRecord {
	protected long studentAcademicRecordId;
	protected long studentUuid;
	protected long instructorUuid;
	protected long courseId;

	protected LetterGrade letterGrade;
	protected String comments;
	protected Date createdDate;
	protected Date lastModifiedDate;

	public StudentAcademicRecord(final Instructor instructor, final Student student, final Course course) {
		this.studentAcademicRecordId = -1;
		this.studentUuid = student.uuid;
		this.instructorUuid = instructor.uuid;
	//	this.courseId = course.courseId;
	}

	public boolean addComments(final String comments) {
		this.comments = comments;
		return true;
	}

	public boolean setLetterGrade(final LetterGrade letterGrade) {
		this.letterGrade = letterGrade;
		return true;
	}

	public boolean saveAcademicRecord() {
		//DbHelper.saveAcademicRecord(this);
		return true;
	}
}
