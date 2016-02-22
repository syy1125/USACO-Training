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
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

class HoleXCompare implements Comparator<Hole>
{
	public static final HoleXCompare instance = new HoleXCompare();

	public int compare(Hole h1, Hole h2)
	{
		return h1.xPos - h2.xPos;
	}
}

class Hole
{
	int xPos;
	int yPos;
	
	Hole linked;
	
	public Hole(int x, int y)
	{
		xPos = x;
		yPos = y;
		linked = null;
	}
	
	Hole copy()
	{
		return new Hole(xPos, yPos);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Hole) {
			Hole h = (Hole) obj;
			return (h.xPos == xPos && h.yPos == yPos);
		}
		return false;
	}
}

class HoleMap
{
	List<Hole> allHoles;
	
	HoleMap()
	{
		allHoles = new ArrayList<Hole>();
	}
	
	/**
	 * Adds a pair of linked wormholes to the map. This method will not link the holes themselves, but copies of the holes, so the original holes are
	 * safe.
	 */
	void addPair(Hole h1, Hole h2)
	{
		Hole hole1 = h1.copy();
		Hole hole2 = h2.copy();
		hole1.linked = hole2;
		hole2.linked = hole1;
		allHoles.add(hole1);
		allHoles.add(hole2);
	}
	
	HoleMap copy()
	{
		List<Hole> holeBuffer = new ArrayList<Hole>();
		holeBuffer.addAll(allHoles);
		HoleMap copy = new HoleMap();

		while (holeBuffer.size() > 0) {
			Hole firstHole = holeBuffer.remove(0);
			Hole linked = holeBuffer.remove(holeBuffer.indexOf(firstHole.linked));
			copy.addPair(firstHole, linked);
		}
		
		return copy;
	}
	
	boolean hasPair(Hole h1, Hole h2)
	{	
		int index = allHoles.indexOf(h1);
		return (index >= 0 && allHoles.get(index).linked.equals(h2));
	}
	
	boolean isIncludedIn(HoleMap other)
	{	
		for (Hole h : allHoles) {
			if (!other.hasPair(h, h.linked)) return false;
		}
		return true;
	}
}

/**
 * A {@code Cow} searches through a map to find any infinite loops.
 */
class Cow
{
	static List<HoleMap> mapWithLoops = new ArrayList<HoleMap>();
	static int currentIndex = 0;
	static int searchCount = 0;

	HoleMap map;
	Set<Hole> used;
	int index;
	

	Cow(HoleMap holeMap)
	{
		map = holeMap;
		used = new HashSet<Hole>();

		index = currentIndex;
		currentIndex ++;
	}
	
	/**
	 * @param from Searches from this hole
	 * 
	 * @return The next hole the cow will enter, or null if the cow leaves the map.
	 */
	Hole findNextHole(Hole from)
	{
		List<Hole> holeList = new ArrayList<Hole>();
		
		// Add all the holes with the same y coordinate.
		for (Hole h : map.allHoles) {
			if (from.yPos == h.yPos)
			{
				holeList.add(h);
			}
		}
		
		Collections.sort(holeList, HoleXCompare.instance);
		int index = holeList.indexOf(from);
		
		// Try to get the next hole, but it's okay if the index is the last one.
		Hole next = null;
		try {
			next = holeList.get(index + 1);
		}
		catch (IndexOutOfBoundsException ex) {}
		
		return next;
	}
	
	/**
	 * @param exit The hole that the cow exited from
	 * 
	 * @return Whether an infinite loop is found.
	 */
	boolean searchFrom(Hole exit)
	{
		List<Hole> holeTrace = new ArrayList<Hole>();
		holeTrace.add(exit);
		
		Hole nextHole;
		while (true)
		{
			nextHole = findNextHole(exit);
			if (nextHole == null) {
				// Escaped
				return false;
			}
			
			// Teleport to the linked hole
			exit = nextHole.linked;
			
			int exitIndex = holeTrace.indexOf(exit);
			if (exitIndex >= 0) {
				// Exited from the same hole.
				// Save the information about this loop into the mapWithLoops.
				HoleMap loopMap = new HoleMap();
				Hole pastExit;
				for (int i = exitIndex; i < holeTrace.size(); i ++) {
					pastExit = holeTrace.get(i);
					loopMap.addPair(pastExit, pastExit.linked);
				}
				mapWithLoops.add(loopMap);

				return true;
			}
			holeTrace.add(exit);
			
			if (!used.add(exit))
			{
				// Escaped from here before.
				return false;
			}

		}
	}
	
	/**
	 * Searches in the given map for infinite loops.
	 * 
	 * @param index The index of this map, used for debugging purposes
	 * @return Whether there is an infinite loop in this map
	 */
	public boolean search() throws Exception
	{
		for (HoleMap loopMap : mapWithLoops) {
			if (loopMap.isIncludedIn(map)) {
				wormhole.ioh.logNewEntry(wormhole.LogLevel.DEBUG);
				wormhole.ioh.log("Found a map with a previously determined loop. Exiting.");
				return true;
			}
		}
		
		searchCount ++;

		// Maybe we can save some time by checking to see
		// Since only the holes are the important things here, why don't we just search from the holes?
		for (Hole startHole : map.allHoles)
		{
			if (used.contains(startHole)) {
				continue;
			}
			
			if (searchFrom(startHole)) {
				wormhole.ioh.logNewEntry(wormhole.LogLevel.TRACE);
				wormhole.ioh.log("Found an infinite loop in map#", index);
				return true;
			}
		}
		
		wormhole.ioh.logNewEntry(wormhole.LogLevel.TRACE);
		wormhole.ioh.log("No loop was found in map#");
		wormhole.ioh.log(index);
		return false;
	}
	
}

