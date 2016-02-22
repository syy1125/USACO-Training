

import java.io.*;
import java.util.*;

public class beads {

	static String readFile() throws FileNotFoundException, IOException {
		BufferedReader in = new BufferedReader(new FileReader("beads.in"));
		String useless = in.readLine();
		String sOut = in.readLine();
		return sOut;
	}

	static int cycSum(int i1, int i2, int limit) {
		int iOut = i1 + i2;

		while (iOut < 0) {
			iOut = iOut + limit;
		}
		while (iOut >= limit) {
			iOut = iOut - limit;
		}

		return iOut;
	}

	static int collect(String beads, int pos) {
		int l = beads.length();

		int curPosLeft = cycSum(pos, -1, l);
		int curPosRight = cycSum(pos, 0, l);

		char prev = beads.charAt(curPosLeft);
		char next = beads.charAt(curPosRight);

		char beadLeft = prev;
		char beadRight = next;

		int collected = 0;

		while ((beadLeft == prev || beadLeft == 'w' || prev == 'w')
				&& collected < l) {
			if (prev == 'w' && beadLeft != 'w') {
				prev = beadLeft;
			}
			// System.out.print(beadLeft);
			collected++;
			curPosLeft = cycSum(curPosLeft, -1, l);
			beadLeft = beads.charAt(curPosLeft);
		}
		// System.out.println();

		while ((beadRight == next || beadRight == 'w' || next == 'w')
				&& collected < l) {
			if (next == 'w' && beadRight != 'w') {
				next = beadRight;
			}
			// System.out.print(beadRight);
			collected++;
			curPosRight = cycSum(curPosRight, 1, l);
			beadRight = beads.charAt(curPosRight);
		}
		// System.out.println();

		return collected;
	}

	static int findMax(String beads) {
		int lMax = 0;
		for (int i = 0; i < beads.length(); i++) {
			lMax = Math.max(lMax, collect(beads, i));
		}
		return lMax;
	}

	static void writeFile(int i) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter("beads.out"));
		out.write(Integer.toString(i));
		out.newLine();
		out.close();
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		String beads = readFile();
		int i = findMax(beads);
		writeFile(i);

		System.exit(0);
	}
}
