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
	private int cooldown;
	
	private int clipSize = 1;
	private int currClip = 1;
	
	private int weaponReload;
	private int reloadTimer;
	private int reloadMax;

	public Weapon(final float x, final float y, String type, final double direction){
		
		this.x = x;
		this.y = y;
		
		this.direction = direction;
		
		this.type = type;
		flipped = 0;
		
		switch(this.type) {
			case BlerrgGame.WEAPON_CROSSBOW: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_CROSSBOW)); break;
			case BlerrgGame.WEAPON_KNIFE: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_KNIFE)); break;
			case BlerrgGame.WEAPON_RIFLE: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_RIFLE)); 
				clipSize = 8; currClip = 8; weaponReload = 800; break;
			case BlerrgGame.WEAPON_SHOTGUN: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN)); 
				clipSize = 4; currClip = 4; weaponReload = 1600; break;
			case BlerrgGame.WEAPON_SMG: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SMG)); 
				clipSize = 24; currClip = 24; weaponReload = 1200; break;
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
			case BlerrgGame.WEAPON_RIFLE: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_RIFLE)); 
				clipSize = 8; currClip = 8; weaponReload = 40; break;
			case BlerrgGame.WEAPON_SHOTGUN: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN)); 
				clipSize = 4; currClip = 4; weaponReload = 80; break;
			case BlerrgGame.WEAPON_SMG: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SMG)); 
				clipSize = 24; currClip = 24; weaponReload = 30; break;
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
	
	public boolean isReady() {
		if (cooldown <= 0 && reloadTimer <= 0 && currClip > 0)
			return true;
		else
			return false;
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
	public void setCooldown(int c) {
		cooldown = c;
	}
	
	public int getClipSize() {
		return clipSize;
	}
	
	public void currClipDown() {
		currClip -= 1;
	}
	
	public int getCurrClip() {
		return currClip;
	}
	
	public void startReload() {
		if (reloadTimer <= 0 && currClip != clipSize) {
			ResourceManager.getSound(BlerrgGame.GUN_RELOAD_1).play(1, (float) .1);
			reloadTimer = weaponReload;
		}
	}
	
	public int getReloadTimer() {
		return reloadTimer;
	}
	
	public int getWeaponReload() {
		return weaponReload;
	}
	
	public void update(int delta) {
		if(cooldown > 0) cooldown -= delta;
		//else cooldown = 100;
		if (reloadTimer > 0) {
			reloadTimer -= delta;
			if (reloadTimer <= 0) {
				currClip = clipSize;
			}
		}
	}
}
