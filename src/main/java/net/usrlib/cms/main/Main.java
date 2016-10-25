package net.usrlib.cms.main;

import java.util.List;
import java.util.Scanner;

import net.usrlib.cms.digest.Digest;
import net.usrlib.cms.logger.Log;
import net.usrlib.cms.util.DbHelper;

public class Main {
	public static boolean done = false;

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());

		if (args != null && args.length > 0 && args[0] != null) {
			final String option = args[0];
			Log.setDebug(option.equalsIgnoreCase("debug"));
		}

		startUpWithDigest();
		enterCommandLoop();
	}

	public static void startUpWithDigest() {
		DbHelper.dropTables();
		DbHelper.createTables();
		Digest.loadCsvData();

		// Digest Main Output
		System.out.println(Digest.getNumberOfCourseRequests());
		System.out.println(Digest.getNumberOfValidCourseRequests());
		System.out.println(Digest.getNumberOfMissingPrerequisites());
		System.out.println(Digest.getNumberOfCourseAlreadyTaken());
		System.out.println(Digest.getNumberOfNoAvailableSeats());
		/*
		(1) the total number of records in the requests.csv file (don’t count blank lines, etc.)
		(2) the number of valid (granted) requests
		(3) the number of requests that were denied because of one or more missing prerequisites
		(4) the number of requests that were denied the course was already taken
		(5) the number of requests that were denied because of a lack of available seats
		 */
	}

	public static void enterCommandLoop() {
		final Scanner scanner = new Scanner(System.in);

		do {
			System.out.print(System.lineSeparator() + "$main: ");

			String command = scanner.next();

			if (command.equals("quit")) {
				scanner.close();
				done = true;
				exitCommandLoop();

			} else if (command.equals("display_requests")) {
				printList(Digest.fetchApprovedRequestsInfo());

			} else if (command.equals("display_seats")) {
				printList(Digest.fetchCourseAssignmentsInfo());

			} else if (command.equals("display_records")) {
				printList(Digest.fetchAcademicRecordsInfo());

			} else if (command.startsWith("check_request")) {
				checkRequest(command);

			} else if (command.startsWith("add_record")) {
				addRecord(command);

			} else if (command.startsWith("add_seats")) {
				addSeats(command);

			} else {
				log(String.format("> unknown command %s", command));
			}
		} while (!done);
	}

	public static void exitCommandLoop() {
		log("> stopping the command loop");
		//DbHelper.dropTables();
		DbHelper.closeConnection();
	}

	public static void checkRequest(final String command) {

	}

	public static void addRecord(final String command) {

	}

	public static void addSeats(final String command) {

	}

	public static void log(final String message) {
		System.out.println(System.lineSeparator() + message);
	}

	public static void printList(final List<String> list) {
		for (String item : list) {
			System.out.println(item);
		}
	}

	public final static class ShutdownHook extends Thread {
		@Override
		public void run() {
			if (!done) {
				exitCommandLoop();
			}
		}
	}
}
