


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ctiming
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
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args)
			throws IOException
	{
		String[] input = FileIO.readFile("ctiming.in");
		
		String[] in = input[0].split(" ");
		
		int day = Integer.parseInt(in[0]);
		int hour = Integer.parseInt(in[1]);
		int min = Integer.parseInt(in[2]);
		
		int time = 1440 * (day - 11) + 60 * (hour - 11) + (min - 11);
		
		if (time < 0)
		{
			time = -1;
		}
		
		String[] out = {Integer.toString(time)};

		/*
		 * if (out[0].equals(correctOut[0])) { System.out.println("Test " + i +
		 * " successful."); } else { System.out.println("Test " + i +
		 * " unsuccessful."); System.out.println("Program output: " + out[0]);
		 * System.out.println("Correct output: " + correctOut[0]); }
		 */
		
		FileIO.writeFile("ctiming.out", out);
		System.exit(0);
	}
}
