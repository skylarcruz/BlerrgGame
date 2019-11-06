package blerrg;

import org.newdawn.slick.Image;

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
	public Image image;
	
	public Tile(final float x, final float y, int r, int c, TileType typ) {
		super(x, y);
		
		//Set the row and column of this tile
		row = r;
		col = c;
		type = typ;
		
		switch(type) {
		case FLOOR: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.TILE_1)); break;
		case WALL: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.TILE_1)); break;
		
		default: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.TILE_1)); break;
		}
	}

	public void setImage(Image img) {
		//update the image variable
		image = img;
		
		//replace image
		addImageWithBoundingBox(image);
		
		
	}
	
	
	public static TileType typeFromGID(int t_gid) {
		// TODO Auto-generated method stub
		
		if(t_gid == 8) return TileType.FLOOR;
		else {
			//System.out.println("GID is not 8! Should be wall");
			return TileType.WALL;
		}
		
	}
}
