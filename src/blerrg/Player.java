package blerrg;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Player extends Entity {
	
	private Vector velocity;
	private boolean isStopped;
	private int direction;

	public Player(final float x, final float y, final float vx, final float vy, int characterType) {
		super(x, y);
		
		switch(characterType) {
			case 0: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.CHARACTER_PLACEHOLDER)); break;
			default: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.CHARACTER_PLACEHOLDER)); break;
		}
		
		velocity = new Vector(vx, vy);
	}
	
	public void setStopped(boolean s) {
		if (s != isStopped) {
			isStopped = s;
		}
	}
	
	public void setDirection(final int d) {
		if (d != direction) {
			direction = d;
		} 
	}
	
	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
	}
}
