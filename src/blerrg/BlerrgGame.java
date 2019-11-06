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
	
	public final static int STARTSTATE = 0;
	public final static int PLAYINGSTATE = 0;
	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	
	//PLAYER
	public final static String CHARACTER_PLACEHOLDER = "blerrg/resource/character_placeholder.png";
	
	//TILES
	public final static String TILE_1 = "blerrg/resource/tile1.png";

	WorldModel world;

	
	
	public BlerrgGame(String title, int width, int height) throws SlickException {
		super(title);
		ScreenWidth = width;
		ScreenHeight = height;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);

	}
	
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new StartState());
		addState(new PlayingState());
		
		//Load Player Resources
		ResourceManager.loadImage(CHARACTER_PLACEHOLDER);
		
		//Load Tile Resources
		ResourceManager.loadImage(TILE_1);
	}
	
	


	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new BlerrgGame(":: Blerrg ::", 1280, 720));
			app.setDisplayMode(1280, 720, false);
			app.setShowFPS(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	

}
