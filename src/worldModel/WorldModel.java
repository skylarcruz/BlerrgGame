package worldModel;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class WorldModel {

	public TileMap map;
	
	public WorldModel() {
		
		//simple test map for now
		map = new TileMap();
		
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
