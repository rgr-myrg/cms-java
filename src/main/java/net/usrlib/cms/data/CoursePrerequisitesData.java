package net.usrlib.cms.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.usrlib.cms.sql.CoursePrerequisitesTable;
import net.usrlib.cms.util.CsvDataLoader;

public class CoursePrerequisitesData extends CsvDataLoader {
	public static final String FILE_NAME = "prereqs.csv";

	public CoursePrerequisitesData() {
		super(FILE_NAME);
	}

	@Override
	public void populateCsvDataToDb(final Connection connection, final String[] rawDataArray) {
		if (connection == null || rawDataArray.length == 0) {
			return;
		}

		PreparedStatement preparedStatement = null;

		// preReqCourseId, courseId
		// 2,10

		for (String line : rawDataArray) {
			String[] parts = line.split(",");
			if (parts.length > 0) {
				try {
					preparedStatement = connection.prepareStatement(CoursePrerequisitesTable.INSERT_SQL);
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
		new CoursePrerequisitesData();
	}
}
