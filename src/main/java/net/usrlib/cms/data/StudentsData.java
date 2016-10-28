package net.usrlib.cms.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.usrlib.cms.sql.UsersTable;
import net.usrlib.cms.user.UserRole;
import net.usrlib.cms.util.CsvDataLoader;
import net.usrlib.cms.util.DbHelper;

public class StudentsData extends CsvDataLoader {
	public static final String FILE_NAME = "students.csv";

	public StudentsData() {
		super(FILE_NAME);
	}

	@Override
	public void populateCsvDataToDb(final String[] rawDataArray) {
		if (rawDataArray.length == 0) {
			return;
		}

		PreparedStatement preparedStatement = null;

		// uuid, name, address, phone
		// 8,REBECCA CURRY,692 Ashley Court 92876,9636667844

		for (String line : rawDataArray) {
			String[] parts = line.split(",");
			if (parts.length > 0) {
				try {
					preparedStatement = DbHelper.getConnection().prepareStatement(UsersTable.INSERT_SQL);
					preparedStatement.setInt(1, Integer.valueOf(parts[0]));
					preparedStatement.setString(2, parts[1]);
					preparedStatement.setString(3, parts[2]);
					preparedStatement.setString(4, parts[3]);
					preparedStatement.setInt(5, UserRole.STUDENT.ordinal());

					preparedStatement.execute();
				} catch (SQLException e) {
					//e.printStackTrace();
				}
			}
		}
	}

	public static final void load() {
		new StudentsData();
	}
}
