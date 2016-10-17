package net.usrlib.cms.util;

import java.io.IOException;
import java.sql.Connection;

public class CsvDataLoader {
	public CsvDataLoader(final String fileName) {
		String[] rawDataArray = null;
		Connection connection = null;

		try {
			rawDataArray = loadDataFromFile(fileName);
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}

		connection = DbHelper.getConnection();

		if (connection != null && rawDataArray != null) {
			populateCsvDataToDb(connection, rawDataArray);
			DbHelper.closeConnection(connection);
		}
	}

	private final String[] loadDataFromFile(final String fileName) throws InvalidInputException {
		String rawData = null;

		try {
			rawData = FileUtil.readFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
			rawData = null;
		}

		if (rawData == null) {
			throw InvalidInputException.unableToReadFile(fileName);
		}

		return rawData.split("\n");
	}

	// Override in subclass if needed
	public void populateCsvDataToDb(final Connection connection, final String[] rawDataArray) {}
}
