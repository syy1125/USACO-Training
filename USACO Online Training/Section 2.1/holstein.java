/*
ID: syy11251
LANG: JAVA
TASK: holstein
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

public class holstein
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
	
	public static final IOHandler ioh = new IOHandler("holstein");
	
	/**
	 * Checks whether the current vitamin level meets the requirement.
	 */
	private static boolean checkRequirement(int[] requirement, int[] current)
	{
		assert requirement.length == current.length;
		
		for (int i = 0; i < requirement.length; i++)
		{
			if (requirement[i] > current[i])
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean enoughFeed(int[] requirement, boolean[] feeds, int[][] feedVitamins)
	{
		assert feeds.length == feedVitamins.length;
		assert requirement.length == feedVitamins[0].length;
		
		int[] vitamins = new int[requirement.length];
		for (int i = 0; i < feedVitamins.length; i++)
		{
			if (feeds[i])
			{
				for (int j = 0; j < vitamins.length; j++)
				{
					vitamins[j] += feedVitamins[i][j];
				}
			}
			
			if (i % 10 == 9 && checkRequirement(requirement, vitamins))
			{
				return true;
			}
		}
		
		return checkRequirement(requirement, vitamins);
	}
	
	private static boolean[] toFeedConfig(long index, int length)
	{
		boolean[] output = new boolean[length];
		for (int i = 0; i < length; i++)
		{
			output[i] = (index & 1 << (length - i - 1)) == 0;
		}
		
		return output;
	}
	
	private static int feedCount(boolean[] feeds)
	{
		int count = 0;
		for (boolean feed : feeds)
		{
			if (feed)
			{
				count++;
			}
		}
		return count;
	}
	
	public static void main(String[] args)
			throws IOException
	{
		ioh.initRun();
		String[] vitaminRequirements = ioh.getData()[1].split(" ");
		
		int[] requirements = new int[vitaminRequirements.length];
		for (int i = 0; i < requirements.length; i++)
		{
			requirements[i] = Integer.parseInt(vitaminRequirements[i]);
		}
		
		int[][] feedData = new int[ioh.getData().length - 3][requirements.length];
		for (int i = 0; i < feedData.length; i++)
		{
			int[] feed = new int[requirements.length];
			String[] feedInput = ioh.getData()[i + 3].split(" ");
			for (int j = 0; j < feed.length; j++)
			{
				feed[j] = Integer.parseInt(feedInput[j]);
			}
			feedData[i] = feed;
		}
		
		int minFeedCount = Integer.MAX_VALUE;
		boolean[] bestConfig = null;
		for (int i = (1 << feedData.length) - 1; i >= 0; i--)
		{
			boolean[] feedConfig = toFeedConfig(i, feedData.length);
			if (enoughFeed(requirements, feedConfig, feedData))
			{
				int feedCount = feedCount(feedConfig);
				if (feedCount < minFeedCount)
				{
					minFeedCount = feedCount;
					bestConfig = feedConfig;
				}
			}
		}
		
		String output = String.valueOf(minFeedCount);
		for (int i = 0; i < bestConfig.length; i++)
		{
			if (bestConfig[i])
			{
				output += " " + (i + 1);
			}
		}
		ioh.output(output);
	}
}
