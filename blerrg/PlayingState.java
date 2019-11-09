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
import worldModel.WorldModel;

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
		
		bg.world = new WorldModel(bg.ScreenWidth, bg.ScreenHeight, bg);
		
		bg.world.assignPlayer(bg.clientCount+1);
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		bg.world.render(game, g);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		

		//Process input
		bg.world.player.processInput(container.getInput());

		//Update the world
		bg.world.update(delta);
		cUpdate = "";
		

		//This is the server: move player 1, receive input from clients
		// and update associated player objects.
		// Return updated positions to the clients
		if (bg.isServer) {
			serverUpdate(container, bg, delta);
		}
		
		
		// Input from Clients
		if (bg.isClient) {

			//Process input and send data to the server
			String msg = bg.world.thisPlayer.requestFromInput(container.getInput());
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
						case "p1X": bg.world.player.setX(Float.parseFloat(task[1])); break;
						case "p1Y": bg.world.player.setY(Float.parseFloat(task[1])); break;
						
						case "p2X": bg.world.player2.setX(Float.parseFloat(task[1])); break;
						case "p2Y": bg.world.player2.setY(Float.parseFloat(task[1])); break;
						
						case "p3X": bg.world.player3.setX(Float.parseFloat(task[1])); break;
						case "p3Y": bg.world.player3.setY(Float.parseFloat(task[1])); break;
						
						case "p4X": bg.world.player4.setX(Float.parseFloat(task[1])); break;
						case "p4Y": bg.world.player4.setY(Float.parseFloat(task[1])); break;
						default: break;
					}
				}
			}
			
		}
		//End Client Updating
		
		//Update entities
		bg.world.player.update(delta);
		if (bg.clientCount >= 1)
			bg.world.player2.update(delta);
		if (bg.clientCount >= 2)
			bg.world.player3.update(delta);
		if (bg.clientCount == 3)
			bg.world.player4.update(delta);
	}

	
	public void serverUpdate(GameContainer container, BlerrgGame bg, int delta) {
		
		bg.world.player.processInput(container.getInput());
		//END PLAYER MOVEMENT
		
		
		// Get Player2Updates
		if (bg.clientCount >= 1) {
			try {
				in2 = bg.bgServer.get2Updates();
				bg.world.player2.processClientRequest(in2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		// Get Player3Updates
		if (bg.clientCount >= 2) {
			try {
				in3 = bg.bgServer.get3Updates();
				bg.world.player3.processClientRequest(in3);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
		
		// Get Player4Updates
		if (bg.clientCount == 3) {
			try {
				in4 = bg.bgServer.get4Updates();
				bg.world.player4.processClientRequest(in4);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// Get Updates for Client
		cUpdate = cUpdate + "p1X:" + String.valueOf(bg.world.player.getX()) + "|";
		cUpdate = cUpdate + "p1Y:" + String.valueOf(bg.world.player.getY()) + "|";
		if (bg.clientCount >= 1) {
			cUpdate = cUpdate + "p2X:" + String.valueOf(bg.world.player2.getX()) + "|";
			cUpdate = cUpdate + "p2Y:" + String.valueOf(bg.world.player2.getY()) + "|";
		}
		if (bg.clientCount >= 2) {
			cUpdate = cUpdate + "p3X:" + String.valueOf(bg.world.player3.getX()) + "|";
			cUpdate = cUpdate + "p3Y:" + String.valueOf(bg.world.player3.getY()) + "|";
		}
		if (bg.clientCount == 3) {
			cUpdate = cUpdate + "p4X:" + String.valueOf(bg.world.player4.getX()) + "|";
			cUpdate = cUpdate + "p4Y:" + String.valueOf(bg.world.player4.getY()) + "|";
		}
		
		if (bg.clientCount >= 1)
			bg.bgServer.sendToClient(cUpdate, "2");
		if (bg.clientCount >= 2)
			bg.bgServer.sendToClient(cUpdate, "3");
		if (bg.clientCount == 3)
			bg.bgServer.sendToClient(cUpdate, "4");
		
		}
		
		}
		
		}
		
	}
	

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return BlerrgGame.PLAYINGSTATE;
	}

	
}
