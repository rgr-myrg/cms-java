package net.usrlib.cms.sql;

import net.usrlib.cms.user.UserRole;

public class UsersTable {
	public static final String TABLE_NAME = "USERS";

	public static final String ID_COLUMN  = "_id";
	public static final String USER_ID_COLUMN    = "userUuid";
	public static final String NAME_COLUMN       = "name";
	public static final String ADDRESS_COLUMN    = "address";
	public static final String PHONE_COLUMN      = "phone";
	public static final String USER_ROLE         = "userRole";
	public static final String TIMESTAMP_COLUMN  = "timestamp";

	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
											+ TABLE_NAME + "("
												+ ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
												+ USER_ID_COLUMN + " LONG NOT NULL,"
												+ NAME_COLUMN + " TEXT NOT NULL,"
												+ ADDRESS_COLUMN + " TEXT,"
												+ PHONE_COLUMN + " LONG,"
												+ USER_ROLE + " INTEGER NOT NULL,"
												+ TIMESTAMP_COLUMN + " DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
											//	+ "UNIQUE (" + NAME_COLUMN + ") ON CONFLICT REPLACE"
											+ ")";

	public static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME;

	public static final String INSERT_SQL = "INSERT INTO " + TABLE_NAME 
											+ "(" 
												+ USER_ID_COLUMN + "," 
												+ NAME_COLUMN + ","
												+ ADDRESS_COLUMN + ","
												+ PHONE_COLUMN + ","
												+ USER_ROLE
											+ ") VALUES (?, ?, ?, ?, ?)";

	public static final String SELECT_COUNT_BY_STUDENT_TYPE = "SELECT COUNT(DISTINCT " + USER_ID_COLUMN + ") AS total FROM " 
															+ TABLE_NAME 
															+ " WHERE " 
															+ USER_ROLE + "=" + UserRole.STUDENT.ordinal();

	public static final String SELECT_COUNT_BY_STUDENT_TYPE_WITHOUT_CLASSES = SELECT_COUNT_BY_STUDENT_TYPE
																			+ " AND " + USER_ID_COLUMN
																			+ " NOT IN ("
																			+ " SELECT " + RecordsTable.STUDENT_ID_COLUMN
																			+ " FROM " + RecordsTable.TABLE_NAME
																			+ ")";

	public static final String SELECT_COUNT_BY_INSTRUCTOR_TYPE = "SELECT COUNT(DISTINCT " + USER_ID_COLUMN + ") AS total FROM " 
																+ TABLE_NAME 
																+ " WHERE " 
																+ USER_ROLE + "=" + UserRole.INSTRUCTOR.ordinal();

	public static final String SELECT_COUNT_BY_INSTRUCTOR_TYPE_WITHOUT_CLASSES = SELECT_COUNT_BY_INSTRUCTOR_TYPE
																				+ " AND " + USER_ID_COLUMN
																				+ " NOT IN ("
																				+ " SELECT " + RecordsTable.INSTRUCTOR_ID_COLUMN
																				+ " FROM " + RecordsTable.TABLE_NAME
																				+ ")";

	public static final String SELECT_SQL = "SELECT " 
											+ USER_ID_COLUMN + "," 
											+ NAME_COLUMN + ","
											+ ADDRESS_COLUMN + ","
											+ PHONE_COLUMN + ","
											+ USER_ROLE
											+ " FROM " + TABLE_NAME;
}
