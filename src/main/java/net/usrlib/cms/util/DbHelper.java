package net.usrlib.cms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.usrlib.cms.sql.AcademicRecordsTable;
import net.usrlib.cms.sql.CourseAssignmentsTable;
import net.usrlib.cms.sql.CoursePrerequisitesTable;
import net.usrlib.cms.sql.CourseRequestsTable;
import net.usrlib.cms.sql.CoursesTable;
import net.usrlib.cms.sql.UsersTable;

public class DbHelper {
	public static final String SQLITE_JDBC_DRIVER = "org.sqlite.JDBC";
	public static final String DB_CONNECTION_URL  = "jdbc:sqlite:cs6310.db";
	public static Connection connection = null;

	public static final Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName(SQLITE_JDBC_DRIVER);
				connection = DriverManager.getConnection(DB_CONNECTION_URL);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return connection;
	}

	public static final void closeConnection() {
		if (connection == null) {
			return;
		}

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static final void createTables() {
		final Connection conn = getConnection();

		if (conn == null) {
			return;
		}

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(CoursesTable.CREATE_TABLE);
			preparedStatement.execute();

			preparedStatement = conn.prepareStatement(UsersTable.CREATE_TABLE);
			preparedStatement.execute();

			preparedStatement = conn.prepareStatement(AcademicRecordsTable.CREATE_TABLE);
			preparedStatement.execute();

			preparedStatement = conn.prepareStatement(CourseAssignmentsTable.CREATE_TABLE);
			preparedStatement.execute();

			preparedStatement = conn.prepareStatement(CoursePrerequisitesTable.CREATE_TABLE);
			preparedStatement.execute();

			preparedStatement = conn.prepareStatement(CourseRequestsTable.CREATE_TABLE);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static final void dropTables() {
		final Connection conn = getConnection();

		if (conn == null) {
			return;
		}

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(CoursesTable.DROP_TABLE);
			preparedStatement.execute();

			preparedStatement = conn.prepareStatement(UsersTable.DROP_TABLE);
			preparedStatement.execute();

			preparedStatement = conn.prepareStatement(AcademicRecordsTable.DROP_TABLE);
			preparedStatement.execute();

			preparedStatement = conn.prepareStatement(CourseAssignmentsTable.DROP_TABLE);
			preparedStatement.execute();

			preparedStatement = conn.prepareStatement(CoursePrerequisitesTable.DROP_TABLE);
			preparedStatement.execute();

			preparedStatement = conn.prepareStatement(CourseRequestsTable.DROP_TABLE);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static final ResultSet doSql(final String sqlStr) {
		final Connection conn = getConnection();

		if (conn == null || sqlStr == null) {
			return null;
		}

		PreparedStatement preparedStatement;
		ResultSet resultSet = null;

		try {
			preparedStatement = conn.prepareStatement(sqlStr);

			if (preparedStatement == null) {
				return null;
			}

			resultSet = preparedStatement.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultSet;
	}

	public static final int getCountOf(final String sqlStr) {
		final Connection conn = getConnection();
		int total = -1;

		if (conn == null) {
			return total;
		}

		ResultSet resultSet = DbHelper.doSql(sqlStr);

		try {
			total = resultSet.getInt("total");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return total;
	}
}
