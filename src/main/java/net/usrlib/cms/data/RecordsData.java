package net.usrlib.cms.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.usrlib.cms.course.LetterGrade;
import net.usrlib.cms.sql.RecordsTable;
import net.usrlib.cms.util.CsvDataLoader;

public class RecordsData extends CsvDataLoader {
	public static final String FILE_NAME = "records.csv";

	public RecordsData() {
		super(FILE_NAME);
	}

	@Override
	public void populateCsvDataToDb(final Connection connection, final String[] rawDataArray) {
		if (connection == null || rawDataArray.length == 0) {
			return;
		}

		PreparedStatement preparedStatement = null;

		// student uuid, course id, instructor id, comments, letter grade
		// 16,8,3,completes work with quality in mind,D

		for (String line : rawDataArray) {
			String[] parts = line.split(",");
			if (parts.length > 0) {
				try {
					preparedStatement = connection.prepareStatement(RecordsTable.INSERT_SQL);
					preparedStatement.setLong(1, Long.valueOf(parts[0]));
					preparedStatement.setLong(2, Long.valueOf(parts[1]));
					preparedStatement.setLong(3, Long.valueOf(parts[2]));
					preparedStatement.setString(4, parts[3]);
					preparedStatement.setInt(5, LetterGrade.valueOf(parts[4]).ordinal());

					preparedStatement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final void load() {
		new RecordsData();
	}
}
