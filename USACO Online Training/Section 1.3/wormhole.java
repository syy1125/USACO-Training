/*
ID: syy11251
LANG: JAVA
TASK: wormhole
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

class WormH
		implements Comparable<WormH>
{
	public final int x;
	public final int y;
	
	public WormH linked;
	
	public WormH(int x, int y)
	{
		this.x = x;
		this.y = y;
		linked = null;
	}
	
	public void link(WormH other)
	{
		this.linked = other;
		other.linked = this;
	}
	
	@Override
	public int hashCode()
	{
		return x * 23 + y * 17;
	}
	
	@Override
	public int compareTo(WormH o)
	{
		return this.x - o.x;
	}
	
	@Override
	public String toString()
	{
		return "Hole@[" + x + "," + y + "]";
	}
}

class HoleMap
{
	private final WormH[] holes;
	
	public HoleMap(WormH[] holes)
	{
		this.holes = holes;
	}
	
	public void configure(int[] config)
	{
		ArrayList<WormH> tempHoleList = new ArrayList<>();
		Collections.addAll(tempHoleList, holes);
		
		for (int i = config.length - 1; i >= 0; i--)
		{
			tempHoleList.remove(0).link(tempHoleList.remove(config[i]));
		}
	}
	
	private WormH raycast(WormH current)
	{
		WormH result = null;
		
		for (WormH hole : holes)
		{
			if (hole.y == current.y && hole.x > current.x)
			{
				if (result == null || result.x > hole.x)
				{
					result = hole;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * @return true if starting from this position results in an infinite loop.
	 */
	private boolean startFrom(WormH start)
	{
		HashSet<WormH> exits = new HashSet<>();
		exits.add(start);
		
		WormH nextEntrance;
		do
		{
			nextEntrance = raycast(start);
			if (nextEntrance == null)
			{
				return false;
			}
			start = nextEntrance.linked;
		}
		while (exits.add(start));
		
		return true;
	}
	
	public boolean hasLoop()
	{
		for (WormH hole : holes)
		{
			if (startFrom(hole))
			{
				return true;
			}
		}
		return false;
	}
}

interface Processor
{
	int process(int in);
}

public class wormhole
{
	/**
	 * Handles USACO style interaction with files. Also has integrated test methods.
	 *
	 * @author syy1125
	 */
	static class IOHandler
	{
		enum LogLevel
		{
			/** The level used to turn logs off */
			OFF(""),
			ERROR("ERROR"),
			WARN("WARN"),
			INFO("INFO"),
			DEBUG("DEBUG"),
			TRACE("TRACE");
			
			String msg;
			int lvl;
			
			private LogLevel(String logMsg)
			{
				msg = logMsg;
				lvl = ordinal();
			}
		}
		
		private static final String newLine = System.getProperty("line.separator");
		private static final String fileSeparator = System.getProperty("file.separator");
		
		private String prgmName;
		private String fileName;
		private String[] data;
		private LinkedList<String> dataList;
		
		/** The starting time of the program */
		private long startTime;
		/** The <code>DecimalFormat</code> used to format time stamps */
		private static final DecimalFormat timeFormat = new DecimalFormat("0.000");
		/** Whether the program is running under test conditions */
		private boolean test;
		
		/** The lowest log level that will be recorded */
		private LogLevel baseLevel;
		/** The <code>StringBuilder</code> used to build log outputs */
		private StringBuilder logBuilder;
		/** Whether the logger should accept log messages */
		private boolean doLog;
		
		public IOHandler(String programName)
		{
			prgmName = programName;
			logBuilder = new StringBuilder();
			logBuilder.setLength(0);
			startTime = System.currentTimeMillis();
		}
		
		/**
		 * Initializes the handler and loads the file. This is universal across all initialization methods.
		 *
		 * @throws IOException
		 */
		private void loadFile()
				throws IOException
		{
			if (test)
			{
				// Use the documents folder
				fileName = System.getProperty("user.home") + fileSeparator + "Documents" + fileSeparator + "USACO Test" + fileSeparator + prgmName
						+ fileSeparator + prgmName;
			}
			else
			{
				fileName = prgmName;
			}
			
			BufferedReader in = null;
			try
			{
				in = new BufferedReader(new FileReader(fileName + ".in"));
			}
			catch (FileNotFoundException e)
			{
				System.err.println("File is not found: " + fileName + ".in");
				throw e;
			}
			
			dataList = new LinkedList<String>();
			
			String buffer;
			while ((buffer = in.readLine()) != null)
			{
				dataList.add(buffer);
			}
			
			data = dataList.toArray(new String[dataList.size()]);
			
			in.close();
			logNewEntry(LogLevel.INFO);
			log("File reading complete!");
		}
		
		/**
		 * Initialize the <code>IOHandler</code> to run conditions.
		 *
		 * @throws IOException
		 */
		public void initRun()
				throws IOException
		{
			try
			{
				test = false;
				baseLevel = LogLevel.OFF;
				loadFile();
			}
			catch (IOException ex)
			{
				initTest();
			}
		}
		
		/**
		 * Initialize the <code>IOHandler</code> to test conditions.
		 *
		 * @throws IOException
		 */
		public void initTest()
				throws IOException
		{
			test = true;
			baseLevel = LogLevel.INFO;
			loadFile();
		}
		
		/**
		 * Initialize the <code>IOHandler</code> to debug conditions.
		 *
		 * @throws IOException
		 */
		public void initDebug()
				throws IOException
		{
			test = true;
			baseLevel = LogLevel.DEBUG;
			loadFile();
		}
		
		/**
		 * Initialize the <code>IOHandler</code> to trace conditions.
		 *
		 * @throws IOException
		 */
		public void initTrace()
				throws IOException
		{
			test = true;
			baseLevel = LogLevel.TRACE;
			loadFile();
		}
		
