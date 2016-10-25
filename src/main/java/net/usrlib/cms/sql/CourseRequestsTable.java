package net.usrlib.cms.sql;

import net.usrlib.cms.course.CourseRequestStatus;

public class CourseRequestsTable {
	public static final String TABLE_NAME = "COURSE_REQUESTS";

	public static final String ID_COLUMN = "_id";
	public static final String STUDENT_ID_COLUMN  = "studentUuid";
	public static final String COURSE_ID_COLUMN = "courseId";
	public static final String REQUEST_STATUS_COLUMN = "requestStatus";

	public static final String CREATE_TABLE = String.format(
			"CREATE TABLE IF NOT EXISTS %s (" 
					+ "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "%s INTEGER NOT NULL,"
					+ "%s INTEGER NOT NULL,"
					+ "%s INTEGER NOT NULL,"
					+ "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
					+ ")",
					TABLE_NAME, ID_COLUMN, STUDENT_ID_COLUMN, COURSE_ID_COLUMN, REQUEST_STATUS_COLUMN
	);

	public static final String DROP_TABLE = String.format("DROP TABLE %s", TABLE_NAME);

	public static final String INSERT_SQL = String.format(
			"INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)", 
			TABLE_NAME, STUDENT_ID_COLUMN, COURSE_ID_COLUMN, REQUEST_STATUS_COLUMN
	);

	public static final String SELECT_COUNT = String.format(
			"SELECT COUNT(%s) AS total FROM %s", 
			ID_COLUMN, TABLE_NAME
	);

	public static final String SELECT_OPEN_REQUESTS = String.format(
			"SELECT * FROM %s WHERE %s = %d",
			TABLE_NAME, REQUEST_STATUS_COLUMN, CourseRequestStatus.OPEN.ordinal()
	);

	public static final String UPDATE_REQUESTS_TO_PROCESSED = String.format(
			"UPDATE %s SET %s = %d WHERE %s = ? AND %s = ?",
			TABLE_NAME, REQUEST_STATUS_COLUMN, CourseRequestStatus.APPROVED.ordinal(), STUDENT_ID_COLUMN, COURSE_ID_COLUMN
	);

	public static final String SELECT_APPROVED_REQUESTS = String.format(
			"SELECT * FROM %s WHERE %s = %d",
			TABLE_NAME, REQUEST_STATUS_COLUMN, CourseRequestStatus.APPROVED.ordinal()
	);

	public static final String SELECT_APPROVED_REQUESTS_INFO = String.format(
			"SELECT COURSE_REQUESTS.studentUuid AS userUuid, COURSE_REQUESTS.courseId AS courseId, "
				+ "USERS.name AS name, COURSES.courseTitle AS courseTitle "
				+ "FROM COURSE_REQUESTS "
				+ "LEFT JOIN USERS ON COURSE_REQUESTS.studentUuid = USERS.userUuid "
				+ "LEFT JOIN COURSES ON COURSE_REQUESTS.courseId = COURSES.courseId "
				+ "WHERE %s = %d",
			REQUEST_STATUS_COLUMN, CourseRequestStatus.APPROVED.ordinal()
	);
}

/*
The data provided in the requests.csv file represents the requests from students to take specific
courses as listed in the catalog. Each record has two fields: simply, (1) the ID of the student; and, (2)
the ID of the course being requested. As discussed in the previous design sessions, there are various
reasons why a specific course request might not be granted. Your system must use the information
provided in the various files to determine if each request is valid or not based on the various
requirements. For example, the first line of the file below represents student 9 (Gary Allen)
requesting to take course 13 (Machine Learning). Here is an example of the requests.csv file:
9,13
16,29
15,29
*/