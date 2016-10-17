package net.usrlib.cms.main;

import java.util.Scanner;

import net.usrlib.cms.digest.Digest;
import net.usrlib.cms.util.DbHelper;

public class Main {
	public static boolean done = false;

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());

		startUpWithDigest();
		enterCommandLoop();
	}

	public static void startUpWithDigest() {
		DbHelper.createTables();
		Digest.loadCsvData();
		Digest.connectToDb();

		// Digest Output
		System.out.println(Digest.getNumberOfCourses());

		Digest.disconnectFromDb();
	}

	public static void enterCommandLoop() {
		final Scanner scanner = new Scanner(System.in);
		do {
			System.out.print(System.lineSeparator() + "Course Management System. Enter 'quit' when finished: ");

			String command = scanner.next();

			if (command.equals("quit")) {
				scanner.close();
				done = true;
				exitCommandLoop();

			} else if (command.equals("display_requests")) {
				displayRequests();

			} else if (command.equals("display_seats")) {
				displaySeats();

			} else if (command.equals("display_records")) {
				displayRecords();

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
		log("> stopping the command loop.");
		DbHelper.dropTables();
	}

	public static void displayRequests() {

	}

	public static void displaySeats() {

	}

	public static void displayRecords() {

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

	public final static class ShutdownHook extends Thread {
		@Override
		public void run() {
			if (!done) {
				exitCommandLoop();
			}
		}
	}
}
