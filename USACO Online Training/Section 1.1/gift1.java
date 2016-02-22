

import java.io.*;
import java.util.*;

class member {
	String name;
	int money;
	int nFriends;
	int giveEach;
	int delta;

	member(String name) {
		this.name = name;
		delta = 0;
	}

	void setProperty(int account, int numFriends) {
		money = account;
		nFriends = numFriends;
		giveEach = account / numFriends;
	}

	void give() {
		delta = delta - giveEach * nFriends;
	}

	void receive(int amount) {
		delta = delta + amount;
	}

	String getName() {
		return name;
	}

	static int find(member[] m, String nameRequest) {
		int iOut = 0;
		for (int i = 0; i < m.length; i++) {
			if (m[i].getName().equalsIgnoreCase(nameRequest)) {
				iOut = i;
			}
		}

		return iOut;
	}
}

public class gift1 {

	static String[] readFile() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("gift1.in"));
		String line;
		List<String> l = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			l.add(line);
		}

		String[] sOut = l.toArray(new String[l.size()]);
		return sOut;
	}

	static int[] readNumber(String sIn) {
		StringTokenizer st = new StringTokenizer(sIn);
		int[] iOut = new int[2];
		try {
			iOut[0] = Integer.parseInt(st.nextToken());
			iOut[1] = Integer.parseInt(st.nextToken());
		} catch (NumberFormatException e) {
			iOut = null;
		}
		return iOut;
	}

	static member[] process(String[] s) {
		int NR = Integer.parseInt(s[0]);
		member[] m = new member[NR];

		for (int i = 1; i <= NR; i++) {
			m[i - 1] = new member(s[i]);
		}

		for (int i = NR + 1; i < s.length; i++) {
			int index = 0; // The index of the person in m
			int[] temp = new int[2];
			int account = 0;
			int numFriends = 0;

			if (readNumber(s[i]) != null) { // Found a line of readable number

				temp = readNumber(s[i]);
				account = temp[0];
				numFriends = temp[1];
				index = member.find(m, s[i - 1]);

				int receiverI; // Receiver's index
				if (numFriends > 0) { // If this person has friends
					m[index].setProperty(account, numFriends);

					m[index].give();

					// For each line indicating the friend
					for (int j = 1; j <= numFriends; j++) {

						String receiverName = s[i + j];
						int receiver = member.find(m, receiverName);

						m[receiver].receive(m[index].giveEach);
					}
				}

				index++;
			}
		}

		return m;
	}

	static String[] assemble(member[] m) {
		String[] sOut = new String[m.length];
		for (int i = 0; i < m.length; i++) {
			sOut[i] = m[i].name + " " + m[i].delta;
		}
		return sOut;
	}

	static void writeFile(String[] s) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter("gift1.out"));

		for (int i = 0; i < s.length; i++) {
			out.write(s[i]);
			out.newLine();
		}

		out.close();
	}

	public static void main(String[] args) throws IOException {
		String[] s1 = readFile();
		member[] m = process(s1);
		String[] s2 = assemble(m);
		writeFile(s2);
	}

}
