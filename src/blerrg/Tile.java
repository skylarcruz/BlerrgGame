package blerrg;

import jig.Entity;
import jig.ResourceManager;

public class Tile extends Entity {

	public enum TileType{
		FLOOR, WALL
	}
	
	public static final Integer TILE_BASE_SIZE = 32;
	public static final Float TILE_DEF_SCALE = 1.0f;

	public static Integer size = TILE_BASE_SIZE;
	public static float scale = TILE_DEF_SCALE;
	
	
	public int row, col;
	
	public TileType type;
	
	public Tile(final float x, final float y, int r, int c, int tileType) {
		super(x, y);
		
		//Set the row and column of this tile
		row = r;
		col = c;
		
		switch(tileType) {
		case 0: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.TILE_1)); break;
		default: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.TILE_1)); break;
		}
	}
}
