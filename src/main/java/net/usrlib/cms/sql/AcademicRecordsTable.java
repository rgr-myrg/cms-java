package net.usrlib.cms.sql;

public class AcademicRecordsTable {
	public static final String TABLE_NAME = "STUDENT_RECORDS";

	public static final String ID_COLUMN = "_id";
	public static final String STUDENT_ID_COLUMN    = "studentUuid";
	public static final String COURSE_ID_COLUMN     = "courseId";
	public static final String INSTRUCTOR_ID_COLUMN = "instructorUuid";
	public static final String COMMENTS_COLUMN      = "instructorComments";
	public static final String LETTER_GRADE_COLUMN  = "letterGrade";
	public static final String TIMESTAMP_COLUMN     = "timestamp";
 
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
											+ TABLE_NAME + "("
												+ ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
												+ STUDENT_ID_COLUMN + " INTEGER NOT NULL,"
												+ COURSE_ID_COLUMN + " INTEGER NOT NULL,"
												+ INSTRUCTOR_ID_COLUMN + " INTEGER NOT NULL,"
												+ COMMENTS_COLUMN + " TEXT NOT NULL,"
												+ LETTER_GRADE_COLUMN + " INTEGER NOT NULL,"
												+ TIMESTAMP_COLUMN + " DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
												+ ")";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

	public static final String INSERT_SQL = "INSERT INTO " + TABLE_NAME 
											+ "(" 
												+ STUDENT_ID_COLUMN + "," 
												+ COURSE_ID_COLUMN + ","
												+ INSTRUCTOR_ID_COLUMN + ","
												+ COMMENTS_COLUMN + ","
												+ LETTER_GRADE_COLUMN
											+ ") VALUES (?, ?, ?, ?, ?)";

	public static final String SELECT_SQL = "SELECT " 
											+ STUDENT_ID_COLUMN + "," 
											+ COURSE_ID_COLUMN + ","
											+ INSTRUCTOR_ID_COLUMN + ","
											+ COMMENTS_COLUMN + ","
											+ LETTER_GRADE_COLUMN
											+ " FROM " + TABLE_NAME;

	public static final String SELECT_COUNT = "SELECT COUNT(" 
											+ STUDENT_ID_COLUMN 
											+ ") AS total FROM " 
											+ TABLE_NAME;

	public static final String SELECT_COURSE_BY_ID = String.format(
			"SELECT * FROM %s WHERE %s = ? AND %s = ?",
			TABLE_NAME, STUDENT_ID_COLUMN, COURSE_ID_COLUMN
	);

}
