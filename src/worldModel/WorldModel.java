package worldModel;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;

import blerrg.BlerrgGame;
import blerrg.HUD;
import blerrg.Player;
import blerrg.Player.Projectile;
import blerrg.Raycast;
import blerrg.Tile;
import blerrg.Weapon;
import jig.Collision;
import jig.Entity;
import jig.ResourceManager;
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
	
	public HUD pHUD;
	
	public WorldModel(int screenWidth, int screenHeight, BlerrgGame bg) {
		
		//simple test map for now
		map = new TileMap();
		
		quadTree = new QuadTree((int)(map.columns*Tile.TILE_BASE_SIZE),
				(int)(map.rows*Tile.TILE_BASE_SIZE));
		
		frameWidth = screenWidth;
		frameHeight = screenHeight;
		
		staticCollidables = new ArrayList<Entity>();
		specialObjects = new ArrayList<Entity>();
		dynamicCollidables = new ArrayList<Entity>();
		characters = new ArrayList<Entity>();
		
		
		//TODO: Determine starting position from map
		player = new Player(screenWidth/2, screenHeight/2, 0, 0, 0);
		characters.add(player);
		player.weapons.add(new Weapon(player.getX(), player.getY(), "shotgun", 45));
		
		if (bg.clientCount >= 1 && bg.p2Active) {
			player2 = new Player(bg.ScreenWidth/2 + 50, bg.ScreenHeight/2, 0, 0, 0);
			characters.add(player2);
			player2.weapons.add(new Weapon(player.getX(), player.getY(), "shotgun", 45));
		}
		if (bg.clientCount >= 2 && bg.p3Active) {
			player3 = new Player(bg.ScreenWidth/2, bg.ScreenHeight/2 + 50, 0, 0, 0);
			characters.add(player3);
			player3.weapons.add(new Weapon(player.getX(), player.getY(), "shotgun", 45));
		}
		if (bg.clientCount == 3 && bg.p4Active) {
			player4 = new Player(bg.ScreenWidth/2 + 50, bg.ScreenHeight/2 + 50, 0, 0, 0);
			characters.add(player4);
			player4.weapons.add(new Weapon(player.getX(), player.getY(), "shotgun", 45));
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
		pHUD = new HUD(thisPlayer);
	}
	
	public String getPlayer(Player p) {
		if (p == player) return "p1";
		else if (p == player2) return "p2";
		else if (p == player3) return "p3";
		else if (p == player4) return "p4";
		else return "error";
	}
	
	public Player getPlayer(String p) {
		if (p.equals("p1")) return player;
		else if (p.equals("p2")) return player2;
		else if (p.equals("p3")) return player3;
		else if (p.equals("p4")) return player4;
		else return player;
	}
	
	public void removePlayer(int pNum) {
		switch(pNum) {
		case 2: player2.setPosition(-1000000, -1000000); characters.remove(player2); break;
		case 3: player3.setPosition(-1000000, -1000000); characters.remove(player3); break;
		case 4: player4.setPosition(-1000000, -1000000); characters.remove(player4); break;
		}
	}
	
	//Update the game model. All updates should go through this method
	public String update(StateBasedGame game, int delta) {

		String cUp = "";
		cUp += collisionTesting(delta);

		
		//Update entities		
		for(Entity character: characters) {
			Player player = (Player) character;
			player.setPrevPosition(player.getX(), player.getY());
			player.update(delta);
			for(Player.Projectile p : player.projectiles) {
				p.update(delta);
			}
		}
		pHUD.update(thisPlayer);
		
		return cUp;
	}
	
	

	public String collisionTesting(int delta) {
		
		String cUp = "";

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
			
			for (Iterator<Projectile> itr = player.projectiles.iterator(); itr.hasNext();) {
				Player.Projectile shot = (Projectile) itr.next();
				for (Entity cTest : characters) {
					if (player != cTest) {
						if (shot.collides(cTest) != null) {
							if (getPlayer(thisPlayer) == "p1") {
								thisPlayer.hit((Player) player, (Player) cTest);
								cUp += "Cshot:" + getPlayer(player) + "&" + getPlayer((Player) cTest) + "|";
								System.out.println(cUp);
							}
							itr.remove();
							break;
						}
					}
				}
			}
		}
		
		return cUp;
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
				Player currentChar = (Player) character;
				if(points.get(i).getX()*32 <= character.getX() && character.getX() <= (points.get(i).getX()*32) + 32
					&&points.get(i).getY()*32 <= character.getY() && character.getY() <= (points.get(i).getY()*32) + 32) {
					
					// #### WEAPON RENDERING ####
					Weapon weapon = currentChar.weapons.get(0);
					// first if checks if gun pointed towards the right, next is if pointed to the left
					if((Math.abs(weapon.getDirection()) > 90) && weapon.reversed == false) weapon.directionSwap();
					else if((Math.abs(weapon.getDirection()) < 90) && weapon.reversed == true) weapon.directionSwap();
					
					// will rotate nearly the entire screen, but only the weapon is drawn
					g.rotate(currentChar.getX(), currentChar.getY(), (float) weapon.getDirection());
					// keep the weapon tracking the player
					weapon.setPosition(currentChar.getCoarseGrainedMaxX(), currentChar.getY());
					weapon.render(g);
					// rotate the screen back to normal, then render the rest, strange, but works
					g.rotate(currentChar.getX(), currentChar.getY(), 0-(float) weapon.getDirection());

					// #### CHARACTER RENDERING ####
					currentChar.render(g);
					if (currentChar != thisPlayer) {
						if (currentChar.hp.display)
							currentChar.hp.render(g);
					}
					else {
						pHUD.setScore("p1", player.score);
						if (bg.p2Active) pHUD.setScore("p2", player2.score);
						if (bg.p3Active) pHUD.setScore("p3", player3.score);
						if (bg.p4Active) pHUD.setScore("p4", player4.score);
						pHUD.renderHUD(game, g, thisPlayer);
					}
						
				}
				for(Player.Projectile p : currentChar.projectiles) {
					if(points.get(i).getX()*32 <= p.getX() && p.getX() <= (points.get(i).getX()*32) + 32
							&&points.get(i).getY()*32 <= p.getY() && p.getY() <= (points.get(i).getY()*32) + 32) {
							p.render(g);
					}
				}
			}
		}

	}
	
	public void renderThisPlayerHPTemp(Graphics g, Player t) {
		g.setColor(new Color(0, 0, 0));
		if (t.hp.getHealth() == 100)
			g.fillRect(t.getX() - 77, t.getY() + 323, 140, 20);
		else 
			g.fillRect(t.getX() - 77, t.getY() + 323, 130, 20);
		if (t.hp.getHealth() > 50)
			g.setColor(new Color(0, 255, 0));
		else
			g.setColor(new Color(255, 0, 0));
		g.drawString("Health: " + t.hp.getHealth() +  "/100", t.getX() - 75, t.getY() + 325);
		g.setColor(new Color(255, 255, 255));
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
