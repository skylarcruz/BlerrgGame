package blerrg;

import java.io.IOException;
import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

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
		if (BlerrgGame.clientCount >= 1)
			bg.player2 = new Player(bg.ScreenWidth/2 + 50, bg.ScreenHeight/2, 0, 0, 0);
		if (BlerrgGame.clientCount >= 2)
			bg.player3 = new Player(bg.ScreenWidth/2, bg.ScreenHeight/2 + 50, 0, 0, 0);
		if (BlerrgGame.clientCount == 3)
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
		if (BlerrgGame.clientCount >= 1)
			bg.player2.render(g);
		if (BlerrgGame.clientCount >= 2)
			bg.player3.render(g);
		if (BlerrgGame.clientCount == 3)
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
				
				if (in2.equals("stop") == false)
					bg.player2.setStopped(false);
				if (in2.equals("aw")) {
					bg.player2.setVelocity(new Vector(-0.20f, -0.20f));
					bg.player2.setDirection(7);
				} else if (in2.equals("as")) {
					bg.player2.setVelocity(new Vector(-0.20f, +0.20f));
					bg.player2.setDirection(5);
				} else if (in2.equals("a")) {
					bg.player2.setVelocity(new Vector(-0.25f, 0));
					bg.player2.setDirection(6);
				} else if (in2.equals("dw")) {
					bg.player2.setVelocity(new Vector(+0.20f, -0.20f));
					bg.player2.setDirection(1);
				} else if (in2.equals("ds")) {
					bg.player2.setVelocity(new Vector(+0.20f, +0.20f));
					bg.player2.setDirection(3);
				} else if (in2.equals("d")) {
					bg.player2.setVelocity(new Vector(+0.25f, 0));
					bg.player2.setDirection(2);			
				} else if (in2.equals("wa")) {
					bg.player2.setVelocity(new Vector(-0.20f, -0.20f));
					bg.player2.setDirection(7);
				} else if (in2.equals("wd")) {
					bg.player2.setVelocity(new Vector(+0.20f, -0.20f));
					bg.player2.setDirection(1);
				} else if (in2.equals("w")){
					bg.player2.setVelocity(new Vector(0, -0.25f));
					bg.player2.setDirection(0);
				} else if (in2.equals("sa")) {
					bg.player2.setVelocity(new Vector(-0.20f, +0.20f));
					bg.player2.setDirection(5);
				} else if (in2.equals("sd")) {
					bg.player2.setVelocity(new Vector(+0.20f, +0.20f));
					bg.player2.setDirection(3);
				} else if (in2.equals("s")) {
					bg.player2.setVelocity(new Vector(0, +0.25f));
					bg.player2.setDirection(4);
				} else if (in2.equals("stop")){
					bg.player2.setStopped(true);
					bg.player2.setVelocity(new Vector(0, 0));
				}
			}
			
			// Get Player3Updates
			if (bg.clientCount >= 2) {
				try {
					in3 = bg.bgServer.get3Updates();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (in3.equals("stop") == false)
					bg.player3.setStopped(false);
				if (in3.equals("aw")) {
					bg.player3.setVelocity(new Vector(-0.20f, -0.20f));
					bg.player3.setDirection(7);
				} else if (in3.equals("as")) {
					bg.player3.setVelocity(new Vector(-0.20f, +0.20f));
					bg.player3.setDirection(5);
				} else if (in3.equals("a")) {
					bg.player3.setVelocity(new Vector(-0.25f, 0));
					bg.player3.setDirection(6);
				} else if (in3.equals("dw")) {
					bg.player3.setVelocity(new Vector(+0.20f, -0.20f));
					bg.player3.setDirection(1);
				} else if (in3.equals("ds")) {
					bg.player3.setVelocity(new Vector(+0.20f, +0.20f));
					bg.player3.setDirection(3);
				} else if (in3.equals("d")) {
					bg.player3.setVelocity(new Vector(+0.25f, 0));
					bg.player3.setDirection(2);			
				} else if (in3.equals("wa")) {
					bg.player3.setVelocity(new Vector(-0.20f, -0.20f));
					bg.player3.setDirection(7);
				} else if (in3.equals("wd")) {
					bg.player3.setVelocity(new Vector(+0.20f, -0.20f));
					bg.player3.setDirection(1);
				} else if (in3.equals("w")){
					bg.player3.setVelocity(new Vector(0, -0.25f));
					bg.player3.setDirection(0);
				} else if (in3.equals("sa")) {
					bg.player3.setVelocity(new Vector(-0.20f, +0.20f));
					bg.player3.setDirection(5);
				} else if (in3.equals("sd")) {
					bg.player3.setVelocity(new Vector(+0.20f, +0.20f));
					bg.player3.setDirection(3);
				} else if (in3.equals("s")) {
					bg.player3.setVelocity(new Vector(0, +0.25f));
					bg.player3.setDirection(4);
				} else if (in3.equals("stop")){
					bg.player3.setStopped(true);
					bg.player3.setVelocity(new Vector(0, 0));
				}
			}
			
			// Get Player4Updates
			if (bg.clientCount == 3) {
				try {
					in4 = bg.bgServer.get4Updates();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (in4.equals("stop") == false)
					bg.player4.setStopped(false);
				if (in4.equals("aw")) {
					bg.player4.setVelocity(new Vector(-0.20f, -0.20f));
					bg.player4.setDirection(7);
				} else if (in4.equals("as")) {
					bg.player4.setVelocity(new Vector(-0.20f, +0.20f));
					bg.player4.setDirection(5);
				} else if (in4.equals("a")) {
					bg.player4.setVelocity(new Vector(-0.25f, 0));
					bg.player4.setDirection(6);
				} else if (in4.equals("dw")) {
					bg.player4.setVelocity(new Vector(+0.20f, -0.20f));
					bg.player4.setDirection(1);
				} else if (in4.equals("ds")) {
					bg.player4.setVelocity(new Vector(+0.20f, +0.20f));
					bg.player4.setDirection(3);
				} else if (in4.equals("d")) {
					bg.player4.setVelocity(new Vector(+0.25f, 0));
					bg.player4.setDirection(2);			
				} else if (in4.equals("wa")) {
					bg.player4.setVelocity(new Vector(-0.20f, -0.20f));
					bg.player4.setDirection(7);
				} else if (in4.equals("wd")) {
					bg.player4.setVelocity(new Vector(+0.20f, -0.20f));
					bg.player4.setDirection(1);
				} else if (in4.equals("w")){
					bg.player4.setVelocity(new Vector(0, -0.25f));
					bg.player4.setDirection(0);
				} else if (in4.equals("sa")) {
					bg.player4.setVelocity(new Vector(-0.20f, +0.20f));
					bg.player4.setDirection(5);
				} else if (in4.equals("sd")) {
					bg.player4.setVelocity(new Vector(+0.20f, +0.20f));
					bg.player4.setDirection(3);
				} else if (in4.equals("s")) {
					bg.player4.setVelocity(new Vector(0, +0.25f));
					bg.player4.setDirection(4);
				} else if (in4.equals("stop")){
					bg.player4.setStopped(true);
					bg.player4.setVelocity(new Vector(0, 0));
				}
			}
			
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
			if (a) { 
				if (w) { // up-left
					msg = "aw";
				} else if (s) { // down-left
					msg = "as";
				} else { // left
					msg = "a";
				}
			} else if (d) {
				if (w) { // up-right
					msg = "dw";
				} else if (s) { // down-right
					msg = "ds";
				} else { // right
					msg = "d";
				}			
			} else if (w) {
				if (a) { // up-left
					msg = "wa";
				} else if (d) { // up-right
					msg = "wd";
				} else { // up
					msg = "w";
				}
			} else if (s) {
				if (a) { // down-left
					msg = "sa";
				} else if (d) { // down-right
					msg = "sd";
				} else { // down
					msg = "s";
				}
			} else {
				msg = "stop";
			}
			
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
				if (arr[i].matches("p1X(.*)")) {
					String split2[] = arr[i].split(":");
					bg.player.setX(Float.parseFloat(split2[1]));
				}
				else if (arr[i].matches("p1Y(.*)")) {
					String split2[] = arr[i].split(":");
					bg.player.setY(Float.parseFloat(split2[1]));
				}
				else if (arr[i].matches("p2X(.*)")) {
					String split2[] = arr[i].split(":");
					bg.player2.setX(Float.parseFloat(split2[1]));
				}
				else if (arr[i].matches("p2Y(.*)")) {
					String split2[] = arr[i].split(":");
					bg.player2.setY(Float.parseFloat(split2[1]));
				}
				else if (arr[i].matches("p3X(.*)")) {
					String split2[] = arr[i].split(":");
					bg.player3.setX(Float.parseFloat(split2[1]));
				}
				else if (arr[i].matches("p3Y(.*)")) {
					String split2[] = arr[i].split(":");
					bg.player3.setY(Float.parseFloat(split2[1]));
				}
				else if (arr[i].matches("p4X(.*)")) {
					String split2[] = arr[i].split(":");
					bg.player4.setX(Float.parseFloat(split2[1]));
				}
				else if (arr[i].matches("p4Y(.*)")) {
					String split2[] = arr[i].split(":");
					bg.player4.setY(Float.parseFloat(split2[1]));
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
