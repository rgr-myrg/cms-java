package net.usrlib.cms.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.usrlib.cms.course.LetterGrade;
import net.usrlib.cms.sql.AcademicRecordsTable;
import net.usrlib.cms.util.CsvDataLoader;
import net.usrlib.cms.util.DbHelper;

public class RecordsData extends CsvDataLoader {
	public static final String FILE_NAME = "records.csv";

	public RecordsData() {
		super(FILE_NAME);
	}

	@Override
	public void populateCsvDataToDb(final String[] rawDataArray) {
		if (rawDataArray.length == 0) {
			return;
		}

		PreparedStatement preparedStatement = null;

		// student uuid, course id, instructor id, comments, letter grade
		// 16,8,3,completes work with quality in mind,D

		for (String line : rawDataArray) {
			String[] parts = line.split(",");
			if (parts.length > 0) {
				try {
					preparedStatement = DbHelper.getConnection().prepareStatement(AcademicRecordsTable.INSERT_SQL);
					preparedStatement.setInt(1, Integer.valueOf(parts[0]));
					preparedStatement.setInt(2, Integer.valueOf(parts[1]));
					preparedStatement.setInt(3, Integer.valueOf(parts[2]));
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
