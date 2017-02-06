/*
ID: syy11251
LANG: JAVA
TASK: milk3
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.TreeSet;

class BucketState
{
	/** The capacity of bucket A */
	public static int aCap;
	/** The capacity of bucket B */
	public static int bCap;
	/** The capacity of bucket C */
	public static int cCap;
	
	public final int a;
	public final int b;
	public final int c;
	
	public BucketState(int a, int b, int c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public BucketState pourAB()
	{
		int amount = Math.min(bCap - b, a);
		return new BucketState(a - amount, b + amount, c);
	}
	
	public BucketState pourBA()
	{
		int amount = Math.min(aCap - a, b);
		return new BucketState(a + amount, b - amount, c);
	}
	
	public BucketState pourBC()
	{
		int amount = Math.min(cCap - c, b);
		return new BucketState(a, b - amount, c + amount);
	}
	
	public BucketState pourCB()
	{
		int amount = Math.min(bCap - b, c);
		return new BucketState(a, b + amount, c - amount);
	}
	
	public BucketState pourCA()
	{
		int amount = Math.min(aCap - a, c);
		return new BucketState(a + amount, b, c - amount);
	}
	
	public BucketState pourAC()
	{
		int amount = Math.min(cCap - c, a);
		return new BucketState(a - amount, b, c + amount);
	}
	
	public BucketState[] getNextStates()
	{
		return new BucketState[] {
				pourAB(), pourAC(), pourBA(), pourBC(), pourCA(), pourCB()
		};
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		else if (!(obj instanceof BucketState))
		{
			return false;
		}
		else
		{
			BucketState other = (BucketState) obj;
			return this.a == other.a && this.b == other.b && this.c == other.c;
		}
	}
	
	@Override
	public int hashCode()
	{
		return a * 13 + b * 29 + c * 41;
	}
	
	@Override
	public String toString()
	{
		return "BucketState[" + a + "," + b + "," + c + "]";
	}
}

public class milk3
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
			log(LogLevel.TRACE, args);
		}
		
		/**
		 * A convenience function to print to info.
		 */
		public void info(Object... args)
		{
			log(LogLevel.TRACE, args);
		}
		
		/**
		 * A convenience function to print to warn.
		 */
		public void warn(Object... args)
		{
			log(LogLevel.TRACE, args);
		}
		
		/**
		 * A convenience function to print to error.
		 */
		public void error(Object... args)
		{
			log(LogLevel.TRACE, args);
		}
	}
	
	
	public static final IOHandler ioh = new IOHandler("milk3");
	
	public static void main(String[] args)
			throws IOException
	{
		ioh.initRun();
		String[] capacities = ioh.getData()[0].split(" ");
		BucketState.aCap = Integer.parseInt(capacities[0]);
		BucketState.bCap = Integer.parseInt(capacities[1]);
		BucketState.cCap = Integer.parseInt(capacities[2]);
		
		LinkedList<BucketState> toExplore = new LinkedList<>();
		HashSet<BucketState> possibleStates = new HashSet<>();
		toExplore.add(new BucketState(0, 0, BucketState.cCap));
		possibleStates.add(new BucketState(0, 0, BucketState.cCap));
		
		do
		{
			BucketState[] nextStates = toExplore.remove().getNextStates();
			for (BucketState state : nextStates)
			{
				if (possibleStates.add(state))
				{
					toExplore.add(state);
				}
			}
		}
		while (toExplore.size() > 0);
		
		SortedSet<Integer> possibleCAmount = new TreeSet<>();
		for (BucketState state : possibleStates)
		{
			if (state.a == 0)
			{
				possibleCAmount.add(state.c);
			}
		}
		
		StringBuilder output = new StringBuilder();
		Iterator<Integer> amountIterator = possibleCAmount.iterator();
		output.append(amountIterator.next());
		while (amountIterator.hasNext())
		{
			output.append(" ");
			output.append(amountIterator.next());
		}
		
		ioh.output(output.toString());
	}
}
