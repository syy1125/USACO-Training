/*
ID: syy11251
LANG: JAVA
TASK: castle
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * The index system is set up to allow easier computing:
 * <p>
 * second index
 * ^^^^^^^^^^^^
 * ||||||||||||
 * ||||||||||||
 * ||||||||||||
 * ------------> first index
 */
class CastleMap
{
	public enum Direction
	{
		NORTH, EAST, SOUTH, WEST
	}
	
	public class CastleWall
	{
		private int xPos;
		private int yPos;
		public final Direction wallPositionRelativeToRoom;
		public final int roomSizeAfterBreak;
		
		public CastleWall(int xPos, int yPos, Direction wallPositionRelativeToRoom, int roomSizeAfterBreak)
		{
			this.xPos = xPos;
			this.yPos = yPos;
			this.wallPositionRelativeToRoom = wallPositionRelativeToRoom;
			this.roomSizeAfterBreak = roomSizeAfterBreak;
		}
		
		@Override
		public String toString()
		{
			return (castleBlocks[xPos].length - yPos) + " " + (xPos + 1) + " " + wallPositionRelativeToRoom.name().charAt(0);
		}
	}
	
	private class CastleBlock
	{
		private final HashSet<CastleBlock> connectedBlocks;
		private CastleRoom room;
		
		private CastleBlock()
		{
			this.connectedBlocks = new HashSet<>();
			room = null;
		}
	}
	
	private class CastleRoom
	{
		private final HashSet<CastleBlock> blocks;
		
		private CastleRoom()
		{
			blocks = new HashSet<>();
		}
		
		private boolean addBlock(CastleBlock block)
		{
			block.room = this;
			return blocks.add(block);
		}
		
		public int getSize()
		{
			return blocks.size();
		}
	}
	
	private CastleBlock[][] castleBlocks;
	private HashSet<CastleRoom> castleRooms;
	
	/**
	 * @param config Follows the top-down and then left-right indexing system.
	 */
	public CastleMap(int width, int height, int[][] config)
	{
		castleBlocks = new CastleBlock[width][height];
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				castleBlocks[x][y] = new CastleBlock();
			}
		}
		castleRooms = new HashSet<>();
		
		configure(config);
		formRooms();
	}
	
	private void configure(int[][] config)
	{
		assert config.length == castleBlocks[0].length;
		assert config[0].length == castleBlocks.length;
		
		for (int x = 0; x < castleBlocks.length; x++)
		{
			for (int y = 0; y < castleBlocks[x].length; y++)
			{
				int wallConfig = config[config.length - 1 - y][x];
				if ((wallConfig & 1) == 0)
				{
					castleBlocks[x][y].connectedBlocks.add(castleBlocks[x - 1][y]);
				}
				if ((wallConfig & 2) == 0)
				{
					castleBlocks[x][y].connectedBlocks.add(castleBlocks[x][y + 1]);
				}
				if ((wallConfig & 4) == 0)
				{
					castleBlocks[x][y].connectedBlocks.add(castleBlocks[x + 1][y]);
				}
				if ((wallConfig & 8) == 0)
				{
					castleBlocks[x][y].connectedBlocks.add(castleBlocks[x][y - 1]);
				}
				
			}
		}
	}
	
	private void formRooms()
	{
		for (CastleBlock[] column : castleBlocks)
		{
			for (CastleBlock block : column)
			{
				if (block.room == null)
				{
					CastleRoom newRoom = new CastleRoom();
					castleRooms.add(newRoom);
					floodFill(block, newRoom);
				}
			}
		}
	}
	
	private void floodFill(CastleBlock start, CastleRoom room)
	{
		Deque<CastleBlock> toSearch = new ArrayDeque<>();
		room.addBlock(start);
		toSearch.add(start);
		
		do
		{
			for (CastleBlock block : toSearch.removeFirst().connectedBlocks)
			{
				if (room.addBlock(block))
				{
					toSearch.add(block);
				}
			}
		}
		while (toSearch.size() > 0);
	}
	
	public int getRoomCount()
	{
		return castleRooms.size();
	}
	
	public int getLargestRoomSize()
	{
		int size = 0;
		for (CastleRoom room : castleRooms)
		{
			if (size < room.getSize())
			{
				size = room.getSize();
			}
		}
		return size;
	}
	
	public CastleWall findBestWallToBreak()
	{
		// Find the maximum possible size
		int maxSizeRoom = 0;
		int secondMaxSizeRoom = 0;
		for (CastleRoom room : castleRooms)
		{
			if (room.getSize() > maxSizeRoom)
			{
				secondMaxSizeRoom = maxSizeRoom;
				maxSizeRoom = room.getSize();
			}
		}
		int theoreticalMaxSize = maxSizeRoom + secondMaxSizeRoom;
		
		CastleWall bestWall = new CastleWall(0, 0, null, 0);
		
		// Maybe just brute force it?
		for (int x = 0; x < castleBlocks.length; x++)
		{
			for (int y = 0; y < castleBlocks[x].length; y++)
			{
				if (y < castleBlocks[x].length - 1)
				{
					if (castleBlocks[x][y].room != castleBlocks[x][y + 1].room)
					{
						int newRoomSize = castleBlocks[x][y].room.getSize() + castleBlocks[x][y + 1].room.getSize();
						if (newRoomSize > bestWall.roomSizeAfterBreak)
						{
							bestWall = new CastleWall(x, y, Direction.NORTH, newRoomSize);
						}
					}
				}
				
				if (x < castleBlocks.length - 1)
				{
					if (castleBlocks[x][y].room != castleBlocks[x + 1][y].room)
					{
						int newRoomSize = castleBlocks[x][y].room.getSize() + castleBlocks[x + 1][y].room.getSize();
						if (newRoomSize > bestWall.roomSizeAfterBreak)
						{
							bestWall = new CastleWall(x, y, Direction.EAST, newRoomSize);
						}
					}
				}
			}
		}
		
		return bestWall;
	}
}

public class castle
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
	
	public static final IOHandler ioh = new IOHandler("castle");
	
	private static int[] parse(String row)
	{
		String[] tokens = row.split(" ");
		int[] output = new int[tokens.length];
		
		for (int i = 0; i < output.length; i++)
		{
			output[i] = Integer.parseInt(tokens[i]);
		}
		
		return output;
	}
	
	public static void main(String[] args)
			throws IOException
	{
		ioh.initRun();
		
		Iterator<String> itr = ioh.getIterator();
		String[] dimensions = itr.next().split(" ");
		int width = Integer.parseInt(dimensions[0]);
		int height = Integer.parseInt(dimensions[1]);
		
		LinkedList<int[]> cfgList = new LinkedList<>();
		itr.forEachRemaining(line -> cfgList.add(parse(line)));
		int[][] config = cfgList.toArray(new int[cfgList.size()][]);
		
		CastleMap castleMap = new CastleMap(width, height, config);
		
		CastleMap.CastleWall bestWall = castleMap.findBestWallToBreak();
		
		ioh.output(new String[] {
				Integer.toString(castleMap.getRoomCount()),
				Integer.toString(castleMap.getLargestRoomSize()),
				String.valueOf(bestWall.roomSizeAfterBreak),
				bestWall.toString()
		});
	}
}
