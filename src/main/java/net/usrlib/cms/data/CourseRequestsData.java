package net.usrlib.cms.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.usrlib.cms.sql.CourseRequestsTable;
import net.usrlib.cms.util.CsvDataLoader;
import net.usrlib.cms.util.DbHelper;

public class CourseRequestsData extends CsvDataLoader {
	public static final String FILE_NAME = "requests.csv";

	public CourseRequestsData() {
		super(FILE_NAME);
	}

	@Override
	public void populateCsvDataToDb(final String[] rawDataArray) {
		if (rawDataArray.length == 0) {
			return;
		}

		PreparedStatement preparedStatement = null;

		// studentUuid, courseId
		// 9,13

		for (String line : rawDataArray) {
			String[] parts = line.split(",");
			if (parts.length > 0) {
				try {
					preparedStatement = DbHelper.getConnection().prepareStatement(CourseRequestsTable.INSERT_SQL);
					preparedStatement.setInt(1, Integer.valueOf(parts[0]));
					preparedStatement.setInt(2, Integer.valueOf(parts[1]));

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
