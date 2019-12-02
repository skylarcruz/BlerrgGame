package blerrg;

import java.io.IOException;
//import java.util.Arrays;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//import jig.ResourceManager;
import jig.Vector;
import worldModel.WorldModel;

public class PlayingState extends BasicGameState {
	
	private String msg;
	private String cUpdate;
	private String in2;
	private String in3;
	private String in4;
	int amount = 0;
	
	int loadTime;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		loadTime = 100;
		cUpdate = "";
		
		bg.world = new WorldModel(bg.ScreenWidth, bg.ScreenHeight, bg);
		
		if(bg.isClient) {
			
			System.out.println("Client Entering playing state: client count: "+ bg.clientCount);
			System.out.println("/t/tclient num: "+ bg.clientNum);
		}
		
		if(bg.isServer) {
			System.out.println("Server Entering playing state: client count: "+ bg.clientCount);
			System.out.println("/t/tclient num: "+ bg.clientNum);
		}
		
		bg.world.assignPlayer(bg.clientNum);
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;

		bg.world.render(game, g);
		
		if (loadTime > 0) {
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0 + bg.world.cameraX, 0 + bg.world.cameraY, 1280, 720);
			g.setColor(new Color(255, 255, 255));
			g.drawString("Loading...", 600, 360);
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		Input input = container.getInput();
		
		if (loadTime > 0)
			loadTime -= 1;
		else {
		
		if (input.isKeyPressed(Input.KEY_P))
			container.setSoundOn(false);
		
		//cUpdate = "";
		

		//This is the server: move player 1, receive input from clients
		// and update associated player objects.
		// Return updated positions to the clients
		if (bg.isServer) {
			serverUpdate(container, bg, delta);
			//System.out.println("Server update - client count: "+bg.clientCount);
		}
		
		
		// Input from Clients
		if (bg.isClient) {
			//System.out.println("Client update  - client count: "+bg.clientCount);
			
			//Process input and send data to the server
			String msg = bg.world.thisPlayer.requestFromInput(container.getInput(), bg);
			bg.bgClient.updateServer(msg);
			
			
			// getUpdates from Server
			try {
				cUpdate = bg.bgClient.getUpdates();
				
				//System.out.println(cUpdate);
	
				String p[];
				String arr[] = cUpdate.split("\\|");
				
				for (int i = 0; i < arr.length; i++) {
					if (arr[i].matches("(.*):(.*)")) {
						String task[] = arr[i].split(":");
						p = task[1].split("&");
						switch(task[0].charAt(0)) {
						  // Get X Positions
						  case 'X': switch (task[0]) {
							case "Xp1": bg.world.player.setX(Float.parseFloat(task[1])); break;
							case "Xp2": bg.world.player2.setX(Float.parseFloat(task[1])); break;
							case "Xp3": bg.world.player3.setX(Float.parseFloat(task[1])); break;
							case "Xp4": bg.world.player4.setX(Float.parseFloat(task[1])); break;
							default: break; } break;
						  // Get Y Positions
						  case 'Y': switch (task[0]) {
						    case "Yp1": bg.world.player.setY(Float.parseFloat(task[1])); break;
						    case "Yp2": bg.world.player2.setY(Float.parseFloat(task[1])); break;
						    case "Yp3": bg.world.player3.setY(Float.parseFloat(task[1])); break;
						    case "Yp4": bg.world.player4.setY(Float.parseFloat(task[1])); break;
						    default: break; } break;
						  // Get Dir
						  case 'D': switch (task[0]) {
						    case "Dp1": bg.world.player.setDirection(Integer.parseInt(task[1])); break;
						    case "Dp2": bg.world.player2.setDirection(Integer.parseInt(task[1])); break;
						    case "Dp3": bg.world.player3.setDirection(Integer.parseInt(task[1])); break;
						    case "Dp4": bg.world.player4.setDirection(Integer.parseInt(task[1])); break;
						    default: break; } break;
						  // Get Shots Fired
						  case 'F': switch (task[0]) {
						    case "Fp1": bg.world.player.shoot(Float.parseFloat(p[0]), Float.parseFloat(p[1]), 
							            bg.world.player.getX(), bg.world.player.getY(), bg.world.thisPlayer); break;
						    case "Fp2": bg.world.player2.shoot(Float.parseFloat(p[0]), Float.parseFloat(p[1]), 
									    bg.world.player2.getX(), bg.world.player2.getY(), bg.world.thisPlayer); break;
						    case "Fp3": bg.world.player3.shoot(Float.parseFloat(p[0]), Float.parseFloat(p[1]), 
									    bg.world.player3.getX(), bg.world.player3.getY(), bg.world.thisPlayer); break;
						    case "Fp4": bg.world.player4.shoot(Float.parseFloat(p[0]), Float.parseFloat(p[1]), 
									    bg.world.player4.getX(), bg.world.player4.getY(), bg.world.thisPlayer); break;
						    default: break; } break;
					      // Player Shot
						  case 'C': switch (task[0]) {
						  	case "Cshot": Player pS = bg.world.getPlayer(p[0]); Player pD = bg.world.getPlayer(p[1]);
						  				  pS.hit(pD); break;
						  	default: break; } break;			  
						  // Server/Client Disconnects
						  case '!': switch(task[1]) {
						    case "close": container.exit(); break;
						    case "p2": bg.p2Active = false; bg.world.removePlayer(2); break;
							case "p3": bg.p3Active = false; bg.world.removePlayer(3); break;
							case "p4": bg.p4Active = false; bg.world.removePlayer(4); break;
							default: break; } break;
						default: break;
						}
					}
				}
			
			
			} catch (IOException e) {
				
				BlerrgGame.debugPrint("Client unable to get update");
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				
			}
			
			
		}
		//End Client Updating
		}
		
