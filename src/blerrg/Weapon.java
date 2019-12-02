package blerrg;

import jig.Entity;
import jig.ResourceManager;

enum type{
	crossbow,
	dagger,
	grenada,
	laserdeathmachinemadeofrabbitsandcandycanes,
	shotgun;
};

public class Weapon extends Entity{
	
	
	private float x;
	private float y;
	
	private float direction;
	
	type id;

	public Weapon(final float x, final float y, type id, final float direction) {
		this.x = x;
		this.y = y;
		
		this.direction = direction;
		
		this.id = id;
		
		switch(this.id) {
			case shotgun: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN));
			default: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN));
		}
	}
}