class MutableInt
{
	int value;
	
	public MutableInt(int value)
	{
		this.value = value;
	}
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
		private void loadFile() throws IOException
		{
			if (test) {
				// Use the documents folder
				fileName = System.getProperty("user.home") + fileSeparator + "Documents" + fileSeparator + "USACO Test" + fileSeparator + prgmName
						+ fileSeparator + prgmName;
			}
			else {
				fileName = prgmName;
			}
			
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(fileName + ".in"));
			}
			catch (FileNotFoundException e) {
				System.err.println("File is not found: " + fileName + ".in");
				System.exit(1);
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
		public void initRun() throws IOException
		{
			test = false;
			baseLevel = LogLevel.OFF;
			loadFile();
		}
		
		/**
		 * Initialize the <code>IOHandler</code> to test conditions.
		 * 
		 * @throws IOException
		 */
		public void initTest() throws IOException
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
		public void initDebug() throws IOException
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
		public void initTrace() throws IOException
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
		public void output(String[] content) throws IOException
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
			if (level.lvl > baseLevel.lvl) {
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
			for (Object obj : args) {
				logBuilder.append(obj);
			}
		}
	}

	static enum LogLevel
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

	static IOHandler ioh = new IOHandler("wormhole");
	
	/**
	 * Attempts to create all the possible pairing in a map with the given holes.
	 * 
	 * @param holes The list of available holes
	 * @return All the maps using the unique possible pairings of the holes
	 */
	static List<HoleMap> tryPairHoles(List<Hole> holes)
	{
		List<HoleMap> mapList = new ArrayList<HoleMap>();

		// This is the last pair
		if (holes.size() <= 2)
		{
			HoleMap map = new HoleMap();
			map.addPair(holes.get(0), holes.get(1));
			mapList.add(map);
			return mapList;
		}
		
		// Remove the 1st hole
		Hole hole2 = holes.remove(0);
		for (int i = 0; i < holes.size(); i ++)
		{
			// Temporarily remove the taken hole
			Hole hole1 = holes.remove(i);
			List<HoleMap> subMapList = tryPairHoles(holes);
			
			for (HoleMap subMap : subMapList)
			{
				subMap.addPair(hole1, hole2);
			}
			
			// Add back the removed hole
			holes.add(i, hole1);
			mapList.addAll(subMapList);
		}
		holes.add(0, hole2);
		
		return mapList;
	}
	
	static int subMapCount(int holeCount)
	{
		if (holeCount <= 2) return 1;
		return (holeCount - 1) * subMapCount(holeCount - 2);
	}
	
	static void topDownSearch(List<Hole> holes, HoleMap superMap, MutableInt storage) throws Exception
	{	
		HoleMap map = superMap.copy();
		
		// This is the last pair
		if (holes.size() <= 2)
		{
			map.addPair(holes.get(0), holes.get(1));
			if (new Cow(map).search()) {
				storage.value ++;
			}
			
			return;
		}
		
		// Remove the 1st hole
		Hole hole2 = holes.remove(0);
		for (int i = 0; i < holes.size(); i ++)
		{
			// Temporarily remove the taken hole
			Hole hole1 = holes.remove(i);
			map = superMap.copy();
			map.addPair(hole1, hole2);
			
			// If this map already has a loop, we don't need to search further.
			if (new Cow(map).search()) {
				int weight = subMapCount(holes.size());
				wormhole.ioh.logNewEntry(LogLevel.DEBUG);
				wormhole.ioh.log("Skipped ", weight, " submaps because of loop found in supermap.");
				storage.value += weight;
			}
			// Otherwise, search deeper down.
			else {
				topDownSearch(holes, map, storage);
			}

			// Add back the removed hole
			holes.add(i, hole1);
		}
		holes.add(0, hole2);
	}

	public static void main(String[] args) throws Exception
	{
		ioh.initTest();
		String[] in = ioh.getData();
		
		// Initialize all the holes
		int holeCount = in.length - 1;
		List<Hole> holes = new ArrayList<Hole>();
		for (int i = 0; i < holeCount; i ++)
		{
			String[] coordData = in[i + 1].split(" ");
			holes.add(new Hole(Integer.parseInt(coordData[0]), Integer.parseInt(coordData[1])));
		}

		// Find out all the possibilities
		List<HoleMap> possibilities = tryPairHoles(holes);
		ioh.logNewEntry(LogLevel.INFO);
		ioh.log("Found ");
		ioh.log(possibilities.size());
		ioh.log(" possible pairings with the current maps.");

		int totalLoops = 0;

		for (HoleMap map : possibilities)
		{
			if (new Cow(map).search()) {
				totalLoops ++;
			}
		}
		ioh.logNewEntry(LogLevel.INFO);
		ioh.log("Search complete.");
		
		/*
		ioh.newLogEntry(LogLevel.INFO);
		ioh.log("Initiating alternative top-down search method.");
		MutableInt result = new MutableInt(0);
		topDownSearch(holes, new HoleMap(), result);
		totalLoops = result.value;
		ioh.newLogEntry(LogLevel.INFO);
		ioh.log("Top-down search completed.");
		*/

		ioh.logNewEntry(LogLevel.INFO);
		ioh.log("Final count of basic map with loops is ", Cow.mapWithLoops.size());
		ioh.logNewEntry(LogLevel.INFO);
		ioh.log("Search executed ");
		ioh.log(Cow.searchCount);
		ioh.log(" times.");
		ioh.output(new String[] { Integer.toString(totalLoops) });
	}
}
