/*
ID: syy11251
LANG: JAVA
TASK: sort3
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.ListIterator;

class ThreeValueSorter
{
	private int swaps;
	private int twoStart;
	private int threeStart;
	private int[] values;
	
	public ThreeValueSorter(int[] values)
	{
		swaps = 0;
		this.values = values;
	}
	
	public int getSwaps()
	{
		return swaps;
	}
	
	public void sort()
	{
		findStarts();
		for (int i = 0; i < twoStart; i++)
		{
			if (values[i] != 1)
			{
				if (values[i] == 3)
				{
					try
					{
						swap(i, find(1, threeStart));
					}
					catch (RuntimeException ignored)
					{
						swap(i, find(1, twoStart));
					}
				}
				else
				{
					swap(i, find(1, twoStart));
				}
			}
		}
		for (int i = twoStart; i < threeStart; i++)
		{
			if (values[i] != 2)
			{
				swap(i, find(2, threeStart));
			}
		}
	}
	
	/**
	 * Finds where the first two and the first three are supposed to be.
	 */
	private void findStarts()
	{
		int[] counts = new int[4];
		for (int i = 0; i < values.length; i++)
		{
			counts[values[i]]++;
		}
		
		twoStart = counts[1];
		threeStart = counts[1] + counts[2];
	}
	
	private int find(int num, int startIndex)
	{
		for (int i = startIndex; i < values.length; i++)
		{
			if (values[i] == num)
			{
				return i;
			}
		}
		throw new RuntimeException("Number not found.");
	}
	
	private void swap(int index1, int index2)
	{
		int temp = values[index2];
		values[index2] = values[index1];
		values[index1] = temp;
		swaps++;
	}
}

public class sort3
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
	
	/**
	 * Handles USACO style interaction with files. Also has integrated test methods.
	 *
	 * @author syy1125
	 */
	static class IOHandler
	{
		private static final String SPACE = " ";
		private static final String NEW_LINE = System.getProperty("line.separator");
		private static final String FILE_SEPARATOR = System.getProperty("file.separator");
		
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
				fileName = System.getProperty("user.home") + FILE_SEPARATOR + "Documents" + FILE_SEPARATOR + "USACO Test" + FILE_SEPARATOR + prgmName
						+ FILE_SEPARATOR + prgmName;
			}
			else
			{
				fileName = prgmName;
			}
			
			BufferedReader in = null;
			try
			{
				in = new BufferedReader(new FileReader(fileName + ".in"));
				System.out.println("Loaded file: " + fileName + ".in");
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
			log(LogLevel.INFO, "File reading complete!");
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
			catch (IOException e)
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
			
			log(LogLevel.INFO, "Main output writing complete.");
			
			if (test)
			{
				// Write the output log
				out.newLine();
				out.write("Log:");
				out.newLine();
				out.write(logBuilder.toString());
				out.newLine();
			}
			
			System.out.println("Output complete.");
			out.close();
			System.exit(0);
		}
		
		/**
		 * A convenience function to output one single line.
		 *
		 * @param content The content of the output
		 * @throws IOException
		 */
		public void output(String content)
				throws IOException
		{
			output(new String[] {content});
		}
		
		/**
		 * Appends some information about the log, such as the time after start, to the log builder.
		 * <p>
		 * This function will wrap the argument {@code msg} in square brackets and append a space after it.
		 */
		private void appendLogInfo(String msg)
		{
			logBuilder.append('[');
			logBuilder.append(msg);
			logBuilder.append(']');
			logBuilder.append(SPACE);
		}
		
		/**
		 * Adds a log entry to the program log.
		 *
		 * @param level The level of the log; the message will not be recorded if the level is too low
		 * @param args  The information to log
		 */
		public void log(LogLevel level, Object... args)
		{
			// Check log level
			if (level.lvl > baseLevel.lvl)
			{
				return;
			}
			
			// Log the time and level.
			appendLogInfo(timeFormat.format((System.currentTimeMillis() - startTime) / 1000D));
			appendLogInfo(level.msg);
			
			// Log the arguments.
			for (Object obj : args)
			{
				logBuilder.append(obj);
				logBuilder.append(SPACE);
			}
			
			// New line
			logBuilder.append(NEW_LINE);
		}
		
		/**
		 * A convenience function to print to trace.
		 */
		public void trace(Object... args)
		{
			log(LogLevel.TRACE, args);
		}
		
		/**
		 * A convenience function to print to debug.
		 */
		public void debug(Object... args)
		{
			log(LogLevel.DEBUG, args);
		}
		
		/**
		 * A convenience function to print to info.
		 */
		public void info(Object... args)
		{
			log(LogLevel.INFO, args);
		}
		
		/**
		 * A convenience function to print to warn.
		 */
		public void warn(Object... args)
		{
			log(LogLevel.WARN, args);
		}
		
		/**
		 * A convenience function to print to error.
		 */
		public void error(Object... args)
		{
			log(LogLevel.ERROR, args);
		}
	}
	
	
	public static final IOHandler ioh = new IOHandler("sort3");
	
	public static void main(String[] args)
			throws IOException
	{
		ioh.initRun();
		
		int[] values = new int[ioh.getData().length - 1];
		for (int i = 0; i < values.length; i++)
		{
			values[i] = Integer.parseInt(ioh.getData()[i + 1]);
		}
		ThreeValueSorter sorter = new ThreeValueSorter(values);
		
		sorter.sort();
		
		ioh.output(String.valueOf(sorter.getSwaps()));
	}
}
