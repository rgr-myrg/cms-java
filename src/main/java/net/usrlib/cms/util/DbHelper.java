package net.usrlib.cms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.usrlib.cms.sql.CoursesTable;
import net.usrlib.cms.sql.RecordsTable;
import net.usrlib.cms.sql.UsersTable;

public class DbHelper {
	public static final String SQLITE_JDBC_DRIVER = "org.sqlite.JDBC";
	public static final String DB_CONNECTION_URL  = "jdbc:sqlite:cs6310.db";

	public static final Connection getConnection() {
		Connection connection = null;

		try {
			Class.forName(SQLITE_JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_CONNECTION_URL);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

	public static final void closeConnection(final Connection connection) {
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
		final Connection connection = getConnection();

		if (connection == null) {
			return;
		}

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(CoursesTable.CREATE_TABLE);
			preparedStatement.execute();

			preparedStatement = connection.prepareStatement(UsersTable.CREATE_TABLE);
			preparedStatement.execute();

			preparedStatement = connection.prepareStatement(RecordsTable.CREATE_TABLE);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		closeConnection(connection);
	}

	public static final void dropTables() {
		final Connection connection = getConnection();

		if (connection == null) {
			return;
		}

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(CoursesTable.DROP_TABLE);
			preparedStatement.execute();

			preparedStatement = connection.prepareStatement(UsersTable.DROP_TABLE);
			preparedStatement.execute();

			preparedStatement = connection.prepareStatement(RecordsTable.DROP_TABLE);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		closeConnection(connection);
	}

	public static final ResultSet doSql(final Connection connection, final String sqlStr) {
		if (connection == null || sqlStr == null) {
			return null;
		}

		PreparedStatement preparedStatement;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement(sqlStr);

			if (preparedStatement == null) {
				return null;
			}

			resultSet = preparedStatement.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultSet;
	}

	public static final int getCountOf(final Connection connection, final String sqlStr) {
		int total = -1;

		if (connection == null) {
			return total;
		}

		ResultSet resultSet = DbHelper.doSql(connection, sqlStr);

		try {
			total = resultSet.getInt("total");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return total;
	}
}
