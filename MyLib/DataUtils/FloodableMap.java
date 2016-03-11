import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

enum EnumDir
{
	UP, DOWN, LEFT, RIGHT;
	public static final EnumDir[] VALUES = values();
}

class TilePos
{
	private static final int HASH_X_MULT = 37;
	private static final int HASH_Y_MULT = 29;
	
	private final int xPos;
	private final int yPos;
	
	public TilePos(int x, int y)
	{
		xPos = x;
		yPos = y;
	}
	
	@Override
	public int hashCode()
	{
		return xPos * HASH_X_MULT + yPos * HASH_Y_MULT;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof TilePos)) return false;
		TilePos pos = (TilePos) obj;
		return xPos == pos.xPos && yPos == pos.yPos;
	}
	
	public int getX()
	{
		return xPos;
	}
	
	public int getY()
	{
		return yPos;
	}
	
	public TilePos offset(EnumDir dir)
	{
		return offset(dir, 1);
	}
	
	public TilePos offset(EnumDir dir, int numTiles)
	{
		switch (dir)
		{
			case DOWN:
				return new TilePos(xPos, yPos - numTiles);
			case LEFT:
				return new TilePos(xPos - numTiles, yPos);
			case RIGHT:
				return new TilePos(xPos + numTiles, yPos);
			case UP:
				return new TilePos(xPos, yPos + numTiles);
			default:
				return null;
				
		}
	}
}

class FloodableTile<Type extends ICloneable<? extends Type>> implements ICloneable<FloodableTile<Type>>
{
	protected Type data;
	private boolean filled;
	protected TilePos pos;
	
	public FloodableTile(Type dataStorage, TilePos position)
	{
		data = dataStorage;
		filled = false;
		pos = position;
	}
	
	public void setData(Type dataStorage)
	{
		data = dataStorage;
	}
	
	public Type getData()
	{
		return data;
	}
	
	public void setFill(boolean fill)
	{
		filled = fill;
	}
	
	public boolean isFilled()
	{
		return filled;
	}
	
	public TilePos getPos()
	{
		return pos;
	}
	
	@Override
	public FloodableTile<Type> clone()
	{
		return new FloodableTile<Type>(data.clone(), pos);
	}
}

public class FloodableMap
{
	private int fillCounter;
	Map<TilePos, FloodableTile<?>> map;
	
	public FloodableMap()
	{
		fillCounter = 0;
		map = new HashMap<TilePos, FloodableTile<?>>();
	}
	
	public void setTile(TilePos pos, FloodableTile<?> tile)
	{
		map.put(pos, tile);
	}
	
	public FloodableTile<?> getTile(TilePos pos)
	{
		return map.get(pos);
	}
	
	protected Collection<TilePos> getAdjacentTiles(TilePos from)
	{
		List<TilePos> tiles = new ArrayList<TilePos>();
		
		for (EnumDir dir : EnumDir.VALUES)
		{
			tiles.add(from.offset(dir));
		}
		
		return tiles;
	}
	
	public void floodFill(TilePos from)
	{
		LinkedList<TilePos> tileQueue = new LinkedList<TilePos>();
		tileQueue.add(from);
		fillCounter ++;
		
		// While there is still tiles to be processed
		while (tileQueue.size() > 0)
		{
			TilePos nextTilePos = tileQueue.pollFirst();
			if (nextTilePos == null) continue;
			
			tileQueue.addAll(getAdjacentTiles(nextTilePos));
			getTile(nextTilePos).setFill(true);
		}
	}
	
	public int getFillCounter()
	{
		return fillCounter;
	}
	
	public void reset()
	{
		fillCounter = 0;
		for (Entry<TilePos, FloodableTile<?>> e : map.entrySet())
		{
			e.getValue().setFill(false);
		}
	}
	
	public void setRegion(int minX, int minY, int maxX, int maxY, FloodableTile<?> tileToFill)
	{
		for (int x = minX; x <= maxX; x ++) {
			for (int y = minY; y <= maxY; y ++) {
				setTile(new TilePos(x, y), tileToFill.clone());
			}
		}
	}
}
