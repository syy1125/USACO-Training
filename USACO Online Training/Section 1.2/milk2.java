

import java.io.*;
import java.util.*;


class FileIO {
	static String[] readFile(String path) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(path));
		List<String> s = new ArrayList<String>();

		String buffer;
		while ((buffer = in.readLine()) != null) {
			s.add(buffer);
		}

		in.close();

		String[] out = s.toArray(new String[s.size()]);
		return out;
	}

	static void writeFile(String path, String[] content) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(path));

		for (int i = 0; i < content.length; i++) {
			out.write(content[i]);
			out.newLine();
		}

		out.close();
	}
}

class Interval {

	SortedSet<Double> starts = new TreeSet<Double>();
	SortedSet<Double> ends = new TreeSet<Double>();

	double[] s;
	double[] e;

	String lastOp = "None"; // For debug uses

	Interval(double start, double end) {
		starts.add(start);
		ends.add(end);
		lastOp = "Creation: (" + start + ", " + end + ")";

		dumpData();
	}

	void examine() { // For debugging use

		if (starts.size() != ends.size()) {
			System.out.println("Warning: starts and ends have unequal sizes.");

			System.out.println("Dumping data...");
			System.out.println("Start size: " + starts.size() + ", ends size: "
					+ ends.size());
			System.out.println("Last operation: " + lastOp);
			System.out.println("Data dump: ");
			rawPrint();
			System.out.println("Breaking now.");
			System.exit(0);
		}

	}

	static int[] optRange(double[] d, double target) {
		// Returns a smaller range of index to search in
		// As {startIndex, endIndex}

		int curBegin = 0;
		int curEnd = d.length;
		int midRange = (curBegin + curEnd) / 2;

		while (curEnd - curBegin > 8) {
			if (target < d[midRange]) {
				curEnd = midRange;
				midRange = (curBegin + curEnd) / 2;

				// target <= d[curBegin]
				// target > d[curEnd]

				// System.out.println("(" + curBegin + ", " + curEnd + ")");
			} else {
				curBegin = midRange;
				midRange = (curBegin + curEnd) / 2;

				// target <= d[curBegin]
				// target > d[curEnd]

				// System.out.println("(" + curBegin + ", " + curEnd + ")");
			}
		}

		int[] out = { curBegin, curEnd };
		return out;

	}

	void dumpData() { // Dumps starts and ends into s and e
		s = new double[starts.size()];
		e = new double[ends.size()];
		int sCount = 0;
		int eCount = 0;

		for (double dS : starts) {
			s[sCount] = dS;
			sCount++;
		}
		for (double dE : ends) {
			e[eCount] = dE;
			eCount++;
		}
	}

	boolean nonInclusive(double num) { // Treats the interval as (a, b)
		boolean out = false;

		int[] tempS = optRange(s, num);
		int[] tempE = optRange(e, num);

		examine();

		for (int i = Math.min(tempS[0], tempE[0]); i < Math.max(tempS[1],
				tempE[1]); i++) {

			if (num > s[i] && num < e[i]) {
				out = true;
			}

		}

		return out;
	}

	boolean contains(double num) { // Treats the interval as [a, b)
		boolean contains = false;

		int[] tempS = optRange(s, num);
		int[] tempE = optRange(e, num);

		examine();

		for (int i = Math.min(tempS[0], tempE[0]); i < Math.max(tempS[1],
				tempE[1]); i++) {

			if (num >= s[i] && num < e[i]) {
				contains = true;
			}

		}

		return contains;
	}

	boolean invContains(double num) { // Treats the interval as (a, b]
		boolean contains = false;

		int[] tempS = optRange(s, num);
		int[] tempE = optRange(e, num);

		examine();

		for (int i = Math.min(tempS[0], tempE[0]); i < Math.max(tempS[1],
				tempE[1]); i++) {
			if (num > s[i] && num <= e[i]) {
				contains = true;
			}

		}

		return contains;
	}

	boolean touches(double num) { // Treats the interval as [a, b]
		boolean touches = false;

		int[] tempS = optRange(s, num);
		int[] tempE = optRange(e, num);

		// System.out.println(tempS[0] + ", " + tempS[1] + ", " + tempE[0] +
		// ", "
		// + tempE[1]);
		// System.out.println(s.length + ", " + e.length);

		examine();

		for (int i = Math.min(tempS[0], tempE[0]); i < Math.max(tempS[1],
				tempE[1]); i++) {

			if (num >= s[i] && num <= e[i]) {
				touches = true;

			}

		}

		return touches;
	}