		cUpdate = "";
		
		//Update the world
		cUpdate += bg.world.update(game, delta);
		
	}

	
	public void serverUpdate(GameContainer container, BlerrgGame bg, int delta) {
		
		cUpdate += bg.world.player.processInput(container.getInput(), bg);
		//END PLAYER MOVEMENT
		
	
		//Receive inputs from clients
		
		// Get Player2Updates
		if (bg.p2Active) {
			try {in2 = bg.bgServer.get2Updates();
			} catch (IOException e) {e.printStackTrace();}
			cUpdate += bg.world.player2.processClientRequest(bg, bg.world, in2, "2");
		}
			

		// Get Player3Updates
		if (bg.p3Active) {
			try {in3 = bg.bgServer.get3Updates();
			} catch (IOException e) {e.printStackTrace();}
			cUpdate += bg.world.player3.processClientRequest(bg, bg.world, in3, "3");
		}
	 
			
		// Get Player4Updates
		if (bg.p4Active) {
			try {in4 = bg.bgServer.get4Updates();
			} catch (IOException e) {e.printStackTrace();}
			cUpdate +=  bg.world.player4.processClientRequest(bg, bg.world, in4, "4");
		}
			
		
			
		// Send updated positions to all clients
		cUpdate = cUpdate + "Xp1:" + String.valueOf(bg.world.player.getX()) + "|";
		cUpdate = cUpdate + "Yp1:" + String.valueOf(bg.world.player.getY()) + "|";
		if (bg.clientCount >= 1 && bg.p2Active) {
			cUpdate = cUpdate + "Xp2:" + String.valueOf(bg.world.player2.getX()) + "|";
			cUpdate = cUpdate + "Yp2:" + String.valueOf(bg.world.player2.getY()) + "|";
		}
		if (bg.clientCount >= 2 && bg.p3Active) {
			cUpdate = cUpdate + "Xp3:" + String.valueOf(bg.world.player3.getX()) + "|";
			cUpdate = cUpdate + "Yp3:" + String.valueOf(bg.world.player3.getY()) + "|";
		}
		if (bg.clientCount == 3 && bg.p4Active) {
			cUpdate = cUpdate + "Xp4:" + String.valueOf(bg.world.player4.getX()) + "|";
			cUpdate = cUpdate + "Yp4:" + String.valueOf(bg.world.player4.getY()) + "|";
		}
		
		if (bg.p2Active)
			bg.bgServer.sendToClient(cUpdate, "2");
		if (bg.p3Active)
			bg.bgServer.sendToClient(cUpdate, "3");
		if (bg.p4Active)
			bg.bgServer.sendToClient(cUpdate, "4");
		
		
	}
	

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return BlerrgGame.PLAYINGSTATE;
	}

	
}
