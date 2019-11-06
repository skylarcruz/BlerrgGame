package blerrg;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

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
	
	
	public void processInput(Input input) {
		
		
		//START PLAYER MOVEMENT
		boolean a = input.isKeyDown(Input.KEY_A) ? true : false;
		boolean w = input.isKeyDown(Input.KEY_W) ? true : false;
		boolean d = input.isKeyDown(Input.KEY_D) ? true : false;
		boolean s = input.isKeyDown(Input.KEY_S) ? true : false;
		
		
		if (a) { //moving left, top-left, bottom-left
			setStopped(false);
			if (w) {
				setVelocity(new Vector(-0.20f, -0.20f));
				setDirection(7);
			} else if (s) {
				setVelocity(new Vector(-0.20f, +0.20f));
				setDirection(5);
			} else {
				setVelocity(new Vector(-0.25f, 0));
				setDirection(6);
			}
		} else if (d) { //moving right, top-right, bottom-right
			setStopped(false);
			if (w) {
				setVelocity(new Vector(+0.20f, -0.20f));
				setDirection(1);
			} else if (s) {
				setVelocity(new Vector(+0.20f, +0.20f));
				setDirection(3);
			} else {
				setVelocity(new Vector(+0.25f, 0));
				setDirection(2);
			}			
		} else if (w) { //moving up, top-right, top-left
			setStopped(false);
			if (a) {
				setVelocity(new Vector(-0.20f, -0.20f));
				setDirection(7);
			} else if (d) {
				setVelocity(new Vector(+0.20f, -0.20f));
				setDirection(1);
			} else {
				setVelocity(new Vector(0, -0.25f));
				setDirection(0);
			}
		} else if (s) { //moving down, bottom-left, bottom-right
			setStopped(false);
			if (a) {
				setVelocity(new Vector(-0.20f, +0.20f));
				setDirection(5);
			} else if (d) {
				setVelocity(new Vector(+0.20f, +0.20f));
				setDirection(3);
			} else {
				setVelocity(new Vector(0, +0.25f));
				setDirection(4);
			}
		} else {
			setStopped(true);
			setVelocity(new Vector(0, 0));
		}
		//END PLAYER MOVEMENT
		
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