	void rawPrint() {

		System.out.println("Starts: ");
		for (int i = 0; i < s.length; i++) {
			if (i != s.length - 1) {
				System.out.print(s[i] + ", ");
			} else {
				System.out.print(s[i]);
			}
		}
		System.out.println();

		System.out.println("ends: ");
		for (int i = 0; i < e.length; i++) {
			if (i != e.length - 1) {
				System.out.print(e[i] + ", ");
			} else {
				System.out.print(e[i]);
			}
		}
		System.out.println();
	}

	void printData() {

		for (int i = 0; i < s.length; i++) {
			try {
				System.out.println("(" + s[i] + ", " + e[i] + ")");
			} catch (ArrayIndexOutOfBoundsException exception) {
				System.out.println("Experienced exception.");
				System.out.println("Attempting to print each independently.");

				try {
					System.out.println("Start: " + s[i]);
				} catch (ArrayIndexOutOfBoundsException exception2) {

				}
				try {
					System.out.println("End: " + e[i]);
				} catch (ArrayIndexOutOfBoundsException exception2) {

				}
			}
		}

	}

	void addInterval(double start, double end) throws IllegalArgumentException {
		// Add an interval to the current one

		if (start > end) {
			throw new IllegalArgumentException();
		} else {

			boolean startBetween = false;
			boolean endBetween = false;

			for (int i = 0; i < s.length; i++) {
				// System.out.println("(" + s[i] + ", " + e[i] + ")");
				if (this.touches(start) && !startBetween) {
					startBetween = true;
					// System.out.println("Start: " + startBetween);
				}
				if (this.touches(end) && !endBetween) {
					endBetween = true;
					// System.out.println("End: " + endBetween);
				}
			}

			// Wipe out any interval within the inserted interval
			int[] i11 = optRange(s, start);
			int[] i12 = optRange(s, end);
			int[] i21 = optRange(e, start);
			int[] i22 = optRange(e, end);

			Interval temp = new Interval(start, end);
			for (int i = Math.min(i12[0], i21[0]); i < Math.max(i12[1], i22[1]); i++) {
				if (temp.invContains(s[i])) {
					starts.remove(s[i]);
					// System.out.println(s[i]);
					/*
					 * for (double num : starts) { System.out.println(num); }
					 */
				}
				if (temp.contains(e[i])) {
					ends.remove(e[i]);
					// System.out.println(e[i]);
				}
				// System.out.println(i);
			}

			if (!startBetween) {
				starts.add(start);
				// System.out.println(start);
				/*
				 * for (double num : starts) { System.out.println(num); }
				 */
			}
			if (!endBetween) {
				ends.add(end);
				// System.out.println(end);
				/*
				 * for (double num : ends) { System.out.println(num); }
				 */
			}

			dumpData();
		}

		// Debug purposes
		lastOp = "addInterval(" + start + ", " + end + ")";
	}

	void mergeWith(Interval iv) {

		for (int i = 0; i < s.length; i++) {
			addInterval(s[i], e[i]);
		}
	}

}

public class milk2 {

	public static void main(String[] args) throws IOException {
		String[] s = FileIO.readFile("milk2.in");

		String[] temp = s[1].split(" ");
		double start = Double.parseDouble(temp[0]);
		double end = Double.parseDouble(temp[1]);

		Interval times = new Interval(start, end);

		for (int i = 2; i < s.length; i++) {
			temp = s[i].split(" ");
			start = Double.parseDouble(temp[0]);
			end = Double.parseDouble(temp[1]);
			times.addInterval(start, end);

		}

		double[] starts = times.s;
		double[] ends = times.e;

		int maxContTime = 0;
		int maxBreakTime = 0;

		for (int i = 0; i < starts.length; i++) {
			maxContTime = (int) Math.max(maxContTime, ends[i] - starts[i]);
		}

		for (int i = 0; i < starts.length - 1; i++) {
			maxBreakTime = (int) Math
					.max(maxBreakTime, starts[i + 1] - ends[i]);
		}

		String[] sOut = { Integer.toString(maxContTime) + " "
				+ Integer.toString(maxBreakTime) };

		FileIO.writeFile("milk2.out", sOut);
		System.exit(0);
	}
}
