import java.io.*;
import java.util.*;


public class digits
{
	static class FileIO
	{
		static String testPath = "C:\\Users\\Jack\\Eclipse\\USACO\\";
		
		static String[] readFile(String path)
				throws IOException
		{
			BufferedReader in = new BufferedReader(new FileReader(path));
			List<String> s = new ArrayList<String>();
			
			String buffer;
			while ((buffer = in.readLine()) != null)
			{
				s.add(buffer);
			}
			
			in.close();
			
			String[] out = s.toArray(new String[s.size()]);
			return out;
		}
		
		static String[] readTest(String path)
				throws IOException
		{
			return readFile(testPath + path);
		}
		
		static void writeFile(String path, String[] content)
				throws IOException
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			
			for (int i = 0; i < content.length; i++)
			{
				out.write(content[i]);
				out.newLine();
			}
			
			out.close();
		}
		
		static void writeTest(String path, String[] content)
				throws IOException
		{
			writeFile(testPath + path, content);
		}
	}
	
	static Set<String> listPsb(String s, int base)
	{
		
		Set<String> out = new HashSet<String>();
		
		int curDigit;
		String psb;
		
		for (int i = 0; i < s.length(); i++)
		{
			curDigit = Integer.parseInt(String.valueOf(s.charAt(i)));
			
			for (int j = 0; j < base; j++)
			{
				if (j != curDigit)
				{
					psb = s.substring(0, i) + Integer.toString(j).charAt(0)
							+ s.substring(i + 1, s.length());
					out.add(psb);
				}
			}
		}
		
		return out;
	}
	
	public static void main(String[] args)
			throws IOException
	{
		
		String[] in = FileIO.readFile("digits.in");
		
		// System.out.println("Running test " + i + "...");
		
		// String[] in = FileIO.readTest("digits\\I." + Integer.toString(i));
		// String[] correctOut = FileIO.readTest("digits\\O."
		// + Integer.toString(i));
		
		Set<String> psb2 = listPsb(in[0], 2);
		Set<String> psb3 = listPsb(in[1], 3);
		
		int buffer;
		int solution = 0;
		
		loop:
		for (String s : psb2)
		{
			
			buffer = Integer.parseInt(s, 2);
			
			if (psb3.contains(Integer.toString(buffer, 3)))
			{
				solution = buffer;
				break loop;
			}
		}
		
		String[] out = {Integer.toString(solution)};

		/*
		 * if (out[0].equals(correctOut[0])) { System.out.println("Test " + i +
		 * " successful."); } else { System.out.println("Test " + i +
		 * " unsuccessful."); System.out.println("Program output: " + out[0]);
		 * System.out.println("Correct output: " + correctOut[0]); }
		 */
		
		FileIO.writeFile("digits.out", out);
		System.exit(0);
		
	}
}
