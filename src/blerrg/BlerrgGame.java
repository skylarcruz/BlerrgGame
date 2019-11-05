package blerrg;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;


public class BlerrgGame extends StateBasedGame {
	
	public final static int STARTSTATE = 0;
	public final static int PLAYINGSTATE = 0;
	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	public float cameraX;
	public float cameraY;
	
	public static int clientCount = 0;
	public Server bgServer;
	public boolean isServer = false;
	public Client bgClient;
	public boolean isClient = false;
	public int clientNum = 0;
	
	//PLAYER
	public final static String CHARACTER_PLACEHOLDER = "blerrg/resource/character_placeholder.png";
	
	//TILES
	public final static String TILE_1 = "blerrg/resource/tile1.png";
	
	Player player;
	Player player2;
	Player player3;
	Player player4;
	ArrayList<Tile> tiles;

	public BlerrgGame(String title, int width, int height) throws SlickException {
		super(title);
		ScreenWidth = width;
		ScreenHeight = height;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
//		tiles = new ArrayList<Tile>(2500);
	}
	
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new StartState());
		addState(new PlayingState());
		
		//Load Player Resources
		ResourceManager.loadImage(CHARACTER_PLACEHOLDER);
		
		//Load Tile Resources
		ResourceManager.loadImage(TILE_1);
	}
	
	public void createMap(int map) {
		//initialize entities
		tiles = new ArrayList<Tile>(2500);
		
		for (int row = 0; row < 50; row++) {
			for (int col = 0; col < 50; col++) {
				//Create Tiles
				Tile newTile = new Tile(row*32, col*32, 0);
				tiles.add(newTile);
			}
		}
	}

	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new BlerrgGame(":: Blerrg ::", 1280, 720));
			app.setAlwaysRender(true);
			app.setDisplayMode(1280, 720, false);
			app.setVSync(true);
			app.setShowFPS(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
