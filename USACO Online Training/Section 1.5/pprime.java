/*
ID: syy11251
LANG: JAVA
TASK: pprime
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class pprime
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
			
			System.out.println("Output complete. Time: " + timeFormat.format((System.currentTimeMillis() - startTime) / 1000D) + "s");
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
	
	public static final IOHandler ioh = new IOHandler("pprime");
	
	private static List<Integer> buildPrimeList(int upperBound)
	{
		boolean[] notPrime = new boolean[upperBound + 1];
		double sqrt = Math.sqrt(upperBound + 1);
		for (int num = 2; num < sqrt; num++)
		{
			if (!notPrime[num]) // If prime
			{
				// Set all multiples to not prime
				for (int j = 2 * num; j <= upperBound; j += num)
				{
					notPrime[j] = true;
				}
			}
		}
		
		LinkedList<Integer> output = new LinkedList<>();
		for (int num = 2; num <= upperBound; num++)
		{
			if (!notPrime[num])
			{
				output.add(num);
			}
		}
		
		return output;
	}
	
	private static boolean isPalindrome(String word)
	{
		int wordLength = word.length();
		for (int i = 0; i < wordLength / 2; i++)
		{
			if (word.charAt(i) != word.charAt(wordLength - 1 - i))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static List<Integer> buildSieve(int upperBound)
	{
		boolean[] notPrime = new boolean[(int) Math.sqrt(upperBound) + 1];
		double sqrt = Math.sqrt(notPrime.length);
		for (int num = 2; num < sqrt; num++)
		{
			if (!notPrime[num]) // If prime
			{
				// Set all multiples to not prime
				for (int j = 2 * num; j < notPrime.length; j += num)
				{
					notPrime[j] = true;
				}
			}
		}
		
		LinkedList<Integer> primes = new LinkedList<>();
		for (int num = 2; num < notPrime.length; num++)
		{
			if (!notPrime[num])
			{
				primes.add(num);
			}
		}
		
		return primes;
	}
	
	private static boolean isPrime(int num, List<Integer> sieve)
	{
		for (Integer prime : sieve)
		{
			if (num == prime)
			{
				return true;
			}
			if (num % prime == 0)
			{
				return false;
			}
		}
		return true;
	}
	
	private static int intPow(int base, int exponent)
	{
		assert exponent >= 0;
		if (exponent <= 0)
		{
			return 1;
		}
		else
		{
			return base * intPow(base, exponent - 1);
		}
	}
	
	private static List<Integer> palindromes(int length)
	{
		List<Integer> output = new LinkedList<>();
		if ((length & 1) == 1)
		{
			int start = intPow(10, length / 2 - 1);
			int end = intPow(10, length / 2);
			
			if (length == 1)
			{
				for (int i = 0; i < 10; i++)
				{
					output.add(i);
				}
			}
			else
			{
				for (int outer = start; outer < end; outer++)
				{
					for (int center = 0; center < 10; center++)
					{
						StringBuffer outerString = new StringBuffer(Integer.toString(outer));
						output.add(Integer.parseInt(outerString.toString() + Integer.toString(center) + outerString.reverse().toString()));
					}
				}
			}
		}
		else
		{
			int start = intPow(10, length / 2 - 1);
			int end = intPow(10, length / 2);
			for (int outer = start; outer < end; outer++)
			{
				StringBuffer outerString = new StringBuffer(Integer.toString(outer));
				output.add(Integer.parseInt(outerString.toString() + outerString.reverse().toString()));
			}
		}
		
		return output;
	}
	
	public static void main(String[] args)
			throws IOException, ExecutionException, InterruptedException
	{
		ioh.initRun();
		String[] bounds = ioh.getData()[0].split(" ");
		int lowerBound = Integer.parseInt(bounds[0]);
		int upperBound = Integer.parseInt(bounds[1]);
		
		int lowerLength = Integer.toString(lowerBound).length();
		int upperLength = Integer.toString(upperBound).length();
		
		List<Integer> sieve = buildSieve(upperBound);
		List<Integer> palindromePrimes = new LinkedList<>();
		for (int length = lowerLength; length <= upperLength; length++)
		{
			palindromePrimes.addAll(palindromes(length).stream()
													   .filter(number -> number >= lowerBound && number <= upperBound)
													   .filter(number -> isPrime(number, sieve))
													   .collect(Collectors.toList()));
		}
		
		List<String> output = palindromePrimes.stream()
											  .map(number -> Integer.toString(number))
											  .collect(Collectors.toList());
		ioh.output(output.toArray(new String[output.size()]));
	}
}
