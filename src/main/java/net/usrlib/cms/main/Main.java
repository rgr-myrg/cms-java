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
		/*
		(1) the total number of records in the requests.csv file (don’t count blank lines, etc.)
		(2) the number of valid (granted) requests
		(3) the number of requests that were denied because of one or more missing prerequisites
		(4) the number of requests that were denied the course was already taken
		(5) the number of requests that were denied because of a lack of available seats
		 */
		System.out.println(Digest.getNumberOfCourseRequests());
		System.out.println(Digest.getNumberOfValidCourseRequests());
		System.out.println(Digest.getNumberOfMissingPrerequisites());
		System.out.println(Digest.getNumberOfCourseAlreadyTaken());
		System.out.println(Digest.getNumberOfNoAvailableSeats());
	}

	public static void enterCommandLoop() {
		final Scanner scanner = new Scanner(System.in);

		do {
			System.out.print(System.lineSeparator() + "$main: ");

			String command = scanner.next();

			if (scanner.hasNextLine()) {
				command += scanner.nextLine();
			}

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

			} else if (command.startsWith("check_request,")) {
				System.out.println(checkRequest(command));

			} else if (command.startsWith("add_record,")) {
				Digest.addRecord(command.replace("add_record,", ""));

			} else if (command.startsWith("add_seats,")) {
				Digest.addSeats(command.replace("add_seats,", ""));

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

	public static String checkRequest(final String command) {
		// Ex: check_request,15,29
		String[] parts = command.split(",");

		if (parts.length < 2) {
			return "";
		}

		// checkRequest() also processes requests per requirements
		Digest.getNumberOfValidCourseRequests();
		return Digest.checkRequest(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
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
