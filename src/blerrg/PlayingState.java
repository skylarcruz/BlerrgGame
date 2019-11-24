package blerrg;

import java.io.IOException;
//import java.util.Arrays;
import java.util.ArrayList;

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
	private String cUpdate = "";
	private String in2;
	private String in3;
	private String in4;
	int amount = 0;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
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

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		cUpdate = "";
		

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
							case "Xp4": bg.world.player4.setX(Float.parseFloat(task[1])); break; }
						  // Get Y Positions
						  case 'Y': switch (task[0]) {
						    case "Yp1": bg.world.player.setY(Float.parseFloat(task[1])); break;
						    case "Yp2": bg.world.player2.setY(Float.parseFloat(task[1])); break;
						    case "Yp3": bg.world.player3.setY(Float.parseFloat(task[1])); break;
						    case "Yp4": bg.world.player4.setY(Float.parseFloat(task[1])); break;}
						  // Get Dir
						  case 'D': switch (task[0]) {
						    case "Dp1": bg.world.player.setDirection(Integer.parseInt(task[1])); break;
						    case "Dp2": bg.world.player2.setDirection(Integer.parseInt(task[1])); break;
						    case "Dp3": bg.world.player3.setDirection(Integer.parseInt(task[1])); break;
						    case "Dp4": bg.world.player4.setDirection(Integer.parseInt(task[1])); break;}
						  // Get Shots Fired
						  case 'F': switch (task[0]) {
						    case "Fp1": bg.world.player.shoot(Float.parseFloat(p[0]), Float.parseFloat(p[1]), 
							            bg.world.player.getX(), bg.world.player.getY()); break;
						    case "Fp2": bg.world.player.shoot(Float.parseFloat(p[0]), Float.parseFloat(p[1]), 
									    bg.world.player2.getX(), bg.world.player2.getY()); break;
						    case "Fp3": bg.world.player.shoot(Float.parseFloat(p[0]), Float.parseFloat(p[1]), 
									    bg.world.player3.getX(), bg.world.player3.getY()); break;
						    case "Fp4": bg.world.player.shoot(Float.parseFloat(p[0]), Float.parseFloat(p[1]), 
									    bg.world.player4.getX(), bg.world.player4.getY()); break;}
						  case '!': switch(task[1]) {
						    case "p2": bg.p2Active = false; bg.world.removePlayer(2); break;
							case "p3": bg.p3Active = false; bg.world.removePlayer(3); break;
							case "p4": bg.p4Active = false; bg.world.removePlayer(4); break;
						  } break;
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
		
		
		//Update the world
		bg.world.update(game, delta);
		
	}

	
	public void serverUpdate(GameContainer container, BlerrgGame bg, int delta) {
		
		cUpdate += bg.world.player.processInput(container.getInput(), bg, cUpdate);
		//END PLAYER MOVEMENT
		
	
		//Receive inputs from clients
		
		// Get Player2Updates
		if (bg.p2Active) {
			try {in2 = bg.bgServer.get2Updates();
			} catch (IOException e) {e.printStackTrace();}
			cUpdate += bg.world.player2.processClientRequest(bg, bg.world, in2, cUpdate, "2");
		}
			

		// Get Player3Updates
		if (bg.p3Active) {
			try {in3 = bg.bgServer.get3Updates();
			} catch (IOException e) {e.printStackTrace();}
			cUpdate += bg.world.player3.processClientRequest(bg, bg.world, in3, cUpdate, "3");
		}
	 
			
		// Get Player4Updates
		if (bg.p4Active) {
			try {in4 = bg.bgServer.get4Updates();
			} catch (IOException e) {e.printStackTrace();}
			cUpdate +=  bg.world.player4.processClientRequest(bg, bg.world, in4, cUpdate, "4");
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
