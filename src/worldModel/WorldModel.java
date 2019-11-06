package worldModel;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import jig.Entity;

public class WorldModel {

	//Handle for the tile map
	public TileMap map;
	
	public ArrayList<Entity> collidables;
	
	public ArrayList<Entity> specialObjects;
	
	
	public WorldModel() {
		
		//simple test map for now
		map = new TileMap();
		
		//Add wall tiles to collidables
		
	}
	
	
	public void render(Graphics g) {
		
		//Render map
		try {
			map.render(g);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//Render game objects
		
		
	}
	
}
