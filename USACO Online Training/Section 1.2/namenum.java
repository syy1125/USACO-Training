


import java.io.*;
import java.util.*;


public class namenum
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
	
	static char[] toLetters(int in)
	{
		char pos1;
		char pos2;
		char pos3;
		if (in < 7)
		{
			pos1 = (char) (3 * in + 59);
			pos2 = (char) (3 * in + 60);
			pos3 = (char) (3 * in + 61);
		}
		else if (in == 7)
		{
			pos1 = (char) (3 * in + 59);
			pos2 = (char) (3 * in + 61);
			pos3 = (char) (3 * in + 62);
		}
		else
		{
			pos1 = (char) (3 * in + 60);
			pos2 = (char) (3 * in + 61);
			pos3 = (char) (3 * in + 62);
		}
		char[] out = {pos1, pos2, pos3};
		return out;
	}
	
	static boolean isName(String name, Set<String> valid)
	{
		boolean isName = valid.contains(name);
		
		return isName;
	}
	
	static String[] findAll(String s)
	{
		
		int[] digits = new int[s.length()];
		for (int i = 0; i < s.length(); i++)
		{
			digits[i] = s.charAt(i) - 48;
		}
		
		List<String> outList = new ArrayList<String>();
		
		// One counter for each loaction
		int[] counter = new int[s.length()];
		for (int i = 0; i < counter.length; i++)
		{
			counter[i] = 0;
		}
		
		String name;
		// key[position][0-2 of possible letters]
		char[][] key = new char[digits.length][3];
		
		// Build the key array
		for (int i = 0; i < digits.length; i++)
		{
			key[i] = toLetters(digits[i]);
		}
		
		// For each possible output
		for (int i = 0; i < Math.pow(3, digits.length); i++)
		{
			name = "";
			
			// For each letter
			for (int j = 0; j < s.length(); j++)
			{
				name = name + key[j][counter[j]];
			}
			
			outList.add(name);
			
			// Base 3 operation
			counter[s.length() - 1]++;
			for (int j = s.length() - 1; j >= 0; j--)
			{
				if (counter[j] >= 3)
				{
					counter[j] = 0;
					if (j > 0)
					{
						counter[j - 1]++;
					}
				}
			}
		}
		
		if (outList.size() == 0)
		{
			outList.add("NONE");
		}
		
		String[] out = outList.toArray(new String[outList.size()]);
		return out;
		
	}
	
	static String[] findAll(String code, Set<String> names)
	{
		
		int[] digits = new int[code.length()];
		for (int i = 0; i < code.length(); i++)
		{
			digits[i] = code.charAt(i) - 48;
		}
		
		List<String> outList = new ArrayList<String>();
		
		// One counter for each loaction
		int[] counter = new int[code.length()];
		for (int i = 0; i < counter.length; i++)
		{
			counter[i] = 0;
		}
		
		String name;
		char[] temp;
		// For each possible output
		for (int i = 0; i < Math.pow(3, digits.length); i++)
		{
			name = "";
			
			// For each letter
			for (int j = 0; j < code.length(); j++)
			{
				temp = toLetters(digits[j]);
				name = name + temp[counter[j]];
			}
			
			if (isName(name, names))
			{
				outList.add(name);
			}
			
			// Base 3 operation
			counter[code.length() - 1]++;
			for (int j = code.length() - 1; j >= 0; j--)
			{
				if (counter[j] >= 3)
				{
					counter[j] = 0;
					if (j > 0)
					{
						counter[j - 1]++;
					}
				}
			}
		}
		
		if (outList.size() == 0)
		{
			outList.add("NONE");
		}
		
		String[] out = outList.toArray(new String[outList.size()]);
		return out;
		
	}
	
	static String toCode(String name)
	{
		// Takes name and convertis it into a code integer
		int temp;
		String out = "";
		for (int i = 0; i < name.length(); i++)
		{
			temp = name.charAt(i);
			if (temp > 81)
			{
				temp--;
			}
			temp = (temp - 59) / 3;
			out = out + Integer.toString(temp);
		}
		
		return out;
	}
	
	static String[] matchAll(String code, String[] names)
	{
		List<String> outList = new ArrayList<String>();
		
		for (int i = 0; i < names.length; i++)
		{
			if (toCode(names[i]).equalsIgnoreCase(code))
			{
				outList.add(names[i]);
			}
		}
		
		if (outList.size() == 0)
		{
			outList.add("NONE");
		}
		
		String[] out = outList.toArray(new String[outList.size()]);
		return out;
	}
	
	public static void main(String[] args)
			throws IOException
	{
		String[] in = FileIO.readFile("namenum.in");
		String code = in[0];
		
		String[] n = FileIO.readFile("dict.txt");
		
		String[] out = matchAll(code, n);
		
		FileIO.writeFile("namenum.out", out);
		System.exit(0);
	}
}
