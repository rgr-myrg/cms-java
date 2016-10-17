package net.usrlib.cms.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.usrlib.cms.sql.CourseRequestsTable;
import net.usrlib.cms.util.CsvDataLoader;

public class CourseRequestsData extends CsvDataLoader {
	public static final String FILE_NAME = "requests.csv";

	public CourseRequestsData() {
		super(FILE_NAME);
	}

	@Override
	public void populateCsvDataToDb(final Connection connection, final String[] rawDataArray) {
		if (connection == null || rawDataArray.length == 0) {
			return;
		}

		PreparedStatement preparedStatement = null;

		// studentUuid, courseId
		// 9,13

		for (String line : rawDataArray) {
			String[] parts = line.split(",");
			if (parts.length > 0) {
				try {
					preparedStatement = connection.prepareStatement(CourseRequestsTable.INSERT_SQL);
					preparedStatement.setLong(1, Long.valueOf(parts[0]));
					preparedStatement.setLong(2, Long.valueOf(parts[1]));

					preparedStatement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final void load() {
		new CourseRequestsData();
	}
}
