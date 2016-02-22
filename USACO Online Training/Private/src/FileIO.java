import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileIO
{
	static String[] readFile(String path) throws IOException
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
	
	static void writeFile(String path, String[] content) throws IOException
	{
		
		BufferedWriter out = new BufferedWriter(new FileWriter(path));
		
		for (int i = 0; i < content.length; i ++)
		{
			out.write(content[i]);
			out.newLine();
		}
		
		out.close();
	}
}