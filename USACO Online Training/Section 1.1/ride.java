

import java.io.*;
import java.util.*;

public class ride {

	static int toCode(char cIn) {
		int iOut = cIn - 64;
		return iOut;
	}

	static int process(String sIn) {
		int prod = 1;

		for (int i = 0; i < sIn.length(); i++) {
			prod = prod * toCode(sIn.charAt(i));
		}

		int iOut = prod % 47;

		return iOut;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("ride.in"));
		BufferedWriter out = new BufferedWriter(new FileWriter("ride.out"));

		// Input
		String in1 = in.readLine();
		String in2 = in.readLine();

		// Processing
		int i1 = process(in1);
		int i2 = process(in2);

		// Testing
		// int i1 = process("ABSTAR");
		// int i2 = process("USACO");

		if (i1 == i2) {
			out.write("GO");
			out.newLine();
			// Testing
			// System.out.println("GO");
		} else {
			out.write("STAY");
			out.newLine();
			// Testing
			// System.out.println("STAY");
		}

		out.close();
		System.exit(0);
	}

}
