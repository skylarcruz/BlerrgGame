package blerrg;

import jig.Entity;
import jig.ResourceManager;

public class PowerUp extends Entity {
	
	private int powerType;
	
	//0 - armor, 1 - stam, 2 - xray
	public PowerUp(final float x, final float y, int type) {
		super(x, y);
		
		powerType = type;
		switch(type) {
			case 0: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.ARMOR_POWER_UP)); break;
			case 1: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.STAMINA_POWER_UP)); break;
			case 2: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.XRAY_POWER_UP)); break;
			default: break;
		}
	}
	
	public int getPowerType() {
		return powerType;
	}
}
