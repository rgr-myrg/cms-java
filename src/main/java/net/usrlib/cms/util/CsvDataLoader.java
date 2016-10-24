package net.usrlib.cms.util;

import java.io.IOException;

public class CsvDataLoader {
	public CsvDataLoader(final String fileName) {
		String[] rawDataArray = null;

		try {
			rawDataArray = loadDataFromFile(fileName);
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}

		if (rawDataArray != null) {
			populateCsvDataToDb(rawDataArray);
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
	public void populateCsvDataToDb(final String[] rawDataArray) {}
}
