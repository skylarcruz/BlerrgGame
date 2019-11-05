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
	
	private int iTest = 0;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		//g.drawString("Press Space to start", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
		
		if (state == "init") {
			g.drawString("Press S to start as server", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
			g.drawString("Press C to connect as client", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2 + 30);
		}
		if (state == "server") {
			if (getClient == false && BlerrgGame.clientCount < 3)
				g.drawString("Press C to wait for new client", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
			else if (BlerrgGame.clientCount < 3)
				g.drawString("Waiting for clients...", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
			else
				g.drawString("No More Players", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
			g.drawString(BlerrgGame.clientCount + " Clients Connected", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2 + 30);
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
				newPlayer = bg.bgServer.getClient();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
//				bg.isClient = true;
//				try {
//					bg.bgClient = new Client();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		}
		
		if (state == "server") {
			if (startFlag == true)
				bg.enterState(BlerrgGame.PLAYINGSTATE);
			if (input.isKeyPressed(Input.KEY_C))
				getClient = true;
			if (input.isKeyPressed(Input.KEY_SPACE)) {
				startFlag = true;
			}
		}
		
		// Server Updates
		
		if (bg.isServer == true) {
			if (newPlayer == "2") {
				bg.bgServer.sendToClient("NewPlayer:2", "2");
				newPlayer = null;
			}
			else if (newPlayer == "3") {
				bg.bgServer.sendToClient("NewPlayer:3", "3");
				newPlayer = null;
			}
			else if (newPlayer == "4") {
				bg.bgServer.sendToClient("NewPlayer:4", "4");
				newPlayer = null;
			}
			else if (startFlag == true) {
				if (bg.clientCount >= 1)
					bg.bgServer.sendToClient("gameStart", "2");
				if (bg.clientCount >= 2)
					bg.bgServer.sendToClient("gameStart", "3");
				if (bg.clientCount == 3)
					bg.bgServer.sendToClient("gameStart", "4");
			}
			else {
				if (bg.clientCount >= 1)
					bg.bgServer.sendToClient("null", "2");
				if (bg.clientCount >= 2)
					bg.bgServer.sendToClient("null", "3");
				if (bg.clientCount == 3)
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
			
			if (iTest < 5) {
				System.out.println(update.length());
				iTest += 1;
			}
		
			
			if (update.equals("null") == false) {
				if (update.equals("NewPlayer:2")) {
					bg.clientNum = 2;
				}
				else if (update.equals("NewPlayer:3")) {
					bg.clientNum = 3;
				}
				else if (update.equals("NewPlayer:4")) {
					bg.clientNum = 4;
				}
				else if (update.equals("gameStart"))
					bg.enterState(BlerrgGame.PLAYINGSTATE);
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
