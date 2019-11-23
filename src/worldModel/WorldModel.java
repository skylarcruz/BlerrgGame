package worldModel;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;

import blerrg.BlerrgGame;
import blerrg.Player;
import blerrg.Raycast;
import blerrg.Tile;
import jig.Collision;
import jig.Entity;
import jig.Vector;


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
		
		//Update entities		
		for(Entity character: characters) {
			Player player = (Player) character;
			player.setPrevPosition(player.getX(), player.getY());
			player.update(delta);
			for(Player.Projectile p : player.projectiles) {
				p.update(delta);
			}
		}
	}
	
	
	public void collisionTesting(int delta) {
		
		//Check all characters
		for(Entity character: characters) {
			Player player = (Player) character;
			//get nearby entities
			ArrayList<Entity> nearEnts = quadTree.nearbyEntities(player);
			
			for(Entity ent : nearEnts) {
				Collision c = player.collides(ent);
				
				if(c != null) {
					
					player.setVelocity(new Vector(0, 0));
					player.setPosition(player.prevPosition);
					
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
		
		translateCamera(g);

		// ################ BEGIN RENDERING TILES ################

		Raycast field = new Raycast(game, g, 720, thisPlayer);
		ArrayList<Point> points = field.getPoints();
		
		for(int i = 0; i < points.size(); i++) {
			if(points.get(i).getX() >= 0 && points.get(i).getY() >= 0 && points.get(i).getX() < map.tiles.length && points.get(i).getY() < map.tiles[0].length)
			map.tiles[(int)points.get(i).getX() ][(int)points.get(i).getY() ].render(g);
			
		}
		
		// ################ END RENDERING TILES ################
		for(int i = 0; i < points.size(); i++) {
			for(Entity character: characters) {
				if(points.get(i).getX()*32 <= character.getX() && character.getX() <= (points.get(i).getX()*32) + 32
					&&points.get(i).getY()*32 <= character.getY() && character.getY() <= (points.get(i).getY()*32) + 32) {
					character.render(g);
				}
				Player test = (Player) character;
				for(Player.Projectile p : test.projectiles) {
					p.render(g);
				}
			}
			
			
		}
		/*
		for(Entity character: characters) {
			//character.render(g);
			Player test = (Player) character;
			for(Player.Projectile p : test.projectiles) {
				p.render(g);
			}
		}*/
		
//		for(Player.Projectile p : thisPlayer.projectiles) {
//			p.render(g);
//		}
		
	}


	public void translateCamera(Graphics g) {
		//Translate Camera to achieve scrolling
		cameraX = thisPlayer.getPosition().getX() - frameWidth/2;
		cameraY = thisPlayer.getPosition().getY() - frameHeight/2;
		g.translate(-cameraX, -cameraY);
	}
	
	public float getCameraX() {
		return cameraX;
	}
	
	public float getCameraY() {
		return cameraY;
	}
	
}
