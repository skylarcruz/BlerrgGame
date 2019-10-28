package blerrg;

import jig.Entity;
import jig.ResourceManager;

public class Tile extends Entity {

	public Tile(final float x, final float y, int tileType) {
		super(x, y);
		switch(tileType) {
		case 0: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.TILE_1)); break;
		default: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.TILE_1)); break;
		}
	}
}
