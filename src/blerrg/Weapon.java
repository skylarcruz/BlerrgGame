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
	public boolean reversed;

	public Weapon(final float x, final float y, String type, final double direction){
		
		this.x = x;
		this.y = y;
		
		this.direction = direction;
		
		this.type = type;
		reversed = false;
		switch(this.type) {
			case "shotgun": addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN));
			default: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN));
		}
	}
	
	public Weapon(Weapon weapon) {
		this.x = weapon.x;
		this.y = weapon.y;
		this.direction = weapon.direction;
		this.type = weapon.type;
		switch(this.type) {
			case "shotgun": addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN));
			default: addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN));
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
		if(type == "shotgun") {
			if(reversed == false) {
				removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN));
				addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN_R));
				reversed = true;
			}
			else {
				removeImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN_R));
				addImage(ResourceManager.getImage(BlerrgGame.WEAPON_SHOTGUN));
				reversed = false;
			}
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
	
}
