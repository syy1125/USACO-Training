import java.io.*;
import java.util.*;


public class palsquare
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
	
	static boolean isPal(String in)
	{
		boolean p = true;
		
		loop:
		for (int i = 0; i < in.length() / 2; i++)
		{
			if (in.charAt(i) != in.charAt(in.length() - 1 - i))
			{
				p = false;
				break loop;
			}
		}
		
		return p;
	}
	
	static String changeBase(int base, int num)
	{
		String out = Integer.toString(num, base);
		return out;
	}
	
	public static void main(String[] args)
			throws IOException
	{
		String[] in = FileIO.readFile("palsquare.in");
		int base = Integer.parseInt(in[0]);
		
		String sq = "";
		List<String> outList = new ArrayList<String>();
		for (int i = 1; i <= 300; i++)
		{
			sq = changeBase(base, i * i);
			sq = sq.toUpperCase();
			
			if (isPal(sq))
			{
				outList.add(changeBase(base, i).toUpperCase() + " " + sq);
			}
		}
		
		String[] out = outList.toArray(new String[outList.size()]);
		FileIO.writeFile("palsquare.out", out);
		
	}
	
}
