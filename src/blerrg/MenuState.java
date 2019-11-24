package blerrg;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

import worldModel.WorldModel;

public class MenuState extends BasicGameState {
	
	private String p1Image = BlerrgGame.CHARACTER_PLACEHOLDER;
	private String p2Image = BlerrgGame.CHARACTER_PLACEHOLDER;
	private String p3Image = BlerrgGame.CHARACTER_PLACEHOLDER;
	private String p4Image = BlerrgGame.CHARACTER_PLACEHOLDER;
	
	private boolean p2Ready = true;
	private boolean p3Ready = true;
	private boolean p4Ready = true;
	
	private int level = 1;
	private boolean gameStart = false;
	
	private String Sel = "CharSel";
	
	private String in2;
	private String in3;
	private String in4;
	private String netUpdate;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		BlerrgGame bg = (BlerrgGame)game;
		gameStart = false;
		if (bg.clientCount >= 1) { p2Ready = false; }
		if (bg.clientCount >= 2) { p3Ready = false; }
		if (bg.clientCount == 3) { p4Ready = false; }
		
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		g.drawString("BLERRG", 600, 50);
		
		g.drawImage(ResourceManager.getImage((p1Image)).getScaledCopy(5), 85, 200);
		if (bg.p2Active) {
			g.drawImage(ResourceManager.getImage((p2Image)).getScaledCopy(5), 405, 200);
			if (p2Ready) { g.drawString("Ready!", 465, 375); }}
		if (bg.p3Active) {
			g.drawImage(ResourceManager.getImage((p3Image)).getScaledCopy(5), 725, 200);
			if (p3Ready) { g.drawString("Ready!", 785, 375); }}
		if (bg.p4Active) {
			g.drawImage(ResourceManager.getImage((p4Image)).getScaledCopy(5), 1045, 200);
			if (p4Ready) { g.drawString("Ready!", 1105, 375); }}
		
		if (Sel == "CharSel") {
			if (bg.clientNum == 1) {			
				g.drawImage(ResourceManager.getImage(BlerrgGame.CHEVRON_LEFT).getScaledCopy((float) .25), 25, 250);
				g.drawImage(ResourceManager.getImage(BlerrgGame.CHEVRON_RIGHT).getScaledCopy((float) .25), 250, 250);}
			if (bg.clientNum == 2) {			
				g.drawImage(ResourceManager.getImage(BlerrgGame.CHEVRON_LEFT).getScaledCopy((float) .25), 345, 250);
				g.drawImage(ResourceManager.getImage(BlerrgGame.CHEVRON_RIGHT).getScaledCopy((float) .25), 570, 250);}
			if (bg.clientNum == 3) {			
				g.drawImage(ResourceManager.getImage(BlerrgGame.CHEVRON_LEFT).getScaledCopy((float) .25), 665, 250);
				g.drawImage(ResourceManager.getImage(BlerrgGame.CHEVRON_RIGHT).getScaledCopy((float) .25), 890, 250);}
			if (bg.clientNum == 4) {			
				g.drawImage(ResourceManager.getImage(BlerrgGame.CHEVRON_LEFT).getScaledCopy((float) .25), 985, 250);
				g.drawImage(ResourceManager.getImage(BlerrgGame.CHEVRON_RIGHT).getScaledCopy((float) .25), 1210, 250);}
		}	
		else if (Sel == "ReadySel") {
			if (bg.clientNum == 2 & !p2Ready) { g.drawString("Ready?", 465, 375); }
			if (bg.clientNum == 3 & !p3Ready) { g.drawString("Ready?", 785, 375); }
			if (bg.clientNum == 4 & !p4Ready) { g.drawString("Ready?", 1105, 375); }
		}
		
		g.drawString("Level: " + Integer.toString(level), 600, 500);
		if (Sel == "LevelSel") {g.drawString(">", 575, 500);}
		g.drawString("Start Game", 600, 550);
		if (Sel == "StartSel") {g.drawString(">", 575, 550);}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		BlerrgGame bg = (BlerrgGame)game;
		netUpdate = "";
		
		boolean a = input.isKeyPressed(Input.KEY_A) ? true : false;
		boolean w = input.isKeyPressed(Input.KEY_W) ? true : false;
		boolean d = input.isKeyPressed(Input.KEY_D) ? true : false;
		boolean s = input.isKeyPressed(Input.KEY_S) ? true : false;
		boolean spc = input.isKeyPressed(Input.KEY_SPACE) ? true : false;
		
