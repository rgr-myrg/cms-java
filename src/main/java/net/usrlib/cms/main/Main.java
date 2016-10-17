package net.usrlib.cms.main;

import net.usrlib.cms.digest.Digest;
import net.usrlib.cms.util.DbHelper;
import net.usrlib.cms.util.InvalidInputException;

public class Main {
	public static void main(String[] args) throws InvalidInputException {
		DbHelper.createTables();

		Digest.loadCsvData();
		Digest.connectToDb();

		Digest.disconnectFromDb();
		DbHelper.dropTables();
	}
}
