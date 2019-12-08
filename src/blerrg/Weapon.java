package blerrg;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;


public class Weapon extends Entity{
	
	//coarseGrainedCollisionBoundary = Entity.CIRCLE;
	private float x;
	private float y;
	private double direction;
	String type;
	private int flipped; // 0 for false, 1 for true
	private int cooldown = 100;

	public Weapon(final float x, final float y, String type, final double direction){
		
		this.x = x;
		this.y = y;
		
		this.direction = direction;
		
		this.type = type;
		flipped = 0;
		
		switch(this.type) {
			case BlerrgGame.WEAPON_CROSSBOW: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_CROSSBOW)); break;
			case BlerrgGame.WEAPON_KNIFE: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_KNIFE)); break;
			case BlerrgGame.WEAPON_RIFLE: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_RIFLE)); break;
			case BlerrgGame.WEAPON_SHOTGUN: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN)); break;
			case BlerrgGame.WEAPON_SMG: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SMG)); break;
			default: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_KNIFE));
		}
	}
	
	public Weapon(Weapon weapon) {
		this.x = weapon.x;
		this.y = weapon.y;
		this.direction = weapon.direction;
		this.type = weapon.type;
		
		switch(this.type) {
			case BlerrgGame.WEAPON_CROSSBOW: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_CROSSBOW)); break;
			case BlerrgGame.WEAPON_KNIFE: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_KNIFE)); break;
			case BlerrgGame.WEAPON_RIFLE: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_RIFLE)); break;
			case BlerrgGame.WEAPON_SHOTGUN: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN)); break;
			case BlerrgGame.WEAPON_SMG: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SMG)); break;
			default: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_KNIFE));
		}
	}
	
	public Weapon getCopy() {
		return this;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public String getType() {
		return  this.type;
	}
	
	public void directionSwap(){
		switch(type) {
			case BlerrgGame.WEAPON_CROSSBOW: 
				
				switch(flipped) {
					case 0: removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_CROSSBOW));
							addImage(ResourceManager.getImage(BlerrgGame.WEAPON_CROSSBOW_R));
							flipped = 1;
							break;
					case 1: removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_CROSSBOW_R));
							addImage(ResourceManager.getImage(BlerrgGame.WEAPON_CROSSBOW));
							flipped = 0;
							break;
				}
				break;
				
			case BlerrgGame.WEAPON_KNIFE: 
				
				switch(flipped) {
					case 0: removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_KNIFE));
							addImage(ResourceManager.getImage(BlerrgGame.WEAPON_KNIFE_R));
							flipped = 1;
							break;
					case 1: removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_KNIFE_R));
							addImage(ResourceManager.getImage(BlerrgGame.WEAPON_KNIFE));
							flipped = 0;
							break;
				}
				break;
				
			case BlerrgGame.WEAPON_RIFLE: 
				
				switch(flipped) {
					case 0: removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_RIFLE));
							addImage(ResourceManager.getImage(BlerrgGame.WEAPON_RIFLE_R));
							flipped = 1;
							break;
					case 1: removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_RIFLE_R));
							addImage(ResourceManager.getImage(BlerrgGame.WEAPON_RIFLE));
							flipped = 0;
							break;
				}
				break;
			
			case BlerrgGame.WEAPON_SHOTGUN: 
				
				switch(flipped) {
					case 0: removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN));
							addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN_R));
							flipped = 1;
							break;
					case 1: removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN_R));
							addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN));
							flipped = 0;
							break;
				}
				break;
			
			case BlerrgGame.WEAPON_SMG: 
				
				switch(flipped) {
					case 0: removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_SMG));
							addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SMG_R));
							flipped = 1;
							break;
					case 1: removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_SMG_R));
							addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SMG));
							flipped = 0;
							break;
				}
				break;
		}
	}
	/*
	 * receive the angle that the weapon should point toward
	 */
	public double getDirection() {
		return direction;
	}
	
	/*
	 * set the angle for the weapon to point toward
	 */
	public void setDirection(double direction) {
		this.direction = direction;
	}
	public int getFlipped() {
		return flipped;
	}
	public void update(int delta) {
		if(cooldown < 0) cooldown -= delta;
		else cooldown = 100;
		
	}
	
}
