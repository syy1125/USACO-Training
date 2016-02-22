
import java.io.*;
import java.util.*;

class FileIO {
	static String testPath = "C:\\Users\\Jack\\Eclipse\\USACO\\";

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

	static String[] readTest(String path) throws IOException {
		return readFile(testPath + path);
	}

	static void writeFile(String path, String[] content) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(path));

		for (int i = 0; i < content.length; i++) {
			out.write(content[i]);
			out.newLine();
		}

		out.close();
	}

	static void writeTest(String path, String[] content) throws IOException {
		writeFile(testPath + path, content);
	}
}

public class moosick {

	static boolean match(SortedSet<Integer> s1, SortedSet<Integer> s2) {
		boolean out = true;

		if (s1.size() == s2.size()) {
			int[] a1 = new int[s1.size()];
			int count = 0;

			for (int i : s1) {
				a1[count] = i;
				count++;
			}

			int[] a2 = new int[s2.size()];
			count = 0;

			for (int i : s2) {
				a2[count] = i;
				count++;
			}

			int base = a1[0] - a2[0];

			loop: for (int i = 0; i < a1.length; i++) {
				if (a1[i] - a2[i] != base) {
					out = false;
				}
			}

		} else {
			out = false;
		}

		return out;
	}

	public static void main(String[] args) throws IOException {
		String[] in = FileIO.readFile("moosick.in");

		/*
		 * for (int i = 1; i <= 10; i++) { String[] in =
		 * FileIO.readTest("moosick\\I." + Integer.toString(i)); String[]
		 * correctOut = FileIO.readTest("moosick\\O." + Integer.toString(i));
		 */

		int songLength = Integer.parseInt(in[0]);
		int[] song = new int[songLength];
		for (int j = 1; j <= songLength; j++) {
			song[j - 1] = Integer.parseInt(in[j]);
		}

		int chordLength = Integer.parseInt(in[songLength + 1]);

		SortedSet<Integer> chord = new TreeSet<Integer>();
		for (int j = songLength + 2; j < in.length; j++) {
			chord.add(Integer.parseInt(in[j]));
		}

		SortedSet<Integer> buffer = new TreeSet<Integer>();

		SortedSet<Integer> index = new TreeSet<Integer>();

		for (int j = 0; j < songLength - chordLength + 1; j++) {
			buffer.clear();
			for (int k = 0; k < chordLength; k++) {
				buffer.add(song[j + k]);
			}

			if (match(buffer, chord)) {
				index.add(j + 1);
			}
		}

		String[] out = new String[index.size() + 1];

		out[0] = Integer.toString(index.size());
		int count = 1;
		for (Integer j : index) {
			out[count] = Integer.toString(j);
			count++;
		}

		/*
		 * if (out[0].equals(correctOut[0])) { System.out.println("Test " + i +
		 * " successful."); } else { System.out.println("Test " + i +
		 * " unsuccessful."); System.out.println("Program output: " + out[0]);
		 * System.out.println("Correct output: " + correctOut[0]); }
		 */

		FileIO.writeFile("moosick.out", out);

	}
}
