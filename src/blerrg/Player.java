package blerrg;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.Timer;

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
	private int cur_weapon;
	public Vector prevPosition;
	
	public HUDBar hp;
	public HUDBar stam;
	public int score = 0;
	
	public Animation walk;
	public SpriteSheet walking;


	public Player(final float x, final float y, final float vx, final float vy, int characterType) {
		super(x, y);
		
		//just to create a 32x32 bounding box. we do not render this image..
		addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.CHAR1_MENU));
		
		try {
			switch(characterType) {
				case 0: walking = new SpriteSheet(BlerrgGame.CHAR1_TOP_DOWN_SHEET, 32, 32); break;
				case 1: walking = new SpriteSheet(BlerrgGame.CHAR2_TOP_DOWN_SHEET, 32, 32); break;
				case 2: walking = new SpriteSheet(BlerrgGame.CHAR3_TOP_DOWN_SHEET, 32, 32); break;
				case 3: walking = new SpriteSheet(BlerrgGame.CHAR4_TOP_DOWN_SHEET, 32, 32); break;
				case 4: walking = new SpriteSheet(BlerrgGame.CHAR5_TOP_DOWN_SHEET, 32, 32); break;
				default: break;
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}

		
		velocity = new Vector(vx, vy);
		projectiles = new ArrayList<Projectile>(10);
		cur_weapon = 0;
		weapons = new ArrayList<Weapon>();
		//weapons.add(new Weapon(x, y, "shotgun", 45));
		hp = new HUDBar(x - 50, y - 25, 0);
		stam = new HUDBar(x - 50, y - 55, 1);
		
		walk = new Animation(walking, 150);
		walk.stop();
		isStopped = true;
		direction = 0;
	}
	
	public int getCurrentWeapon() {
		return cur_weapon;
	}
	
	public void changeWeaponUp() {
		if(cur_weapon != 0) cur_weapon--;
		else cur_weapon = (weapons.size() - 1);
	}
	public void changeWeaponDown() {
		if(cur_weapon != (weapons.size() - 1)) cur_weapon++;
		else cur_weapon = 0;
	}
	
	public String processInput(Input input, StateBasedGame game) {
		
		BlerrgGame bg = (BlerrgGame)game;
		String cU = "";
				
		//System.out.println("Processing Input directly");
		
		// ALEX'S NEW STUFF
		
//		if(input.isKeyPressed(Input.KEY_E)) {changeWeaponUp();}
//		else if(input.isKeyPressed(Input.KEY_Q)) {changeWeaponDown();}
		
		// END ALEX'S NEW STUFF
		
		float mouseX = input.getMouseX() + bg.world.cameraX;
		float mouseY = input.getMouseY() + bg.world.cameraY;
		//START PLAYER SHOOTING
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) & 
				bg.world.thisPlayer.weapons.get(getCurrentWeapon()).isReady()) {
			shoot(mouseX, mouseY, getX(), getY(), this);
			cU += "Fp1:" + String.valueOf(mouseX) + "&" +
				    String.valueOf(mouseY) + "|";
		}
		
		//SET WEAPON ROTATION
		double theta = getAngle(mouseX, bg.world.thisPlayer.getX(), mouseY, bg.world.thisPlayer.getY());
		
		weapons.get(cur_weapon).setDirection(Math.toDegrees(theta));
		this.setRotation(Math.toDegrees(theta));

		cU += "W:rot&p1&" + String.valueOf(Math.toDegrees(theta)) + "|";
		
		//START PLAYER MOVEMENT
		boolean a = input.isKeyDown(Input.KEY_A) ? true : false;
		boolean w = input.isKeyDown(Input.KEY_W) ? true : false;
		boolean d = input.isKeyDown(Input.KEY_D) ? true : false;
		boolean s = input.isKeyDown(Input.KEY_S) ? true : false;
		
		boolean q = input.isKeyPressed(Input.KEY_Q) ? true : false;
		boolean e = input.isKeyPressed(Input.KEY_E) ? true : false;
		
		boolean r = input.isKeyPressed(Input.KEY_R) ? true : false;
		
		boolean shift = input.isKeyDown(Input.KEY_LSHIFT) ? true : false;
		
		// RELOAD
		if (r) { bg.world.thisPlayer.weapons.get(getCurrentWeapon()).startReload(); }
		
		if (q) { bg.world.pHUD.shiftWeapon("Left"); changeWeaponUp();
				 cU += "W:shift&p1&Left|"; }
		if (e) { bg.world.pHUD.shiftWeapon("Right"); changeWeaponDown();
				 cU += "W:shift&p1&Right|"; }
		
		
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
		
		if (shift) {
			if (stam.getRunDelay()) {
				if (stam.getStat() > 1) {
					stam.setStat(stam.getStat() - 3);
					float x = (float) (getVelocity().getX()*1.4);
					float y = (float) (getVelocity().getY()*1.4);
					setVelocity(new Vector(x, y));
				} else {
					stam.setRunDelay();
				}
			}
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
		
		boolean q = input.isKeyPressed(Input.KEY_Q) ? true : false;
		boolean e = input.isKeyPressed(Input.KEY_E) ? true : false;
		
		boolean r = input.isKeyPressed(Input.KEY_R) ? true : false;
		
		boolean shift = input.isKeyDown(Input.KEY_LSHIFT) ? true : false;
		
		// RELOAD
		if (r) { bg.world.thisPlayer.weapons.get(getCurrentWeapon()).startReload(); }
		
		if (q) { msg += "wShift:Left|"; }
		if (e) { msg += "wShift:Right|"; }

		float mouseX = input.getMouseX() + bg.world.cameraX;
		float mouseY = input.getMouseY() + bg.world.cameraY;
		// Get Shots Fired
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) & 
				bg.world.thisPlayer.weapons.get(getCurrentWeapon()).isReady()) {
			bg.world.thisPlayer.weapons.get(getCurrentWeapon()).setCooldown(50); // prevents double fire
			msg += "fire:" + String.valueOf(mouseX) + "&" + 
					String.valueOf(mouseY) + "|";
		}
		
		// Get Weapon Rotation
		double theta = getAngle(mouseX, bg.world.thisPlayer.getX(), mouseY, bg.world.thisPlayer.getY());
		weapons.get(cur_weapon).setDirection(Math.toDegrees(theta));
		msg += "wRot:" + String.valueOf(Math.toDegrees(theta)) + "|";
		
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
		
		if (shift) {
			if (stam.getRunDelay()) {
				if (stam.getStat() > 1) {
					stam.setStat(stam.getStat() - 3);
					msg += "run:run|";
				} else {
					stam.setRunDelay();
				}
			}
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
			        // Run Action
					case "run": setVelocity(new Vector((float) (getVelocity().getX()*1.4),(float) (getVelocity().getY()*1.4))); break;
			        // Weapon Rotation
					case "wRot": weapons.get(cur_weapon).setDirection(Double.valueOf(task[1])); 
								 cU += "W:rot&p" + num + "&" + task[1] + "|"; break;
					case "wShift": switch(task[1]) {
						case "Left": changeWeaponUp(); cU += "W:shift&p" + num + "&Left|"; break;
						case "Right": changeWeaponDown(); cU += "W:shift&p" + num + "&Right|"; break;
					} break;
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
	
	public void getAnimation(boolean s) {
		if (s) {
			walk.setCurrentFrame(0);
			walk.stop();
		} else {
			walk.start();
		}
	}
	
	public void setStopped(boolean s) {
		if (s != isStopped) {
			isStopped = s;
			getAnimation(s);
		}
	}
	
	public boolean setDirection(final int d) {
		if (d != direction) {
			direction = d;
			getAnimation(false);
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
		
		stam.stamRegen();
		stam.decRunDelay();
		stam.setPosition(getX(), getY());
		
		for (int i = 0; i < weapons.size(); i ++) {
			weapons.get(i).update(delta);
		}
	}
	
	public double getAngle(float ax, float bx, float ay, float by) {
		double a = ay - by;
		double b = ax - bx;
		
		return Math.atan2((a), (b));
	}
	
	public void shoot(float mouseX, float mouseY, float originX, float originY, Player p){

		double speed = 1.0;
		double angle = Math.atan2(mouseX - originX, mouseY - originY);
		
		if(this.weapons.get(getCurrentWeapon()).getType() == BlerrgGame.WEAPON_RIFLE) {
			speed = 2.0;
			float vx = (float) (speed * Math.sin(angle));
			float vy = (float) (speed * Math.cos(angle));
			
			projectiles.add(new Projectile(originX, originY, vx, vy, speed, 0, 30));
			this.weapons.get(getCurrentWeapon()).setCooldown(500);
			this.weapons.get(getCurrentWeapon()).currClipDown();
		}
		else if(this.weapons.get(getCurrentWeapon()).getType() == BlerrgGame.WEAPON_SHOTGUN){
			float vx = (float) (speed * Math.sin(angle));
			float vy = (float) (speed * Math.cos(angle));
			
			float vxl1 = (float) (speed * Math.sin(angle - Math.PI/12));
			float vyl1 = (float) (speed * Math.cos(angle - Math.PI/12));
			float vxl2 = (float) (speed * Math.sin(angle - Math.PI/6));
			float vyl2 = (float) (speed * Math.cos(angle - Math.PI/6));

			float vxr1 = (float) (speed * Math.sin(angle + Math.PI/12));
			float vyr1 = (float) (speed * Math.cos(angle + Math.PI/12));
			float vxr2 = (float) (speed * Math.sin(angle + Math.PI/6));
			float vyr2 = (float) (speed * Math.cos(angle + Math.PI/6));
			
			projectiles.add(new Projectile(originX, originY, vx, vy, speed, 0, 20));
			projectiles.add(new Projectile(originX, originY, vxl1, vyl1, speed, 0, 20));
			projectiles.add(new Projectile(originX, originY, vxr1, vyr1, speed, 0, 20));
			projectiles.add(new Projectile(originX, originY, vxl2, vyl2, speed, 0, 20));
			projectiles.add(new Projectile(originX, originY, vxr2, vyr2, speed, 0, 20));
			this.weapons.get(getCurrentWeapon()).setCooldown(800);
			this.weapons.get(getCurrentWeapon()).currClipDown();
		}
		else if(this.weapons.get(getCurrentWeapon()).getType() == BlerrgGame.WEAPON_CROSSBOW) {
			speed = 0.5;
			float vx = (float) (speed * Math.sin(angle));
			float vy = (float) (speed * Math.cos(angle));
			
			projectiles.add(new Projectile(originX, originY, vx, vy, speed, 0, 20));
			this.weapons.get(getCurrentWeapon()).setCooldown(400);
		}
		else if(this.weapons.get(getCurrentWeapon()).getType() == BlerrgGame.WEAPON_SMG) {
			float vx = (float) (speed * Math.sin(angle));
			float vy = (float) (speed * Math.cos(angle));
			
			projectiles.add(new Projectile(originX, originY, vx, vy, speed, 0, 5));
			//projectiles.add(new Projectile(originX, originY, vx, vy, speed, 100, 10));
			//projectiles.add(new Projectile(originX, originY, vx, vy, speed, 200, 10));
			this.weapons.get(getCurrentWeapon()).setCooldown(125);
			this.weapons.get(getCurrentWeapon()).currClipDown();
		}
		
		//projectiles.add(new Projectile(originX, originY, vx, vy));
		
		
		
		float dX = (p.getX() - this.getX());
		float dY = (p.getY() - this.getY());
		float d = (float) Math.sqrt((dX * dX) + (dY * dY));
		d = d/500 + 1;
		if (d < 3)  d = 1/d ; 
		else d = 0;
		
		ResourceManager.getSound(BlerrgGame.GUN_1_SND).play(1, d/4);
		
//		p.hp.setHealth(p.hp.getHealth() - 5);
	}
	
	public void hit(Player pS, Player pD, int damage) {
		pD.hp.setStat(pD.hp.getStat() - damage);
		System.out.println("Player: " + pD + " was hit! Current health: " + pD.hp.getStat());
		
		if (pD.hp.getStat() <= 0) {
			// reset player p
			System.out.println("Player: " + pD + " killed by Player: " + pS);
			pD.hp.setStat(100);
			pS.score += 1;
		}
	}

	
	public class Projectile extends Entity {
		private Vector velocity;
		private int timer;
		private int damage;
		private double speed;
		
		public Projectile(final float x, final float y, final float vx, final float vy, double speed, int timer, int d) {
			super(x, y);
			this.timer = timer;
			this.speed = speed;
			this.damage = d;
			
			addImageWithBoundingBox(ResourceManager.getImage(BlerrgGame.PROJECTILE_PLACEHOLDER).getScaledCopy(1));
			velocity = new Vector(vx, vy);
		}
		
		public void setVelocity(final Vector v) {
			if (timer <= 0)	
				velocity = v;
		}
		
		public int getTimer() {
			return timer;
		}
		
		public double getSpeed() {
			return speed;
		}
		
		public int getDamage() {
			return damage;
		}
		
		public void update(final int delta) {
			if(timer <= 0) {
				translate(velocity.scale(delta));
			}
			else timer -= delta;
		}
	}
	
	// Healthbar only visible to other players
	public class HUDBar extends Entity {
		private int stat;
		private int statType;
		private String imagePath;
		private Image bar;
		private Image border;
		public boolean display = true;
		private int runDelay = 0;

		//statType: 0 - Health, 1 - Stamina 
		public HUDBar(final float x, final float y, int type) {
			super(x, y);
			stat = 100;
			statType = type;
			
			switch(statType) {
				case 0: imagePath = BlerrgGame.HEALTH_PLACEHOLDER; break;
				case 1: imagePath = BlerrgGame.STAMINA_PLACEHOLDER; break;
				default: break;
			}
			
			border = ResourceManager.getImage(BlerrgGame.HEALTHBORDER_PLACEHOLDER);
			addImage(border, new Vector(0, -25));
			
			bar = ResourceManager.getImage(imagePath).getScaledCopy(stat, 10);
			addImage(bar, new Vector(0 - (50 - stat/2), -25));
			
		}
		
		public void setStat(int h) {
			if (h >= 0)
				stat = h;
			else
				stat = 0;
			
			removeImage(bar);
			bar = ResourceManager.getImage(imagePath).getScaledCopy(stat, 10);
			addImage(bar, new Vector(0 - (50 - stat/2), -25));
		}
		
		public int getStat() {
			return stat;
		}
		
		public void stamRegen() {
			if (stat < 100) {
				stat += 1;
			}
		}
		
		public void setRunDelay() {
			if (runDelay == 0) {
				runDelay = 40;
			} 
		}
		
		public void decRunDelay() {
			if (runDelay > 0) {
				runDelay -= 1;
			} else {
				runDelay = 0;
			}
		}
		
		public boolean getRunDelay() {
			if (runDelay > 0) {
				return false;
			} else {
				return true;
			}
		}
	}

}
