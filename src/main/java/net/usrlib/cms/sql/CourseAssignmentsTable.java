package net.usrlib.cms.sql;

public class CourseAssignmentsTable {
	public static final String TABLE_NAME = "COURSE_ASSIGNMENTS";

	public static final String ID_COLUMN = "_id";
	public static final String INSTRUCTOR_ID_COLUMN  = "instructorUuid";
	public static final String COURSE_ID_COLUMN = "courseId";
	public static final String CAPACITY_COLUMN  = "capacity";

	public static final String CREATE = String.format(
			"CREATE TABLE IF NOT EXISTS %s (" 
					+ "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "%s LONG NOT NULL,"
					+ "%s LONG NOT NULL,"
					+ "%s LONG NOT NULL,"
					+ "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
					+ ")",
					TABLE_NAME, ID_COLUMN, INSTRUCTOR_ID_COLUMN, COURSE_ID_COLUMN, CAPACITY_COLUMN
	);

	public static final String DROP_TABLE = String.format("DROP TABLE %s", TABLE_NAME);

	public static final String INSERT_SQL = String.format(
			"INSERT INTO %s (%d, %d, %d) VALUES (?, ?, ?)", 
			TABLE_NAME, INSTRUCTOR_ID_COLUMN, COURSE_ID_COLUMN, CAPACITY_COLUMN
	);

	public static final String SELECT_COUNT = String.format(
			"SELECT COUNT(%s) AS total FROM %s", 
			ID_COLUMN, TABLE_NAME
	);
}

/*
The data provided in the assignments.csv file represents the number of seats available for each of
the courses in the catalog. Each record in this file has three fields of integers: (1) The identifier of the
instructor teaching the course; (2) the identifier of the course being taught; and, (3) the number of
seats that can be provided/students that can be taught. The first line of the file below represents the
fact that instructor 2 (Everett Kim) will teach course 13 (Machine Learning) and can support two
students (available seats). The fourth and sixth lines of the file represent the fact that instructors 5
(Joseph Lawson) and 8 (Rebecca Curry) are available to teach 1 seat of course 29 (Parallel Computing)
each, for a total of 2 available seats. Here is an example of the assignments.csv file:
2,13,2
5,17,1
3,10,1
5,29,1
2,19,1
8,29,1
3,28,5
*/