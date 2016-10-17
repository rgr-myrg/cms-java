package net.usrlib.cms.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.usrlib.cms.sql.CourseAssignmentsTable;
import net.usrlib.cms.util.CsvDataLoader;

public class CourseAssignmentsData extends CsvDataLoader {
	public static final String FILE_NAME = "assignments.csv";

	public CourseAssignmentsData() {
		super(FILE_NAME);
	}

	@Override
	public void populateCsvDataToDb(final Connection connection, final String[] rawDataArray) {
		if (connection == null || rawDataArray.length == 0) {
			return;
		}

		PreparedStatement preparedStatement = null;

		// instructorUuid, courseId, capacity
		// 2,13,2

		for (String line : rawDataArray) {
			String[] parts = line.split(",");
			if (parts.length > 0) {
				try {
					preparedStatement = connection.prepareStatement(CourseAssignmentsTable.INSERT_SQL);
					preparedStatement.setLong(1, Long.valueOf(parts[0]));
					preparedStatement.setLong(2, Long.valueOf(parts[1]));
					preparedStatement.setLong(3, Long.valueOf(parts[2]));

					preparedStatement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final void load() {
		new CourseAssignmentsData();
	}
}