		if (bg.isServer) {
			if (gameStart) { bg.enterState(BlerrgGame.PLAYINGSTATE); }
			
			// Get Updates From Clients
			if (bg.p2Active) {
			try {in2 = bg.bgServer.get2Updates();
			} catch (IOException e) {e.printStackTrace();}
			processClientInput(bg, in2);}
			System.out.println("Test: " + in2);
			
			if (bg.p3Active) {
			try {in3 = bg.bgServer.get3Updates();} 
			catch (IOException e) {e.printStackTrace();}
			processClientInput(bg, in3);}
			
			if (bg.p4Active) {
			try {in4 = bg.bgServer.get4Updates();} 
			catch (IOException e) {e.printStackTrace();}
			processClientInput(bg, in4);}
			// End Client Updates
			
			// Menu Navigation
			if (Sel == "CharSel") {
				if (s) {Sel = "LevelSel";}
				else if (a) {/*ChangeCharLeft*/}
				else if (d) {/*ChangeCharRight*/}
			}
			else if (Sel == "LevelSel") {
				if (w) {Sel = "CharSel";}
				else if (s) {Sel = "StartSel";}
				else if (a) {/*ChangelvlLeft*/}
				else if (d) {/*ChangelvlRight*/}
			}
			else if (Sel == "StartSel") {
				if (spc & p2Ready & p3Ready & p4Ready) {
					gameStart = true;
					netUpdate += "gameStart|";
				}
				else if (w) {Sel = "LevelSel";}
			}
			if (bg.p2Active) {bg.bgServer.sendToClient(netUpdate, "2");}
			if (bg.p3Active) {bg.bgServer.sendToClient(netUpdate, "3");}
			if (bg.p4Active) {bg.bgServer.sendToClient(netUpdate, "4");}
		}
		
		// If client
		if (bg.isClient) {
			// Get Updates From Server
			try { netUpdate = bg.bgClient.getUpdates();} 
			catch (IOException e) { e.printStackTrace();}
			
			String arr[] = netUpdate.split("\\|");
			netUpdate = "";
			for (int i = 0; i < arr.length; i++) {
				switch(arr[i]) {
					case "gameStart": { gameStart = true; } break;
					case "p2Ready": { p2Ready = true; } break;
					case "p3Ready": { p3Ready = true; } break;
					case "p4Ready": { p4Ready = true; } break;
					case "!p2": { bg.p2Active = false; } break;
					case "!p3": { bg.p3Active = false; } break;
					case "!p4": { bg.p4Active = false; } break;
					default: break;
				}
			}
			if (gameStart) { bg.enterState(BlerrgGame.PLAYINGSTATE); }
			
			if (Sel == "CharSel") {
				if (s) {Sel = "ReadySel";}
				else if (a) {/*ChangeCharLeft*/}
				else if (d) {/*ChangeCharRight*/}
			}
			else if (Sel == "ReadySel") {
				if (w){
					if (bg.clientNum == 2 & !p2Ready || bg.clientNum == 3 & !p3Ready ||
					bg.clientNum == 4 & !p4Ready) { Sel = "CharSel"; }
				}
				if (spc) {
					if (bg.clientNum == 2) { p2Ready = true; netUpdate = "p2Ready";}
					if (bg.clientNum == 3) { p3Ready = true; netUpdate = "p3Ready";}
					if (bg.clientNum == 4) { p4Ready = true; netUpdate = "p4Ready";}
				}
			}
			
			// Send Updates to Server
			bg.bgClient.updateServer(netUpdate);
		}
		
		
		clearInput(input);
	}
	
	public void clearInput(Input input) {
		input.isKeyPressed(Input.KEY_SPACE);
		input.isKeyPressed(Input.KEY_W);
		input.isKeyPressed(Input.KEY_A);
		input.isKeyPressed(Input.KEY_S);
		input.isKeyPressed(Input.KEY_D);
	}
	
	public void processClientInput(StateBasedGame game, String in) {
		BlerrgGame bg = (BlerrgGame)game;
		
		if (in.equals("p2Ready")) { p2Ready = true; netUpdate += "p2Ready|"; }
		else if (in.equals("p3Ready")) { p3Ready = true; netUpdate += "p3Ready|"; }
		else if (in.equals("p4Ready")) { p4Ready = true; netUpdate += "p4Ready|"; }
		else if (in.equals("!:p2|")) { bg.p2Active = false; p2Ready = true; netUpdate += "!p2|"; }
		else if (in.equals("!:p3|")) { bg.p3Active = false; p3Ready = true; netUpdate += "!p3|"; }
		else if (in.equals("!:p4|")) { bg.p4Active = false; p4Ready = true; netUpdate += "!p4|"; }
	}

	@Override
	public int getID() {
		return BlerrgGame.MENUSTATE;
	}

	

}
