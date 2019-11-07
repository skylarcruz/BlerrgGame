package blerrg;

import java.io.IOException;
//import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//import jig.ResourceManager;
import jig.Vector;

public class PlayingState extends BasicGameState {
	
	private String msg;
	private String cUpdate;
	private String in2;
	private String in3;
	private String in4;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		bg.player = new Player(bg.ScreenWidth/2, bg.ScreenHeight/2, 0, 0, 0);
		if (bg.clientCount >= 1)
			bg.player2 = new Player(bg.ScreenWidth/2 + 50, bg.ScreenHeight/2, 0, 0, 0);
		if (bg.clientCount >= 2)
			bg.player3 = new Player(bg.ScreenWidth/2, bg.ScreenHeight/2 + 50, 0, 0, 0);
		if (bg.clientCount == 3)
			bg.player4 = new Player(bg.ScreenWidth/2 + 50, bg.ScreenHeight/2 + 50, 0, 0, 0);
		bg.createMap(0);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		if (bg.isServer) {
			bg.cameraX = bg.player.getPosition().getX() - bg.ScreenWidth/2;
			bg.cameraY = bg.player.getPosition().getY() - bg.ScreenHeight/2;
			g.translate(-bg.cameraX, -bg.cameraY);
		}
		else {
			if (bg.clientNum == 2) {
				bg.cameraX = bg.player2.getPosition().getX() - bg.ScreenWidth/2;
				bg.cameraY = bg.player2.getPosition().getY() - bg.ScreenHeight/2;
				g.translate(-bg.cameraX, -bg.cameraY);
			}
			else if (bg.clientNum == 3) {
				bg.cameraX = bg.player3.getPosition().getX() - bg.ScreenWidth/2;
				bg.cameraY = bg.player3.getPosition().getY() - bg.ScreenHeight/2;
				g.translate(-bg.cameraX, -bg.cameraY);
			}
			else if (bg.clientNum == 4) {
				bg.cameraX = bg.player4.getPosition().getX() - bg.ScreenWidth/2;
				bg.cameraY = bg.player4.getPosition().getY() - bg.ScreenHeight/2;
				g.translate(-bg.cameraX, -bg.cameraY);
			}
		}
		
		//Render Entities
		for (Tile t : bg.tiles) { t.render(g); }
		bg.player.render(g);
		if (bg.clientCount >= 1)
			bg.player2.render(g);
		if (bg.clientCount >= 2)
			bg.player3.render(g);
		if (bg.clientCount == 3)
			bg.player4.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		BlerrgGame bg = (BlerrgGame)game;
		
		cUpdate = "";
		
		//START PLAYER MOVEMENT
		boolean a = input.isKeyDown(Input.KEY_A) ? true : false;
		boolean w = input.isKeyDown(Input.KEY_W) ? true : false;
		boolean d = input.isKeyDown(Input.KEY_D) ? true : false;
		boolean s = input.isKeyDown(Input.KEY_S) ? true : false;
		
