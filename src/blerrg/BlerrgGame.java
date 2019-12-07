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
	public final static int ENDSTATE = 3;
	
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
	
	public int winScore = 10;
	
	//PLAYER
	public final static String CHAR1_TOP_DOWN_SHEET = "blerrg/resource/Player/char1_topdown_sheet.png";
	public final static String CHAR2_TOP_DOWN_SHEET = "blerrg/resource/Player/char2_topdown_sheet.png";
	public final static String CHAR3_TOP_DOWN_SHEET = "blerrg/resource/Player/char3_topdown_sheet.png";
	public final static String CHAR4_TOP_DOWN_SHEET = "blerrg/resource/Player/char4_topdown_sheet.png";
	public final static String CHAR5_TOP_DOWN_SHEET = "blerrg/resource/Player/char5_topdown_sheet.png";


	public final static String HEALTHBORDER_PLACEHOLDER = "blerrg/resource/Player/hpBorder.png";
	public final static String HEALTH_PLACEHOLDER = "blerrg/resource/Player/health.png";
	public final static String STAMINA_PLACEHOLDER = "blerrg/resource/Player/stamina.png";
	
	//HUD
	public final static String HUD_HP_BORDER = "blerrg/resource/HUD/HUDhpBorder.png";
	public final static String HUD_HP_BAR = "blerrg/resource/HUD/HUDhpBar.png";
	public final static String HUD_STAM_BAR = "blerrg/resource/HUD/HUDstamBar.png";
	public final static String SHOTGUN_ICON_A = "blerrg/resource/HUD/shotgunIconA.png";
	public final static String SHOTGUN_ICON_NA = "blerrg/resource/HUD/shotgunIconNA.png";
	public final static String CROSSBOW_ICON_A = "blerrg/resource/HUD/crossbowIconA.png";
	public final static String CROSSBOW_ICON_NA = "blerrg/resource/HUD/crossbowIconNA.png";
	public final static String KNIFE_ICON_A = "blerrg/resource/HUD/knifeIconA.png";
	public final static String KNIFE_ICON_NA = "blerrg/resource/HUD/knifeIconNA.png";
	public final static String RIFLE_ICON_A = "blerrg/resource/HUD/rifleIconA.png";
	public final static String RIFLE_ICON_NA = "blerrg/resource/HUD/rifleIconNA.png";
	public final static String SMG_ICON_A = "blerrg/resource/HUD/smgIconA.png";
	public final static String SMG_ICON_NA = "blerrg/resource/HUD/smgIconNA.png";
	
	
	//MENU
	public final static String CHEVRON_LEFT = "blerrg/resource/Menu/leftChevron.png";
	public final static String CHEVRON_RIGHT = "blerrg/resource/Menu/rightChevron.png";
	public final static String CHAR1_MENU = "blerrg/resource/Menu/char1_menu.png";
	public final static String CHAR2_MENU = "blerrg/resource/Menu/char1_menu.png";
	public final static String CHAR3_MENU = "blerrg/resource/Menu/char1_menu.png";
	public final static String CHAR4_MENU = "blerrg/resource/Menu/char1_menu.png";
	public final static String CHAR5_MENU = "blerrg/resource/Menu/char1_menu.png";


	
	//TILES
	public final static String TILE_1 = "blerrg/resource/tile1.png";
	
	//SFX
	public static final String GUN_1_SND = "blerrg/resource/sfx/gunshot1.wav";
	
	//WEAPONS
	public final static String PROJECTILE_PLACEHOLDER = "blerrg/resource/Weapons/pellet.png";
	public static final String WEAPON_CROSSBOW = "blerrg/resource/Weapons/Crossbow.png";
	public static final String WEAPON_CROSSBOW_R = "blerrg/resource/Weapons/Crossbow_R.png";
	
	public static final String WEAPON_KNIFE = "blerrg/resource/Weapons/Knife.png";
	public static final String WEAPON_KNIFE_R = "blerrg/resource/Weapons/Knife_R.png";
	
	public static final String WEAPON_RIFLE = "blerrg/resource/Weapons/Rifle.png";
	public static final String WEAPON_RIFLE_R = "blerrg/resource/Weapons/Rifle_R.png";
	
	public static final String WEAPON_SHOTGUN = "blerrg/resource/Weapons/Shotgun.png";
	public static final String WEAPON_SHOTGUN_R = "blerrg/resource/Weapons/Shotgun_R.png";
	
	public static final String WEAPON_SMG = "blerrg/resource/Weapons/SMG.png";
	public static final String WEAPON_SMG_R = "blerrg/resource/Weapons/SMG_R.png";

	WorldModel world;

	
	
	public BlerrgGame(String title, int width, int height) throws SlickException {
		super(title);
		ScreenWidth = width;
		ScreenHeight = height;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.CIRCLE);

	}
	
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new StartState());
		addState(new MenuState());
		addState(new PlayingState());
		addState(new EndState());
		
		//Load Player Resources
		ResourceManager.loadImage(CHAR1_TOP_DOWN_SHEET);
		ResourceManager.loadImage(CHAR2_TOP_DOWN_SHEET);
		ResourceManager.loadImage(CHAR3_TOP_DOWN_SHEET);
		ResourceManager.loadImage(CHAR4_TOP_DOWN_SHEET);
		ResourceManager.loadImage(CHAR5_TOP_DOWN_SHEET);
		

		ResourceManager.loadImage(PROJECTILE_PLACEHOLDER);
		ResourceManager.loadImage(HEALTHBORDER_PLACEHOLDER);
		ResourceManager.loadImage(HEALTH_PLACEHOLDER);
		
		//Load HUD Resources
		ResourceManager.loadImage(HUD_HP_BORDER);
		ResourceManager.loadImage(HUD_HP_BAR);
		ResourceManager.loadImage(HUD_STAM_BAR);
		ResourceManager.loadImage(SHOTGUN_ICON_A);
		ResourceManager.loadImage(SHOTGUN_ICON_NA);
		ResourceManager.loadImage(CROSSBOW_ICON_A);
		ResourceManager.loadImage(CROSSBOW_ICON_NA);
		ResourceManager.loadImage(KNIFE_ICON_A);
		ResourceManager.loadImage(KNIFE_ICON_NA);
		ResourceManager.loadImage(RIFLE_ICON_A);
		ResourceManager.loadImage(RIFLE_ICON_NA);
		ResourceManager.loadImage(SMG_ICON_A);
		ResourceManager.loadImage(SMG_ICON_NA);
		
		
		//Load Menu Resources
		ResourceManager.loadImage(CHEVRON_LEFT);
		ResourceManager.loadImage(CHEVRON_RIGHT);
		ResourceManager.loadImage(CHAR1_MENU);
		ResourceManager.loadImage(CHAR2_MENU);
		ResourceManager.loadImage(CHAR3_MENU);
		ResourceManager.loadImage(CHAR4_MENU);
		ResourceManager.loadImage(CHAR5_MENU);

		
		//Load Tile Resources
		ResourceManager.loadImage(TILE_1);
		
		//Load SFX
		ResourceManager.loadSound(GUN_1_SND);
		
		//Load Weapons
		ResourceManager.loadImage(WEAPON_CROSSBOW);
		ResourceManager.loadImage(WEAPON_CROSSBOW_R);
		ResourceManager.loadImage(WEAPON_KNIFE);
		ResourceManager.loadImage(WEAPON_KNIFE_R);
		ResourceManager.loadImage(WEAPON_RIFLE);
		ResourceManager.loadImage(WEAPON_RIFLE_R);
		ResourceManager.loadImage(WEAPON_SHOTGUN);
		ResourceManager.loadImage(WEAPON_SHOTGUN_R);
		ResourceManager.loadImage(WEAPON_SMG);
		ResourceManager.loadImage(WEAPON_SMG_R);
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
