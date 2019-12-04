package blerrg;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import jig.Entity;
import jig.ResourceManager;
import jig.Shape;
import jig.Vector;
import worldModel.WorldModel;

public class Player extends Entity {
	
	private Vector velocity;
	private boolean isStopped;
	private int direction;
	public ArrayList<Projectile> projectiles;
	public ArrayList<Weapon> weapons;
	private String cur_weapon;
	public Vector prevPosition;
	
	public Healthbar hp;
	public int score = 0;
	public double rotation;

	public Player(final float x, final float y, final float vx, final float vy, int characterType) {
		super(x, y);
		
		switch(characterType) {
			case 0: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.CHARACTER_PLACEHOLDER)); break;
			default: addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.CHARACTER_PLACEHOLDER)); break;
		}
		
		velocity = new Vector(vx, vy);
		projectiles = new ArrayList<Projectile>(10);
		cur_weapon = "shotgun";
		weapons = new ArrayList<Weapon>();
		//weapons.add(new Weapon(x, y, "shotgun", 45));
		hp = new Healthbar(x - 50, y - 25);

	}
	public String getCurrentWeapon() {
		return cur_weapon;
	}
	
	public String processInput(Input input, StateBasedGame game) {
		
		BlerrgGame bg = (BlerrgGame)game;
		String cU = "";
				
		//System.out.println("Processing Input directly");

		float mouseX = input.getMouseX() + bg.world.cameraX;
		float mouseY = input.getMouseY() + bg.world.cameraY;
		//START PLAYER SHOOTING
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			shoot(mouseX, mouseY, getX(), getY(), this);
			cU += "Fp1:" + String.valueOf(mouseX) + "&" +
				    String.valueOf(mouseY) + "|";
		}
		
		//SET WEAPON ROTATION
		double theta = getAngle(mouseX, bg.world.thisPlayer.getX(), mouseY, bg.world.thisPlayer.getY());
		weapons.get(0).setDirection(Math.toDegrees(theta));
		cU += "W:rot&p1&" + String.valueOf(Math.toDegrees(theta)) + "|";
		
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

		float mouseX = input.getMouseX() + bg.world.cameraX;
		float mouseY = input.getMouseY() + bg.world.cameraY;
		// Get Shots Fired
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			msg += "fire:" + String.valueOf(mouseX) + "&" + 
					String.valueOf(mouseY) + "|";
		}
		
		// Get Weapon Rotation
		double theta = getAngle(mouseX, bg.world.thisPlayer.getX(), mouseY, bg.world.thisPlayer.getY());
		weapons.get(0).setDirection(Math.toDegrees(theta));
		msg += "wRot:" + String.valueOf(Math.toDegrees(theta)) + "|";
		
		if(!msg.equalsIgnoreCase("")) {
			//System.out.println("Client Input Request: "+msg);			
		}
		
		return msg;
	}
	
	//Recieves a string from a client, updates this player accordingly
	public String processClientRequest(StateBasedGame game, WorldModel w, String in, String num) {
		BlerrgGame bg = (BlerrgGame)game;
		String cU = "";
	
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
								getX(), getY(), bg.world.player); 

						cU += "Fp" + num + ":" + p[0] + "&" +
							       p[1] + "|"; break;
			        // Weapon Rotation
					case "wRot": weapons.get(0).setDirection(Double.valueOf(task[1])); 
								 cU += "W:rot&p" + num + "&" + task[1] + "|"; break;
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
		hp.setPosition(getX(), getY());
	}
	
	public double getAngle(float ax, float bx, float ay, float by) {
		double a = ay - by;
		double b = ax - bx;
		
		return Math.atan2((a), (b));
	}
	
	public void shoot(float mouseX, float mouseY, float originX, float originY, Player p) {

		double speed = 1.0;
		double angle = Math.atan2(mouseX - originX, mouseY - originY);
		float vx = (float) (speed * Math.sin(angle));
		float vy = (float) (speed * Math.cos(angle));
		
		projectiles.add(new Projectile(originX, originY, vx, vy));
		
		float dX = (p.getX() - this.getX());
		float dY = (p.getY() - this.getY());
		float d = (float) Math.sqrt((dX * dX) + (dY * dY));
		d = d/500 + 1;
		if (d < 3)  d = 1/d ; 
		else d = 0;
		
		ResourceManager.getSound(BlerrgGame.GUN_1_SND).play(1, d/4);
		
//		p.hp.setHealth(p.hp.getHealth() - 5);
	}
	
	public void hit(Player pS, Player pD) {
		pD.hp.setHealth(pD.hp.getHealth() - 30);
		System.out.println("Player: " + pD + " was hit! Current health: " + pD.hp.getHealth());
		
		if (pD.hp.getHealth() <= 0) {
			// reset player p
			System.out.println("Player: " + pD + " killed by Player: " + pS);
			pD.hp.setHealth(100);
			pS.score += 1;
		}
	}

	
	public class Projectile extends Entity {
		private Vector velocity;
		
		
		public Projectile(final float x, final float y, final float vx, final float vy) {
			super(x, y);
			addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.PROJECTILE_PLACEHOLDER).getScaledCopy(2));
			velocity = new Vector(vx, vy);
		}
		
		public void setVelocity(final Vector v) {
			velocity = v;
		}
		
		public void update(final int delta) {
			translate(velocity.scale(delta));
		}
	}
	
	// Healthbar only visible to other players
	public class Healthbar extends Entity {
		private int health;
		private Image bar;
		private Image border;
		public boolean display = true;

		public Healthbar(final float x, final float y) {
			super(x, y);
			health = 100;
			
			border = ResourceManager.getImage(BlerrgGame.HEALTHBORDER_PLACEHOLDER);
			addImage(border, new Vector(0, -25));
			
			bar = ResourceManager.getImage(BlerrgGame.HEALTH_PLACEHOLDER).getScaledCopy(health, 10);
			addImage(bar, new Vector(0 - (50 - health/2), -25));
			
		}
		
		public void setHealth(int h) {
			if (h >= 0)
				health = h;
			else
				health = 0;
			
			removeImage(bar);
			bar = ResourceManager.getImage(BlerrgGame.HEALTH_PLACEHOLDER).getScaledCopy(health, 10);
			addImage(bar, new Vector(0 - (50 - health/2), -25));
		}
		
		public int getHealth() {
			return health;
		}
	}
}