		if (bg.isServer) {
			// Update Player1
			if (a) { //moving left, top-left, bottom-left
				bg.player.setStopped(false);
				if (w) {
					bg.player.setVelocity(new Vector(-0.20f, -0.20f));
					bg.player.setDirection(7);
				} else if (s) {
					bg.player.setVelocity(new Vector(-0.20f, +0.20f));
					bg.player.setDirection(5);
				} else {
					bg.player.setVelocity(new Vector(-0.25f, 0));
					bg.player.setDirection(6);
				}
			} else if (d) { //moving right, top-right, bottom-right
				bg.player.setStopped(false);
				if (w) {
					bg.player.setVelocity(new Vector(+0.20f, -0.20f));
					bg.player.setDirection(1);
				} else if (s) {
					bg.player.setVelocity(new Vector(+0.20f, +0.20f));
					bg.player.setDirection(3);
				} else {
					bg.player.setVelocity(new Vector(+0.25f, 0));
					bg.player.setDirection(2);
				}			
			} else if (w) { //moving up, top-right, top-left
				bg.player.setStopped(false);
				if (a) {
					bg.player.setVelocity(new Vector(-0.20f, -0.20f));
					bg.player.setDirection(7);
				} else if (d) {
					bg.player.setVelocity(new Vector(+0.20f, -0.20f));
					bg.player.setDirection(1);
				} else {
					bg.player.setVelocity(new Vector(0, -0.25f));
					bg.player.setDirection(0);
				}
			} else if (s) { //moving down, bottom-left, bottom-right
				bg.player.setStopped(false);
				if (a) {
					bg.player.setVelocity(new Vector(-0.20f, +0.20f));
					bg.player.setDirection(5);
				} else if (d) {
					bg.player.setVelocity(new Vector(+0.20f, +0.20f));
					bg.player.setDirection(3);
				} else {
					bg.player.setVelocity(new Vector(0, +0.25f));
					bg.player.setDirection(4);
				}
			} else {
				bg.player.setStopped(true);
				bg.player.setVelocity(new Vector(0, 0));
			}
			//END PLAYER MOVEMENT
			
			// Get Player2Updates
			if (bg.clientCount >= 1) {
				try {
					in2 = bg.bgServer.get2Updates();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String arr[] = in2.split("\\|");
				for (int i = 0; i < arr.length; i++) {
					if (arr[i].matches("(.*):(.*)")) {
						String task[] = arr[i].split(":");
						switch(task[0]) {
						    // Movement Action
							case "mov":
								if (task[1].equals("stop") == false)
									bg.player2.setStopped(false);
								switch(task[1]) {
									case "U":  bg.player2.setVelocity(new Vector(0, -0.25f));
								       		   bg.player2.setDirection(0); break;
									case "UR": bg.player2.setVelocity(new Vector(+0.20f, -0.20f));
								               bg.player2.setDirection(1); break;
									case "R":  bg.player2.setVelocity(new Vector(+0.25f, 0));
								       		   bg.player2.setDirection(2); break;	
									case "DR": bg.player2.setVelocity(new Vector(+0.20f, +0.20f));
								               bg.player2.setDirection(3); break;
									case "D":  bg.player2.setVelocity(new Vector(0, +0.25f));
								               bg.player2.setDirection(4); break;
									case "DL": bg.player2.setVelocity(new Vector(-0.20f, +0.20f));
									           bg.player2.setDirection(5); break;
									case "L":  bg.player2.setVelocity(new Vector(-0.25f, 0));
								               bg.player2.setDirection(6); break;
									case "UL": bg.player2.setVelocity(new Vector(-0.20f, -0.20f));
								               bg.player2.setDirection(7); break;
									case "stop": bg.player2.setStopped(true);
							                     bg.player2.setVelocity(new Vector(0, 0)); break;
						            default:   bg.player2.setStopped(true);
						                       bg.player2.setVelocity(new Vector(0, 0)); break;
								} break;
							default: break;
						} // end of task[0] switch
					} // end of if
				} // end of for loop
			} // end of Player2 Updates
			
			// Get Player3Updates
			if (bg.clientCount >= 2) {
				try {
					in3 = bg.bgServer.get3Updates();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String arr[] = in3.split("\\|");
				for (int i = 0; i < arr.length; i++) {
					if (arr[i].matches("(.*):(.*)")) {
						String task[] = arr[i].split(":");
						switch(task[0]) {
						    // Movement Action
							case "mov":
								if (task[1].equals("stop") == false)
									bg.player3.setStopped(false);
								switch(task[1]) {
									case "U":  bg.player3.setVelocity(new Vector(0, -0.25f));
								       		   bg.player3.setDirection(0); break;
									case "UR": bg.player3.setVelocity(new Vector(+0.20f, -0.20f));
								               bg.player3.setDirection(1); break;
									case "R":  bg.player3.setVelocity(new Vector(+0.25f, 0));
								       		   bg.player3.setDirection(2); break;	
									case "DR": bg.player3.setVelocity(new Vector(+0.20f, +0.20f));
								               bg.player3.setDirection(3); break;
									case "D":  bg.player3.setVelocity(new Vector(0, +0.25f));
								               bg.player3.setDirection(4); break;
									case "DL": bg.player3.setVelocity(new Vector(-0.20f, +0.20f));
									           bg.player3.setDirection(5); break;
									case "L":  bg.player3.setVelocity(new Vector(-0.25f, 0));
								               bg.player3.setDirection(6); break;
									case "UL": bg.player3.setVelocity(new Vector(-0.20f, -0.20f));
								               bg.player3.setDirection(7); break;
									case "stop": bg.player3.setStopped(true);
							                     bg.player3.setVelocity(new Vector(0, 0)); break;
						            default:   bg.player3.setStopped(true);
						                       bg.player3.setVelocity(new Vector(0, 0)); break;
								} break;
							default: break;
						} // end of task[0] switch
					} // end of if
				} // end of for loop
			} // End of Player 3 Updates
			
			// Get Player4Updates
			if (bg.clientCount == 3) {
				try {
					in4 = bg.bgServer.get4Updates();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String arr[] = in4.split("\\|");
				for (int i = 0; i < arr.length; i++) {
					if (arr[i].matches("(.*):(.*)")) {
						String task[] = arr[i].split(":");
						switch(task[0]) {
						    // Movement Action
							case "mov":
								if (task[1].equals("stop") == false)
									bg.player4.setStopped(false);
								switch(task[1]) {
									case "U":  bg.player4.setVelocity(new Vector(0, -0.25f));
								       		   bg.player4.setDirection(0); break;
									case "UR": bg.player4.setVelocity(new Vector(+0.20f, -0.20f));
								               bg.player4.setDirection(1); break;
									case "R":  bg.player4.setVelocity(new Vector(+0.25f, 0));
								       		   bg.player4.setDirection(2); break;	
									case "DR": bg.player4.setVelocity(new Vector(+0.20f, +0.20f));
								               bg.player4.setDirection(3); break;
									case "D":  bg.player4.setVelocity(new Vector(0, +0.25f));
								               bg.player4.setDirection(4); break;
									case "DL": bg.player4.setVelocity(new Vector(-0.20f, +0.20f));
									           bg.player4.setDirection(5); break;
									case "L":  bg.player4.setVelocity(new Vector(-0.25f, 0));
								               bg.player4.setDirection(6); break;
									case "UL": bg.player4.setVelocity(new Vector(-0.20f, -0.20f));
								               bg.player4.setDirection(7); break;
									case "stop": bg.player4.setStopped(true);
							                     bg.player4.setVelocity(new Vector(0, 0)); break;
						            default:   bg.player4.setStopped(true);
						                       bg.player4.setVelocity(new Vector(0, 0)); break;
								} break;
							default: break;
						} // end of task[0] switch
					} // end of if
				} // end of for loop
			} // End of Player 4 Updates
			
			// Get Updates for Client
			cUpdate = cUpdate + "p1X:" + String.valueOf(bg.player.getX()) + "|";
			cUpdate = cUpdate + "p1Y:" + String.valueOf(bg.player.getY()) + "|";
			if (bg.clientCount >= 1) {
				cUpdate = cUpdate + "p2X:" + String.valueOf(bg.player2.getX()) + "|";
				cUpdate = cUpdate + "p2Y:" + String.valueOf(bg.player2.getY()) + "|";
			}
			if (bg.clientCount >= 2) {
				cUpdate = cUpdate + "p3X:" + String.valueOf(bg.player3.getX()) + "|";
				cUpdate = cUpdate + "p3Y:" + String.valueOf(bg.player3.getY()) + "|";
			}
			if (bg.clientCount == 3) {
				cUpdate = cUpdate + "p4X:" + String.valueOf(bg.player4.getX()) + "|";
				cUpdate = cUpdate + "p4Y:" + String.valueOf(bg.player4.getY()) + "|";
			}
			
			if (bg.clientCount >= 1)
				bg.bgServer.sendToClient(cUpdate, "2");
			if (bg.clientCount >= 2)
				bg.bgServer.sendToClient(cUpdate, "3");
			if (bg.clientCount == 3)
				bg.bgServer.sendToClient(cUpdate, "4");
		}
		
		// Input from Clients
		
		if (bg.isClient) {
			msg = "";
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
			
			bg.bgClient.updateServer(msg);
			
			// getUpdates from Server
			try {
				cUpdate = bg.bgClient.getUpdates();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String arr[] = cUpdate.split("\\|");
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].matches("(.*):(.*)")) {
					String task[] = arr[i].split(":");
					switch(task[0]) {
						case "p1X": bg.player.setX(Float.parseFloat(task[1])); break;
						case "p1Y": bg.player.setY(Float.parseFloat(task[1])); break;
						case "p2X": bg.player2.setX(Float.parseFloat(task[1])); break;
						case "p2Y": bg.player2.setY(Float.parseFloat(task[1])); break;
						case "p3X": bg.player3.setX(Float.parseFloat(task[1])); break;
						case "p3Y": bg.player3.setY(Float.parseFloat(task[1])); break;
						case "p4X": bg.player4.setX(Float.parseFloat(task[1])); break;
						case "p4Y": bg.player4.setY(Float.parseFloat(task[1])); break;
						default: break;
					}
				}
			}
			
		}
		
		//Update entities
		bg.player.update(delta);
		if (bg.clientCount >= 1)
			bg.player2.update(delta);
		if (bg.clientCount >= 2)
			bg.player3.update(delta);
		if (bg.clientCount == 3)
			bg.player4.update(delta);
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return BlerrgGame.PLAYINGSTATE;
	}

	
}