		/**
		 * @return The data stored in the handler which is read from the input file
		 */
		public String[] getData()
		{
			return data;
		}
		
		/**
		 * @return The <code>ListIterator</code> of the <code>List</code> storing the input data.
		 */
		public ListIterator<String> getIterator()
		{
			return dataList.listIterator();
		}
		
		/**
		 * @param startIndex The starting index of the <code>ListIterator</code>
		 * @return The <code>ListIterator</code> starting at the specified index
		 */
		public ListIterator<String> getIterator(int startIndex)
		{
			return dataList.listIterator(startIndex);
		}
		
		/**
		 * Outputs the specified content to the output file.
		 *
		 * @param content The content of the output
		 * @throws IOException
		 */
		public void output(String[] content)
				throws IOException
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName + ".out"));
			
			for (String line : content)
			{
				out.write(line);
				out.newLine();
			}
			
			logNewEntry(LogLevel.INFO);
			log("Main output writing complete.");
			
			if (test)
			{
				// Write the output log
				out.newLine();
				out.write("Log:");
				out.write(logBuilder.toString());
				out.newLine();
			}
			
			System.out.println("Output complete.");
			out.close();
			System.exit(0);
		}
		
		/**
		 * Adds the header for a log entry.
		 *
		 * @param level The priority level of the log message
		 * @return Whether the log should be added
		 */
		public void logNewEntry(LogLevel level)
		{
			// Only log the information above the base level
			if (level.lvl > baseLevel.lvl)
			{
				doLog = false;
				return;
			}
			
			// Add a new line for this message
			logBuilder.append(newLine);
			
			// Time
			logBuilder.append('[');
			logBuilder.append(timeFormat.format((System.currentTimeMillis() - startTime) / 1000F));
			logBuilder.append(']');
			
			// Level
			logBuilder.append('[');
			logBuilder.append(level.msg);
			logBuilder.append(']');
			logBuilder.append(' ');
			
			// Continue
			doLog = true;
		}
		
		public void log(boolean b)
		{
			// Only log if the level is correct.
			if (!doLog) return;
			// Log.
			logBuilder.append(b);
		}
		
		public void log(char c)
		{
			// Only log if the level is correct.
			if (!doLog) return;
			// Log.
			logBuilder.append(c);
		}
		
		public void log(char[] c)
		{
			// Only log if the level is correct.
			if (!doLog) return;
			// Log.
			logBuilder.append(c);
		}
		
		public void log(double d)
		{
			// Only log if the level is correct.
			if (!doLog) return;
			// Log.
			logBuilder.append(d);
		}
		
		public void log(float f)
		{
			// Only log if the level is correct.
			if (!doLog) return;
			// Log.
			logBuilder.append(f);
		}
		
		public void log(int i)
		{
			// Only log if the level is correct.
			if (!doLog) return;
			// Log.
			logBuilder.append(i);
		}
		
		public void log(long l)
		{
			// Only log if the level is correct.
			if (!doLog) return;
			// Log.
			logBuilder.append(l);
		}
		
		public void log(String message)
		{
			// Only log if the level is correct.
			if (!doLog) return;
			// Log.
			logBuilder.append(message);
		}
		
		public void log(Object obj)
		{
			// Only log if the level is correct.
			if (!doLog) return;
			// Log.
			logBuilder.append(obj);
		}
		
		public void log(Object... args)
		{
			// Only log if the level is correct.
			if (!doLog) return;
			// Log.
			for (Object obj : args)
			{
				logBuilder.append(obj);
			}
		}
	}
	
	static IOHandler ioh;
	
	private static ArrayList<Processor> processors = new ArrayList<>();
	
	private static Processor getProcessor(int index)
	{
		Processor p;
		if (processors.size() > index && (p = processors.get(index)) != null)
		{
			return p;
		}
		else
		{
			int divisionFactor = 1;
			for (int i = 1; i < index; i++)
			{
				divisionFactor *= 2 * i + 1;
			}
			final int finalDivisionFactor = divisionFactor;
			final int finalModuloFactor = index * 2 + 1;
			
			p = in -> (in / finalDivisionFactor) % finalModuloFactor;
			
			processors.add(index, p);
			
			return p;
		}
	}
	
	/**
	 * Each element of the return array indicates which other hole should the current one be paired with. 0 indicates the next one, 1 indicates the hole at index + 2, etc.
	 * The output array should be read BACKWARDS.
	 */
	private static int[] indexToConfig(int index, int arrayLength)
	{
		int[] config = new int[arrayLength];
		
		for (int i = 0; i < arrayLength; i++)
		{
			config[i] = getProcessor(i).process(index);
		}
		
		return config;
	}
	
	public static void main(String[] args)
			throws IOException
	{
		ioh = new IOHandler("wormhole");
		ioh.initRun();
		
		Iterator<String> inputIterator = ioh.getIterator();
		WormH[] holes = new WormH[Integer.parseInt(inputIterator.next())];
		for (int i = 0; i < holes.length; i++)
		{
			String[] coordinates = inputIterator.next().split(" ");
			holes[i] = new WormH(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
		}
		
		HoleMap map = new HoleMap(holes);
		
		int numConfigs = 1;
		for (int i = 0; i < holes.length / 2; i++)
		{
			numConfigs *= 2 * i + 1;
		}
		
		int loopCount = 0;
		for (int i = 0; i < numConfigs; i++)
		{
			map.configure(indexToConfig(i, holes.length / 2));
			if (map.hasLoop())
			{
				loopCount++;
			}
		}
		
		ioh.output(new String[]{Integer.toString(loopCount)});
	}
}