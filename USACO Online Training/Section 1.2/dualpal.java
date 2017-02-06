


import java.io.*;
import java.util.*;


public class dualpal
{
	static class FileIO
	{
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
	}
	
	static boolean isPal(String s)
	{
		boolean out = true;
		
		loop:
		for (int i = 0; i < s.length() / 2; i++)
		{
			if (s.charAt(i) != s.charAt(s.length() - 1 - i))
			{
				out = false;
				break loop;
			}
		}
		
		return out;
	}
	
	static boolean isPal(int i)
	{
		String s = Integer.toString(i);
		return isPal(s);
	}
	
	static boolean palInBase(int num, int base)
	{
		String in = Integer.toString(num, base);
		return isPal(in);
	}
	
	static boolean palMultiBase(int num)
	{
		boolean out = false;
		int count = 0;
		
		loop:
		for (int i = 2; i < 11; i++)
		{
			if (palInBase(num, i))
			{
				count++;
			}
			if (count >= 2)
			{
				out = true;
				break loop;
			}
		}
		
		return out;
	}
	
	public static void main(String[] args)
			throws IOException
	{
		String[] input = FileIO.readFile("dualpal.in");
		
		String[] in = input[0].split(" ");
		int n = Integer.parseInt(in[0]);
		int s = Integer.parseInt(in[1]);
		
		int i = s + 1;
		int[] outInt = new int[n];
		int count = 0;
		
		while (count < n)
		{
			if (palMultiBase(i))
			{
				outInt[count] = i;
				count++;
			}
			
			i++;
		}
		
		String[] out = new String[n];
		for (int j = 0; j < n; j++)
		{
			out[j] = Integer.toString(outInt[j]);
		}
		
		FileIO.writeFile("dualpal.out", out);
		System.exit(0);
		
	}
}
