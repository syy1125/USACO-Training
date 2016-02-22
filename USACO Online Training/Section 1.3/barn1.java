




import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import contest.FileIO;



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

class Gap implements Comparable {
	int l; // Length
	int i; // Index

	Gap() {

	}

	Gap(int length, int index) {
		l = length;
		i = index;
	}

	public int compareTo(Object g) {
		int out = 0;
		Gap in = (Gap) g;

		if (l < in.l) {
			out = -1;
		} else if (l > in.l) {
			out = 1;
		} else if (i < in.i) {
			out = -1;
		} else if (i > in.i) {
			out = 1;
		}

		return out;
	}

	// Debugging use
	void printData() {
		System.out.println("Gap of length " + l + " at index " + i + ".");
	}
}

class Barn {
	int l; // The number of stalls
	boolean[] occ; // Whether each stall is occupied
	boolean[] cover; // Wheter each section is covered

	Barn() {

	}

	Barn(int length) {
		l = length;
	}

	Barn(boolean[] occupied) {
		l = occupied.length;
		occ = occupied;
		cover = occupied;
	}

	Barn(int length, int[] occStall) { // occStall for occupied stall locations
		l = length;
		occ = new boolean[l];

		Set<Integer> s = new HashSet<Integer>();
		for (int i = 0; i < occStall.length; i++) {
			s.add(occStall[i]);
		}

		for (int i = 0; i < l; i++) {
			if (s.contains(i + 1)) {
				occ[i] = true;
			} else {
				occ[i] = false;
			}
		}

		cover = occ;
	}

	int getBoardCount() {
		int count = 0;
		boolean board = false; // True if the current index is in the middle of
								// a board.

		for (int i = 0; i < l; i++) {
			if (!board && cover[i]) {
				count++;
				board = true;
			} else if (board && !cover[i]) {
				board = false;
			}
		}

		return count;

	}

	SortedSet<Gap> getGaps() {
		SortedSet<Gap> out = new TreeSet<Gap>();

		boolean gap = true;
		int gLength = 0; // Current gap length
		int index = 0;

		boolean firstGap = true; // Skip the gap before the first occupied stall

		for (int i = 0; i < l; i++) {
			if (!gap && !cover[i]) {
				gap = true;
				firstGap = false;
				gLength++;
				index = i;
			} else if (gap && cover[i]) {
				out.add(new Gap(gLength, index));
				gLength = 0;
				gap = false;
			} else if (gap && !firstGap) {
				gLength++;
			}
		}

		out.remove(new Gap(0, 0));

		return out;

	}

	int getCoverCount() {
		int out = 0;
		for (int i = 0; i < l; i++) {
			if (cover[i]) {
				out++;
			}
		}
		return out;
	}

	void coverGap(Gap g) {
		for (int i = g.i; i < g.i + g.l; i++) {
			cover[i] = true;
		}
	}

}

public class barn1 {

	public static void main(String[] args) throws IOException {
		String[] in = FileIO.readFile("barn1.in");
		// String[] in = { "4 50 18", "3", "4", "6", "8", "14", "15", "16",
		// "17",
		// "21", "25", "26", "27", "30", "31", "40", "41", "42", "43" };

		String[] in1 = in[0].split(" ");

		int bLim = Integer.parseInt(in1[0]);
		int barnLength = Integer.parseInt(in1[1]);

		int[] occ = new int[in.length - 1];

		for (int i = 0; i < in.length - 1; i++) {
			occ[i] = Integer.parseInt(in[i + 1]);
		}

		Barn b = new Barn(barnLength, occ);

		if (bLim < b.getBoardCount()) {
			// System.out.println(b.getBoardCount());
			SortedSet<Gap> gaps = b.getGaps();
			int count = b.getBoardCount() - bLim;

			loop: for (Gap g : gaps) {
				// g.printData();
				b.coverGap(g);
				count--;
				// System.out.println(count);
				if (count <= 0)
					break loop;
			}
		}

		String[] out = { Integer.toString(b.getCoverCount()) };

		// System.out.println(b.getCoverCount());
		FileIO.writeFile("barn1.out", out);
		System.exit(0);

	}

}
