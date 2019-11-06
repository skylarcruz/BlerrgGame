package worldModel;

import java.util.ArrayList;

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
	
	
	public WorldModel(int screenWidth, int screenHeight) {
		
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
		
		
		//Add wall tiles to collidables
		for(Tile t: map.getSolidTiles()) {
			staticCollidables.add(t);
		}
	}
	
	
	//Update the game model. All updates should go through this method
	public void update(int delta) {
		
		//Test for collisions
		for(Entity statCol: staticCollidables) {
			
			Collision c = player.collides(statCol);
			if(c != null) {
				System.out.println("Collision!");
				
				
			}
		}
		
		//update entities(Just player for now)
		player.update(delta);
	}
	
	
	
	public void render(Graphics g) {
		
		try {
			
			translateCamera(g);
			
			map.render(g);
			
			player.render(g);
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//Render game objects
		
		
	}


	public void translateCamera(Graphics g) {
		//Translate Camera to achieve scrolling
		cameraX = player.getPosition().getX() - frameWidth/2;
		cameraY = player.getPosition().getY() - frameHeight/2;
		g.translate(-cameraX, -cameraY);
	}
	
}
