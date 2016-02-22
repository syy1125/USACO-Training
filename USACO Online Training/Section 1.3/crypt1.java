

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

class Crypt {
	SortedSet<Integer> allowed = new TreeSet<Integer>();
	int[] key;

	Crypt() {
		allowed.clear();
	}

	Crypt(int[] n) {
		allowed.clear();
		for (int i = 0; i < n.length; i++) {
			allowed.add(n[i]);
		}

		key = new int[allowed.size()];
		int count = 0;

		for (int i : allowed) {
			key[count] = i;
			count++;
		}
	}

	boolean isValidNum(int n) {
		boolean out = true;

		char[] c = Integer.toString(n).toCharArray();

		int curNum;
		loop: for (int i = 0; i < c.length; i++) {
			curNum = c[i] - 48;

			if (!allowed.contains(curNum)) {
				out = false;
				break loop;
			}
		}

		return out;

	}

	boolean isValidProd(int n1, int n2) {
		boolean out = false;

		if (100 <= n1 && n1 < 1000 && 10 <= n2 && n2 < 100) {

			char dig1 = Integer.toString(n2).charAt(0);
			char dig2 = Integer.toString(n2).charAt(1);

			int prod1 = n1 * Integer.parseInt(Character.toString(dig2));
			int prod2 = n1 * Integer.parseInt(Character.toString(dig1));

			int prod = n1 * n2;

			if (100 <= prod1 && prod1 < 1000 && 100 <= prod2 && prod2 < 1000
					&& prod < 10000) {
				if (isValidNum(prod1) && isValidNum(prod2) && isValidNum(prod)) {
					out = true;
				}
			}
		}

		return out;
	}

	int[] all3Dig() {
		List<Integer> list = new ArrayList<Integer>();
		int s = allowed.size();

		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				for (int k = 0; k < s; k++) {
					list.add(100 * key[i] + 10 * key[j] + key[k]);
				}
			}
		}

		int[] out = new int[list.size()];
		Integer[] buffer = list.toArray(new Integer[list.size()]);

		for (int i = 0; i < out.length; i++) {
			out[i] = buffer[i];
		}

		return out;
	}

	int[] all2Dig() {
		List<Integer> list = new ArrayList<Integer>();
		int s = allowed.size();

		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				list.add(10 * key[i] + key[j]);
			}
		}

		int[] out = new int[list.size()];
		Integer[] buffer = list.toArray(new Integer[list.size()]);

		for (int i = 0; i < out.length; i++) {
			out[i] = buffer[i];
		}

		return out;
	}
}

public class crypt1 {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String[] input = FileIO.readFile("crypt1.in");
		String[] in = input[1].split(" ");
		int[] n = new int[in.length];

		for (int i = 0; i < in.length; i++) {
			n[i] = Integer.parseInt(in[i]);
		}

		Crypt c = new Crypt(n);

		int[] num1 = c.all3Dig();
		int[] num2 = c.all2Dig();

		int count = 0;

		for (int i = 0; i < num1.length; i++) {
			for (int j = 0; j < num2.length; j++) {
				if (c.isValidProd(num1[i], num2[j])) {
					count++;
				}
			}
		}

		String[] out = { Integer.toString(count) };

		FileIO.writeFile("crypt1.out", out);
	}

}
