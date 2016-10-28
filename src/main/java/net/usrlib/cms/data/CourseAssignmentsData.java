package net.usrlib.cms.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.usrlib.cms.sql.CourseAssignmentsTable;
import net.usrlib.cms.util.CsvDataLoader;
import net.usrlib.cms.util.DbHelper;

public class CourseAssignmentsData extends CsvDataLoader {
	public static final String FILE_NAME = "assignments.csv";

	public CourseAssignmentsData() {
		super(FILE_NAME);
	}

	@Override
	public void populateCsvDataToDb(final String[] rawDataArray) {
		if (rawDataArray.length == 0) {
			return;
		}

		PreparedStatement preparedStatement = null;

		// instructorUuid, courseId, capacity
		// 2,13,2

		for (String line : rawDataArray) {
			String[] parts = line.split(",");
			if (parts.length > 0) {
				try {
					preparedStatement = DbHelper.getConnection().prepareStatement(CourseAssignmentsTable.INSERT_SQL);
					preparedStatement.setInt(1, Integer.valueOf(parts[0]));
					preparedStatement.setInt(2, Integer.valueOf(parts[1]));
					preparedStatement.setInt(3, Integer.valueOf(parts[2]));

					preparedStatement.execute();
				} catch (SQLException e) {
					//e.printStackTrace();
				}
			}
		}
	}

	public static final void load() {
		new CourseAssignmentsData();
	}
}
