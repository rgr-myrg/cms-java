package net.usrlib.cms.sql;

public class CourseRequestsTable {
	public static final String TABLE_NAME = "COURSE_REQUESTS";

	public static final String ID_COLUMN = "_id";
	public static final String STUDENT_ID_COLUMN  = "studentUuid";
	public static final String COURSE_ID_COLUMN = "courseId";

	public static final String CREATE_TABLE = String.format(
			"CREATE TABLE IF NOT EXISTS %s (" 
					+ "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "%s LONG NOT NULL,"
					+ "%s LONG NOT NULL,"
					+ "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
					+ ")",
					TABLE_NAME, ID_COLUMN, STUDENT_ID_COLUMN, COURSE_ID_COLUMN
	);

	public static final String DROP_TABLE = String.format("DROP TABLE %s", TABLE_NAME);

	public static final String INSERT_SQL = String.format(
			"INSERT INTO %s (%s, %s) VALUES (?, ?)", 
			TABLE_NAME, STUDENT_ID_COLUMN, COURSE_ID_COLUMN
	);

	public static final String SELECT_COUNT = String.format(
			"SELECT COUNT(%s) AS total FROM %s", 
			ID_COLUMN, TABLE_NAME
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