

import java.io.*;
import java.util.*;

public class friday {

	static boolean isLeap(int year) {
		boolean leap = false;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0) {
					leap = true;
				} else {
					leap = false;
				}
			} else {
				leap = true;
			}
		}
		return leap;
	}

	static int days(int year) {
		int days = 365;
		if (isLeap(year)) {
			days++;
		}
		return days;
	}

	static int[] day13(int year) {
		int[] monthLength = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (isLeap(year)) {
			monthLength[1]++;
		}

		int[] days = new int[12];
		for (int i = 0; i < 12; i++) {
			days[i] = 12;

			if (i != 0) {
				for (int j = 0; j < i; j++) {
					days[i] = days[i] + monthLength[j];
				}
			}
		}
		return days;
	}

	static int sumDays(int startYear, int endYear) { // endYear is not included
		int sum = 0;
		if (endYear > startYear) {
			for (int i = startYear; i < endYear; i++) {
				sum = sum + days(i);
			}
		}
		return sum;
	}

	public static void main(String[] args) throws NumberFormatException,
			IOException {

		BufferedReader in = new BufferedReader(new FileReader("friday.in"));
		BufferedWriter out = new BufferedWriter(new FileWriter("friday.out"));
		int start = 1900;
		int end = 1900 + Integer.parseInt(in.readLine());

		int[] freq = { 0, 0, 0, 0, 0, 0, 0 }; // SSMTWTF

		int preDays = 0;
		int[] inDays = new int[12];
		int totalDay = 0;
		int dayOfWeek = 0; // The day of the week with 0 indicating sat
		for (int year = start; year < end; year++) { // For each year
			preDays = sumDays(start, year);
			inDays = day13(year);
			for (int i = 0; i < 12; i++) {// For each month
				totalDay = preDays + inDays[i];
				dayOfWeek = (totalDay + 2) % 7;
				freq[dayOfWeek]++;

				// System.out.print(dayOfWeek + " ");
			}
			// System.out.println();
		}

		String msg;
		for (int i = 0; i < 7; i++) {

			if (i != 6) {
				msg = Integer.toString(freq[i]) + " ";
			} else {
				msg = Integer.toString(freq[i]);
			}
			out.write(msg);
			// System.out.println(msg);
		}

		/*
		 * int[] temp = day13(1998); for (int i = 0; i < 12; i++) {
		 * System.out.println(temp[i]); }
		 */

		out.newLine();
		out.close();

		System.exit(0);
	}
}
