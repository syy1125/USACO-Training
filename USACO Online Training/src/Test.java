/*
ID: syy11251
LANG: JAVA
TASK: test
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

class Test {

	public static void main(String[] Args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("test.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				"test.out)")));

		StringTokenizer st = new StringTokenizer(in.readLine());

		int i1 = Integer.parseInt(st.nextToken());
		int i2 = Integer.parseInt(st.nextToken());

		int i = i1 + i2;

		out.println(i);
		out.close();
		System.exit(0);
	}
}

// Am I doing this right?
