

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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

@SuppressWarnings("rawtypes")
class Farmer implements Comparable {

	int price;
	int amount; // Amount remaining
	// Uses a separate ID for each farmer to prevent overlaps from merging
	int id;

	Farmer(int id) {

	}

	Farmer(int price, int amount, int id) {
		this.price = price;
		this.amount = amount;
		this.id = id;
	}

	public int compareTo(Object arg0) {
		int p1 = price;
		int i1 = id;
		int p2 = ((Farmer) arg0).price;
		int i2 = ((Farmer) arg0).id;
		int out = 0;

		if (p1 < p2) {
			out = -1;
		} else if (p1 > p2) {
			out = 1;
		} else if (i1 < i2) {
			out = -1;
		} else if (i1 > i2) {
			out = 1;
		}

		return out;
	}

	void setData(int price, int amount) {
		this.price = price;
		this.amount = amount;
	}

	int purchase(int a) {
		int out = 0;

		if (a <= amount) {
			amount = amount - a;
			out = a * price;
		}

		return out;
	}

	int purchaseAll() {
		int out = amount * price;
		amount = 0;
		return out;
	}

}

class Company {
	int cost; // cost
	int req; // Required amount of milk

	Company() {

	}

	Company(int required) {
		cost = 0;
		req = required;
	}

	void setReq(int required) {
		req = required;
	}

	void purchaseFrom(Farmer f, int amount) {
		cost = cost + f.purchase(amount);
		req = req - amount;
	}

	void purchaseAll(Farmer f) {
		req = req - f.amount;
		cost = cost + f.purchaseAll();
	}

	boolean needPurchase() {
		boolean out = false;
		if (req > 0) {
			out = true;
		}
		return out;
	}

}

public class milk {

	public static void main(String[] args) throws IOException {
		String[] in = FileIO.readFile("milk.in");
		// Debugging
		// String[] in = FileIO.readFile("C:\\Users\\Jack\\milk.txt");

		SortedSet<Farmer> fSet = new TreeSet<Farmer>();

		String[] company = in[0].split(" ");
		int req = Integer.parseInt(company[0]);

		Company c = new Company(req);

		// Sorts the farmers in order of increasing price
		String[] temp = new String[2];
		int p; // Price
		int a; // Amount
		for (int i = 1; i < in.length; i++) {
			temp = in[i].split(" ", 2);
			p = Integer.parseInt(temp[0]);
			a = Integer.parseInt(temp[1]);
			fSet.add(new Farmer(p, a, i));
		}

		Farmer[] fArray = new Farmer[fSet.size()];
		int count = 0;
		for (Farmer f : fSet) {
			fArray[count] = f;
			count++;
		}

		int curF = 0; // Index of the current farmer in discussion

		loop: while (c.needPurchase()) {
			System.out.println(c.req + " " + c.cost);
			System.out.println(fArray[curF].amount + " " + fArray[curF].price);

			if (c.req > fArray[curF].amount) {
				c.purchaseAll(fArray[curF]);
			} else if (c.req <= fArray[curF].amount) {
				c.purchaseFrom(fArray[curF], c.req);
				break loop;
			}

			curF++;
		}

		String[] out = { Integer.toString(c.cost) };
		System.out.println(c.req + " " + c.cost);

		FileIO.writeFile("milk.out", out);
		// Debugging
		// FileIO.writeFile("C:\\Users\\Jack\\milkOut.txt", out);
	}
}
