package blerrg;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import worldModel.WorldModel;



public class BlerrgGame extends StateBasedGame {
	
	public final static boolean DEBUG = true;
	
	public final static int STARTSTATE = 0;
	public final static int MENUSTATE = 1;
	public final static int PLAYINGSTATE = 2;
	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	
	public int clientCount = 0;
	Server bgServer;
	boolean isServer = false;
	Client bgClient;
	boolean isClient = false;
	int clientNum = 1;
	
	public boolean p2Active = false;
	public boolean p3Active = false;
	public boolean p4Active = false;
	
	//PLAYER
	public final static String CHARACTER_PLACEHOLDER = "blerrg/resource/character_placeholder.png";
	public final static String PROJECTILE_PLACEHOLDER = "blerrg/resource/projectile_placeholder.png";
	public final static String HEALTHBORDER_PLACEHOLDER = "blerrg/resource/Player/hpBorder.png";
	public final static String HEALTH_PLACEHOLDER = "blerrg/resource/Player/health.png";
	
	//MENU
	public final static String CHEVRON_LEFT = "blerrg/resource/Menu/leftChevron.png";
	public final static String CHEVRON_RIGHT = "blerrg/resource/Menu/rightChevron.png";
	
	//TILES
	public final static String TILE_1 = "blerrg/resource/tile1.png";
	
	//SFX
	public static final String GUN_1_SND = "blerrg/resource/sfx/gunshot1.wav";

	WorldModel world;

	
	
	public BlerrgGame(String title, int width, int height) throws SlickException {
		super(title);
		ScreenWidth = width;
		ScreenHeight = height;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);

	}
	
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new StartState());
		addState(new MenuState());
		addState(new PlayingState());
		
		//Load SFX
		ResourceManager.loadSound(GUN_1_SND);
		
		//Load Player Resources
		ResourceManager.loadImage(CHARACTER_PLACEHOLDER);
		ResourceManager.loadImage(PROJECTILE_PLACEHOLDER);
		ResourceManager.loadImage(HEALTHBORDER_PLACEHOLDER);
		ResourceManager.loadImage(HEALTH_PLACEHOLDER);
		
		//Load Menu Resources
		ResourceManager.loadImage(CHEVRON_LEFT);
		ResourceManager.loadImage(CHEVRON_RIGHT);
		
		//Load Tile Resources
		ResourceManager.loadImage(TILE_1);
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
	
	public int getClientCount() {
		return clientCount;
	}
	
	
	public static void debugPrint(Object... args) {
		if(DEBUG) {
			
			StringBuilder sb = new StringBuilder();
			
			for(Object arg : args) {
				//get string of object
				sb.append(String.valueOf(arg));
			}
		
			System.out.println(sb.toString());
		}
		
		
	}
}
