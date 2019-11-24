package blerrg;

import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import worldModel.WorldModel;

public class Player extends Entity {
	
	private Vector velocity;
	private boolean isStopped;
	private int direction;
	public ArrayList<Projectile> projectiles;
	public Vector prevPosition;


	public Player(final float x, final float y, final float vx, final float vy, int characterType) {
		super(x, y);
		
		switch(characterType) {
			case 0: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.CHARACTER_PLACEHOLDER)); break;
			default: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.CHARACTER_PLACEHOLDER)); break;
		}
		
		velocity = new Vector(vx, vy);
		projectiles = new ArrayList<Projectile>(10);

	}
	
	
	public String processInput(Input input, StateBasedGame game, String cU) {
		
		BlerrgGame bg = (BlerrgGame)game;
				
		//System.out.println("Processing Input directly");
		
		
		//START PLAYER SHOOTING
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			float mouseX = input.getMouseX() + bg.world.cameraX;
			float mouseY = input.getMouseY() + bg.world.cameraY;
			shoot(mouseX, mouseY, getX(), getY());
			cU += "Fp1:" + String.valueOf(mouseX) + "&" +
				    String.valueOf(mouseY) + "|";
		}
		//END PLAYER SHOOTING
		
		//START PLAYER MOVEMENT
		boolean a = input.isKeyDown(Input.KEY_A) ? true : false;
		boolean w = input.isKeyDown(Input.KEY_W) ? true : false;
		boolean d = input.isKeyDown(Input.KEY_D) ? true : false;
		boolean s = input.isKeyDown(Input.KEY_S) ? true : false;
		
		
		if (a) { //moving left, top-left, bottom-left
			setStopped(false);
			if (w) {
				setVelocity(new Vector(-0.20f, -0.20f));
				if(setDirection(7)) {
					cU += "Dp1:7|"; }
			} else if (s) {
				setVelocity(new Vector(-0.20f, +0.20f));
				if(setDirection(5)) {
					cU += "Dp1:5|"; }	
			} else {
				setVelocity(new Vector(-0.25f, 0));
				if(setDirection(6)) {
					cU += "Dp1:6|"; }
			}
		} else if (d) { //moving right, top-right, bottom-right
			setStopped(false);
			if (w) {
				setVelocity(new Vector(+0.20f, -0.20f));
				if(setDirection(1)) {
					cU += "Dp1:1|"; }
			} else if (s) {
				setVelocity(new Vector(+0.20f, +0.20f));
				if(setDirection(3)) {
					cU += "Dp1:3|"; }	
			} else {
				setVelocity(new Vector(+0.25f, 0));
				if(setDirection(2)) {
					cU += "Dp1:2|"; }
			}			
		} else if (w) { //moving up, top-right, top-left
			setStopped(false);
			if (a) {
				setVelocity(new Vector(-0.20f, -0.20f));
				if(setDirection(7)) {
					cU += "Dp1:7|"; }
			} else if (d) {
				setVelocity(new Vector(+0.20f, -0.20f));
				if(setDirection(1)) {
					cU += "Dp1:1|"; }
			} else {
				setVelocity(new Vector(0, -0.25f));
				if(setDirection(0)) {
					cU += "Dp1:0|"; }
			}
		} else if (s) { //moving down, bottom-left, bottom-right
			setStopped(false);
			if (a) {
				setVelocity(new Vector(-0.20f, +0.20f));
				if(setDirection(5)) {
					cU += "Dp1:5|"; }
			} else if (d) {
				setVelocity(new Vector(+0.20f, +0.20f));
				if(setDirection(3)) {
					cU += "Dp1:3|"; }
			} else {
				setVelocity(new Vector(0, +0.25f));
				if(setDirection(4)) {
					cU += "Dp1:4|"; }
			}
		} else {
			setStopped(true);
			setVelocity(new Vector(0, 0));
			
		}
		//END PLAYER MOVEMENT
		return cU;
	}
	
	
	//Used by clients to create a request string from the current input
	public String requestFromInput(Input input, StateBasedGame game) {
		
		BlerrgGame bg = (BlerrgGame)game;
		
		String msg = "";
		
		boolean a = input.isKeyDown(Input.KEY_A) ? true : false;
		boolean w = input.isKeyDown(Input.KEY_W) ? true : false;
		boolean d = input.isKeyDown(Input.KEY_D) ? true : false;
		boolean s = input.isKeyDown(Input.KEY_S) ? true : false;

		
		//Get Movement
		if (a) { 
			if (w) { // up-left
				msg += "mov:UL|";
			} else if (s) { // down-left
				msg += "mov:DL|";
			} else { // left
				msg += "mov:L|";
			}
		} else if (d) {
			if (w) { // up-right
				msg += "mov:UR|";
			} else if (s) { // down-right
				msg += "mov:DR|";
			} else { // right
				msg += "mov:R|";
			}			
		} else if (w) {
			if (a) { // up-left
				msg += "mov:UL|";
			} else if (d) { // up-right
				msg += "mov:UR|";
			} else { // up
				msg += "mov:U|";
			}
		} else if (s) {
			if (a) { // down-left
				msg += "mov:DL|";
			} else if (d) { // down-right
				msg += "mov:DR|";
			} else { // down
				msg += "mov:D|";
			}
		} else {
			msg += "mov:stop|";
		} // End of Movement

		// Get Shots Fired
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			float mouseX = input.getMouseX() + bg.world.cameraX;
			float mouseY = input.getMouseY() + bg.world.cameraY;
			msg += "fire:" + String.valueOf(mouseX) + "&" + 
					String.valueOf(mouseY) + "|";
		}
		
		if(!msg.equalsIgnoreCase("")) {
			//System.out.println("Client Input Request: "+msg);			
		}
		
		return msg;
	}
	
	//Recieves a string from a client, updates this player accordingly
	public String processClientRequest(StateBasedGame game, WorldModel w, String in, String cU, String num) {
		BlerrgGame bg = (BlerrgGame)game;
	
		//make sure request was not null
		if(in == null) {
			System.out.println("No client request");
			return cU;
		}
		
		if(in.contentEquals("")) {
			System.out.println("Blank client request");
			return cU;
		}
		
		//System.out.println("Recieved request: "+in);
	
		String p[];
		String arr[] = in.split("\\|");
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].matches("(.*):(.*)")) {
				String task[] = arr[i].split(":");
				switch(task[0]) {
				    // Movement Action
					case "mov":
						if (task[1].equals("stop") == false)
							setStopped(false);
						switch(task[1]) {
							case "U":  setVelocity(new Vector(0, -0.25f));
						       		   if(setDirection(0)) { 
						       			   cU += "Dp" + num + ":0|";} break;
							case "UR": setVelocity(new Vector(+0.20f, -0.20f));
									   if(setDirection(1)) {
										   cU += "Dp" + num + ":1|";} break;
							case "R":  setVelocity(new Vector(+0.25f, 0));
									   if(setDirection(2)) {
										   cU += "Dp" + num + ":2|";} break;	
							case "DR": setVelocity(new Vector(+0.20f, +0.20f));
									   if(setDirection(3)) {
										   cU += "Dp" + num + ":3|";} break;
							case "D":  setVelocity(new Vector(0, +0.25f));
									   if(setDirection(4)) {
										   cU += "Dp" + num + ":4|";} break;
							case "DL": setVelocity(new Vector(-0.20f, +0.20f));
									   if(setDirection(5)) { 
										   cU += "Dp" + num + ":5|";} break;
							case "L":  setVelocity(new Vector(-0.25f, 0));
									   if(setDirection(6)) { 
										   cU += "Dp" + num + ":6|";} break;
							case "UL": setVelocity(new Vector(-0.20f, -0.20f));
									   if(setDirection(7)) {
										   cU += "Dp" + num + ":7|";} break;
							case "stop": setStopped(true);
					                     setVelocity(new Vector(0, 0)); break;
				            default:   setStopped(true);
				                       setVelocity(new Vector(0, 0)); break;
						} break;
					// Fire Action
					case "fire":
						p = task[1].split("&");
						shoot(Float.parseFloat(p[0]), Float.parseFloat(p[1]), 
								getX(), getY()); 
						cU += "Fp" + num + ":" + p[0] + "&" +
							       p[1] + "|"; break;
			        // Disconnect
					case "!": switch(task[1]) {
						case "p2": bg.p2Active = false; cU += "!:p2|"; w.removePlayer(2); break;
						case "p3": bg.p3Active = false; cU += "!:p3|"; w.removePlayer(3); break;
						case "p4": bg.p4Active = false; cU += "!:p4|"; w.removePlayer(4); break;
					} break;
					default: break;
					
				} // end of task[0] switch
			} // end of if
		} // end of for loop
		return cU;
	}
	
	public void setStopped(boolean s) {
		if (s != isStopped) {
			isStopped = s;
		}
	}
	
	public boolean setDirection(final int d) {
		if (d != direction) {
			direction = d;
			return true;
		}
		else {
			return false;
		}
	}
	
	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}
	
	public void setPrevPosition(float xCoord, float yCoord) {
		prevPosition = new Vector(xCoord, yCoord);
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
	}
	
	public void shoot(float mouseX, float mouseY, float originX, float originY) {
		double speed = 1.0;
		double angle = Math.atan2(mouseX - originX, mouseY - originY);
		float vx = (float) (speed * Math.sin(angle));
		float vy = (float) (speed * Math.cos(angle));

		projectiles.add(new Projectile(originX, originY, vx, vy));
	}

	
	public class Projectile extends Entity {
		private Vector velocity;
		
		
		public Projectile(final float x, final float y, final float vx, final float vy) {
			super(x, y);
			addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.PROJECTILE_PLACEHOLDER));
			velocity = new Vector(vx, vy);
		}
		
		public void setVelocity(final Vector v) {
			velocity = v;
		}
		
		public void update(final int delta) {
			translate(velocity.scale(delta));
		}
	}

}
