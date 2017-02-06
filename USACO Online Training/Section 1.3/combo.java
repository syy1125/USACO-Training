/*
ID: syy11251
LANG: JAVA
TASK: combo
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class TestHelper
{
	static String testName = "combo";
	static final String testParent = "C:\\Users\\Jack\\Education\\Programming\\USACO\\tests\\";
	static List<String> log;
	
	public static void init()
	{
		log = new ArrayList<String>();
	}
	
	public static String[] readTest()
			throws IOException
	{
		return FileIO.readFile(testParent + testName + "\\" + testName + ".in");
	}
	
	public static void log(String msg)
	{
		log.add(msg);
	}
	
	public static void writeTest(String[] contents)
			throws IOException
	{
		FileIO.writeFile(testParent + testName + "\\" + testName + ".out", contents);
	}
	
	public static void writeLog()
			throws IOException
	{
		FileIO.writeFile(testParent + testName + "\\" + testName + ".log", log.toArray(new String[log.size()]));
	}
}


class MathUtils
{
	public static boolean closeEnough(int num1, int num2, int limit, int tolerance)
	{
		int diff = num1 < num2 ? num2 - num1 : num1 - num2;
		return (diff <= tolerance || limit - diff <= tolerance);
	}
}

class LockCombo
{
	static int limit;
	private static final int TOLERANCE = 2;
	
	private int[] keys;
	
	LockCombo(int[] k)
	{
		keys = k;
	}
	
	boolean closeTo(LockCombo other)
	{
		int length = keys.length < other.keys.length ? keys.length : other.keys.length;
		
		for (int i = 0; i < length; i++)
		{
			if (!MathUtils.closeEnough(keys[i], other.keys[i], limit, TOLERANCE)) return false;
		}
		
		// If the key passed all
		return true;
	}
	
	@Override
	public String toString()
	{
		String str = "LockCombo(";
		
		for (int i = 0; i < keys.length - 1; i++)
		{
			str += keys[i] + ", ";
		}
		
		str += keys[keys.length - 1] + ")";
		return str;
	}
	
}

class ComboSearcher
		implements Callable<Integer>
{
	private int limit;
	private int assigned;
	private LockCombo[] unlockCombos;
	
	public ComboSearcher(int limit, int assigned, LockCombo[] unlockCombos)
	{
		this.limit = limit;
		this.assigned = assigned;
		this.unlockCombos = unlockCombos;
	}
	
	@Override
	public Integer call()
			throws Exception
	{
		int count = 0;
		
		for (int i = 0; i < limit; i++)
		{
			searchLoop:
			for (int j = 0; j < limit; j++)
			{
				LockCombo currentCombo = new LockCombo(new int[]{assigned, i, j});
				for (LockCombo combo : unlockCombos)
				{
					if (currentCombo.closeTo(combo))
					{
						// Found a unlockable position, skip to the next combination.
						// TestHelper.log("Found " + currentCombo + " close to " + combo);
						count++;
						continue searchLoop;
					}
				}
			}
		}
		
		return Integer.valueOf(count);
	}
	
}

public class combo
{
	static class FileIO
	{
		static String[] readFile(String path)
				throws IOException
		{
			
			BufferedReader in = new BufferedReader(new FileReader(path));
			List<String> s = new ArrayList<>();
			
			String buffer;
			while ((buffer = in.readLine()) != null)
			{
				s.add(buffer);
			}
			
			in.close();
			
			return s.toArray(new String[s.size()]);
		}
		
		static void writeFile(String path, String[] content)
				throws IOException
		{
			
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			
			for (String aContent : content)
			{
				out.write(aContent);
				out.newLine();
			}
			
			out.close();
		}
	}
	
	public static void main(String[] args)
			throws IOException, InterruptedException, ExecutionException
	{
		// TestHelper.init();
		// String[] in = TestHelper.readTest();
		String[] in = FileIO.readFile("combo.in");
		
		// Limit
		int lim = Integer.parseInt(in[0]);
		LockCombo.limit = lim;
		
		// First combo
		String[] data1 = in[1].split(" ");
		LockCombo johnCombo = new LockCombo(new int[]{Integer.parseInt(data1[0]) - 1, Integer.parseInt(data1[1]) - 1,
				Integer.parseInt(data1[2]) - 1});
		// TestHelper.log("Created unlock combo " + johnCombo);
		
		// Second combo
		String[] data2 = in[2].split(" ");
		LockCombo masterCombo = new LockCombo(new int[]{Integer.parseInt(data2[0]) - 1, Integer.parseInt(data2[1]) - 1,
				Integer.parseInt(data2[2]) - 1});
		// TestHelper.log("Created unlock combo " + masterCombo);
		
		LockCombo[] unlocks = {johnCombo, masterCombo};
		
		FutureTask<Integer>[] tasks = new FutureTask[lim];
		for (int i = 0; i < lim; i++)
		{
			tasks[i] = new FutureTask<Integer>(new ComboSearcher(lim, i, unlocks));
			tasks[i].run();
		}
		
		int count = 0;
		for (int i = 0; i < lim; i++)
		{
			count += tasks[i].get();
			// TestHelper.log("Count increased to " + count);
		}
		
		// TestHelper.writeTest(new String[] { Integer.toString(count) });
		// TestHelper.writeLog();
		FileIO.writeFile("combo.out", new String[]{Integer.toString(count)});
	}
}
