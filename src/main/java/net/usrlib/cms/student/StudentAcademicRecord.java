package net.usrlib.cms.student;

import java.sql.Date;

import net.usrlib.cms.course.LetterGrade;
import net.usrlib.cms.course.Semester;

public class StudentAcademicRecord {
	private int studentAcademicRecordId;
	private int studentUuid;
	private int instructorUuid;
	private int courseId;
	private LetterGrade letterGrade;
	private String instructorComments;
	private Date lastModifiedDate;
	private Semester academicTerm;

	public StudentAcademicRecord (
				int studentUuid, 
				int courseId, 
				int instructorUuid, 
				String instructorComments, 
				LetterGrade letterGrade) {

		this.studentUuid = studentUuid;
		this.courseId = courseId;
		this.instructorUuid = instructorUuid;
		this.instructorComments = instructorComments;
		this.letterGrade = letterGrade;
	}

	public StudentAcademicRecord(final String data) {
		// Ex: 15,8,2,nice job,A
		final String[] parts = data.split(",");

		if (parts.length < 5) {
			return;
		}

		this.studentUuid = Integer.valueOf(parts[0]);
		this.courseId = Integer.valueOf(parts[1]);
		this.instructorUuid = Integer.valueOf(parts[2]);
		this.instructorComments = parts[3];
		this.letterGrade = LetterGrade.valueOf(parts[4]);
	}

	public int getStudentAcademicRecordId() {
		return studentAcademicRecordId;
	}

	public int getStudentUuid() {
		return studentUuid;
	}

	public int getInstructorUuid() {
		return instructorUuid;
	}

	public int getCourseId() {
		return courseId;
	}

	public LetterGrade getLetterGrade() {
		return letterGrade;
	}

	public String getInstructorComments() {
		return instructorComments;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public Semester getAcademicTerm() {
		return academicTerm;
	}
}
