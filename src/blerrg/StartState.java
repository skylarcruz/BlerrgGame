package blerrg;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartState extends BasicGameState {
	
	private String state = "init";
	private boolean getClient = false;
	private boolean isConnecting = false;
	private String newPlayer = null;
	private String update = null;
	private boolean startFlag = false;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		if (state == "init") {
			g.drawString("Press S to start as server", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
			g.drawString("Press C to connect as client", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2 + 30);
		}
		if (state == "server") {
			if (getClient == false && bg.clientCount < 3)
				g.drawString("Press C to wait for new client", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
			else if (bg.clientCount < 3)
				g.drawString("Waiting for clients...", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
			else
				g.drawString("No More Players", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
			g.drawString(bg.clientCount + " Clients Connected", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2 + 30);
			g.drawString("Press Space to Start", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2 + 60);
		}
		if (state == "clientConnect") {
			g.drawString("Connecting...", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
			g.drawString("Player: " + bg.clientNum, bg.ScreenWidth/2 - 30, bg.ScreenHeight/2 + 30);
		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		BlerrgGame bg = (BlerrgGame)game;
		
		if (getClient == true) {
			try {
				newPlayer = bg.bgServer.getClient(bg.clientCount);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (newPlayer != null)
				bg.clientCount += 1;
			getClient = false;
		}
		
		if (isConnecting == true) {
			bg.isClient = true;
			try {
				bg.bgClient = new Client();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isConnecting = false;
		}
		
		if (state == "init") {
			if (input.isKeyPressed(Input.KEY_S)) {
				state = "server";
				bg.isServer = true;
				try {
					bg.bgServer = new Server();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (input.isKeyPressed(Input.KEY_C)) {
				state = "clientConnect";
				isConnecting = true;
			}
		}
		
		if (state == "server") {
			if (startFlag == true)
				bg.enterState(BlerrgGame.MENUSTATE);
			if (input.isKeyPressed(Input.KEY_C))
				getClient = true;
			if (input.isKeyPressed(Input.KEY_SPACE)) {
				startFlag = true;
			}
		}
		
		// Server Updates
		
		if (bg.isServer == true) {
			if (newPlayer == "2") {
				bg.p2Active = true;
				bg.bgServer.sendToClient("NewPlayer:2", "2");
				newPlayer = null;
			}
			else if (newPlayer == "3") {
				bg.p3Active = true;
				bg.bgServer.sendToClient("NewPlayer:3", "3");
				bg.bgServer.sendToClient("incCount", "2");
				newPlayer = null;
			}
			else if (newPlayer == "4") {
				bg.p4Active = true;
				bg.bgServer.sendToClient("NewPlayer:4", "4");
				bg.bgServer.sendToClient("incCount", "2");
				bg.bgServer.sendToClient("incCount", "3");
				newPlayer = null;
			}
			else if (startFlag == true) {
				if (bg.clientCount >= 1 && bg.p2Active)
					bg.bgServer.sendToClient("menuStart", "2");
				if (bg.clientCount >= 2 && bg.p3Active)
					bg.bgServer.sendToClient("menuStart", "3");
				if (bg.clientCount == 3 && bg.p4Active)
					bg.bgServer.sendToClient("menuStart", "4");
			}
			else {
				if (bg.clientCount >= 1 && bg.p2Active)
					bg.bgServer.sendToClient("null", "2");
				if (bg.clientCount >= 2 && bg.p3Active)
					bg.bgServer.sendToClient("null", "3");
				if (bg.clientCount == 3 && bg.p4Active)
					bg.bgServer.sendToClient("null", "4");
			}
			
		}
		
		// Client Updates
		
		if (bg.isClient == true) {
			try {
				update = bg.bgClient.getUpdates();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (update.equals("null") == false) {
				if (update.equals("NewPlayer:2")) {
					bg.p2Active = true;
					bg.clientNum = 2;
					bg.clientCount = 1;
				}
				else if (update.equals("NewPlayer:3")) {
					bg.p2Active = true; bg.p3Active = true;
					bg.clientNum = 3;
					bg.clientCount = 2;
				}
				else if (update.equals("NewPlayer:4")) {
					bg.p2Active = true; bg.p3Active = true; bg.p4Active = true;
					bg.clientNum = 4;
					bg.clientCount = 3;
				}
				else if (update.equals("incCount")) {
					bg.clientCount += 1;
					if (bg.p3Active == false) { bg.p3Active = true; }
					else if (bg.p4Active == false) { bg.p4Active = true; }
				}
				else if (update.equals("!:p2|")) { bg.p2Active = false; }
				else if (update.equals("!:p3|")) { bg.p3Active = false; }
				else if (update.equals("!:p4|")) { bg.p4Active = false; }
				else if (update.equals("menuStart"))
					bg.enterState(BlerrgGame.MENUSTATE);
			}
			update = null;
		}

//		if (input.isKeyDown(Input.KEY_SPACE)) {
//			bg.enterState(BlerrgGame.PLAYINGSTATE);
//		}
	}

	@Override
	public int getID() {
		return BlerrgGame.STARTSTATE;
	}

	

}
