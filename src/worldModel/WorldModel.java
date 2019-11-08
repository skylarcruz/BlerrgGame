package worldModel;

import java.util.ArrayList;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import blerrg.Player;
import blerrg.Tile;
import jig.Collision;
import jig.Entity;

public class WorldModel {

	//Handle for the tile map
	public TileMap map;
	
	public ArrayList<Entity> staticCollidables;
	
	public ArrayList<Entity> specialObjects;
	
	public ArrayList<Entity> characters;
	
	//Adapted from Jonathan's setup
	public float cameraX;
	public float cameraY;
	
	public int frameWidth;
	public int frameHeight;
	
	public Player player;
	public Player player2;
	public Player player3;
	public Player player4;
	
	public Player thisPlayer;
	
	public WorldModel(int screenWidth, int screenHeight, BlerrgGame bg) {
		
		//simple test map for now
		map = new TileMap();
		
		frameWidth = screenWidth;
		frameHeight = screenHeight;
		
		staticCollidables = new ArrayList<Entity>();
		specialObjects = new ArrayList<Entity>();
		characters = new ArrayList<Entity>();
		
		
		//TODO: Determine starting position from map
		player = new Player(screenWidth/2, screenHeight/2, 0, 0, 0);
		characters.add(player);
		
		if (bg.clientCount >= 1) {
			player2 = new Player(bg.ScreenWidth/2 + 50, bg.ScreenHeight/2, 0, 0, 0);
			characters.add(player2);
		}
		if (bg.clientCount >= 2) {
			player3 = new Player(bg.ScreenWidth/2, bg.ScreenHeight/2 + 50, 0, 0, 0);
			characters.add(player3);
		}
		if (bg.clientCount == 3) {
			player4 = new Player(bg.ScreenWidth/2 + 50, bg.ScreenHeight/2 + 50, 0, 0, 0);
			characters.add(player4);
		}
		
		
		//Add wall tiles to collidables
		for(Tile t: map.getSolidTiles()) {
			staticCollidables.add(t);
		}
	}
	
	
	public void assignPlayer(int clientNum) {
		switch(clientNum) {
		case 1: thisPlayer = player; break;
		case 2: thisPlayer = player2; break;
		case 3: thisPlayer = player3; break;
		case 4: thisPlayer = player4; break;
		default: thisPlayer = player; break;
		}
	}
	
	//Update the game model. All updates should go through this method
	public void update(int delta) {
		
		//Test for collisions
		collisionTesting(delta);
		
		//update entities(Just player for now)
		player.update(delta);
	}
	
	
	public void collisionTesting(int delta) {
		
		for(Entity statCol: staticCollidables) {
			
			Collision c = player.collides(statCol);
			if(c != null) {
				System.out.println("Collision!");
				
				//Details:
				System.out.println("---------");
				System.out.println("MinPen: "+c.getMinPenetration().toString());
				System.out.println("---------");
				
				//Move player back by the minimum penetration
				jig.Vector back = c.getMinPenetration().scale(1.0f);
				player.translate(back);
				player.setVelocity(back);
			}
		}
		
	}
	
	
	
	public void render(StateBasedGame game, Graphics g) {
		
		BlerrgGame bg = (BlerrgGame)game;
		
		try {
			
			translateCamera(g);
			
			map.render(g);
			
			//player.render(g);
			
			for(Entity character: characters) {
				character.render(g);
			}
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//Render game objects
		
		
	}


	public void translateCamera(Graphics g) {
		//Translate Camera to achieve scrolling
		cameraX = thisPlayer.getPosition().getX() - frameWidth/2;
		cameraY = thisPlayer.getPosition().getY() - frameHeight/2;
		g.translate(-cameraX, -cameraY);
	}
	
}
