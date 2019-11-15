package worldModel;

import java.util.ArrayList;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import blerrg.BlerrgGame;
import blerrg.Player;
import blerrg.Tile;
import jig.Collision;
import jig.Entity;

public class WorldModel {

	//Handle for the tile map
	public TileMap map;
	
	QuadTree quadTree;
	
	public ArrayList<Entity> staticCollidables;
	
	public ArrayList<Entity> dynamicCollidables;
	
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
		
		quadTree = new QuadTree((int)(map.columns*Tile.TILE_BASE_SIZE),
				(int)(map.rows*Tile.TILE_BASE_SIZE));
		
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
		
		
		//Add wall tiles to the quadtree
		for(Tile t: map.getSolidTiles()) {
			//staticCollidables.add(t);
			
			quadTree.addEntity(t);
		}
		
		
		quadTree.printTree();
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
	public void update(StateBasedGame game, int delta) {
		BlerrgGame bg = (BlerrgGame)game;
		//Test for collisions
		collisionTesting(delta);
		
		//update entities(Just player for now)
		//player.update(delta);
		
		//Update entities
		player.update(delta);
		if (bg.clientCount >= 1)
			player2.update(delta);
		if (bg.clientCount >= 2)
			player3.update(delta);
		if (bg.clientCount == 3)
			player4.update(delta);
		
	}
	
	
	public void collisionTesting(int delta) {
		
		//Check all characters
		for(Entity character: characters) {
			//get nearby entities
			ArrayList<Entity> nearEnts = quadTree.nearbyEntities(character);
			
//			if(!nearEnts.isEmpty()) {
//				BlerrgGame.debugPrint("Nearby Entities: ", nearEnts.size());
//			}
			
			for(Entity ent: nearEnts) {
				Collision c = character.collides(ent);
				
				if(c != null) {
					System.out.println("Collision!");
					
					//Resolve the collision, this depends on the type of object
					
					//Details:
					System.out.println("---------");
					System.out.println("MinPen: "+c.getMinPenetration().toString());
					System.out.println("---------");
					
					//Move player back by the minimum penetration
					jig.Vector back = c.getMinPenetration().scale(1.0f);
					//character.translate(back);
					
					//for now, assume character is player
					((Player)character).setVelocity(back);
					
					//Stop checking for collisions with this character
					break;
				}
			}
		}
		
		
		//Check dynamic collidables
		
//		for(Entity statCol: staticCollidables) {
//			
//			Collision c = player.collides(statCol);
//			if(c != null) {
//				System.out.println("Collision!");
//				
//				//Details:
//				System.out.println("---------");
//				System.out.println("MinPen: "+c.getMinPenetration().toString());
//				System.out.println("---------");
//				
//				//Move player back by the minimum penetration
//				jig.Vector back = c.getMinPenetration().scale(1.0f);
//				player.translate(back);
//				player.setVelocity(back);
//			}
//		}
		
	}
	
	
	
	public void render(StateBasedGame game, Graphics g) {
		
		BlerrgGame bg = (BlerrgGame)game;
		
		//try {
			
			translateCamera(g);
			
			//map.render(g);
			
			//get tiles in the area of the camera
			ArrayList<Tile> visTiles = 
					map.tilesInArea(thisPlayer.getX() - frameWidth/2, thisPlayer.getY() - frameHeight/2,
							thisPlayer.getX()+frameWidth/2, thisPlayer.getY()+frameHeight/2);
			for(Tile t: visTiles) {
				t.render(g);
			}
			
			//player.render(g);
			
			for(Entity character: characters) {
				character.render(g);
			}
			
//		} catch (SlickException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		//Render game objects
		
		
	}


	public void translateCamera(Graphics g) {
		//Translate Camera to achieve scrolling
		cameraX = thisPlayer.getPosition().getX() - frameWidth/2;
		cameraY = thisPlayer.getPosition().getY() - frameHeight/2;
		g.translate(-cameraX, -cameraY);
	}
	
}
