package blerrg;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;

import blerrg.Tile.TileType;

public class Raycast {

	BlerrgGame bg;
	public ArrayList<Point> points;
	
	float px;
	float py;
	
	float bordertop;
	float borderbottom;
	float borderleft;
	float borderright;

	Point[][] gridpoints;
	
	int minx , maxx , miny , maxy; //useful for slightly faster bounds checking
	int angles; // ######## NUMBER OF RAYS EQUAL TO TWICE THIS INT ########
	
	/*
	 * This class is designed to create a list of points in the arraylist points of 
	 * which we can cycle through to render the right tiles within los
	 */
	
	
	
	public Raycast(StateBasedGame game, Graphics g, int half_number_of_rays, Player p) {
		bg = (BlerrgGame)game;
		
		this.px = p.getX();
		this.py = p.getY();
		
		
		this.bordertop = (py-bg.ScreenHeight/2)/32;
		this.borderbottom = (py+bg.ScreenHeight/2)/32;
		this.borderleft = (px-bg.ScreenWidth/2)/32;
		this.borderright = (px+bg.ScreenWidth/2)/32;
		
		this.gridpoints = new Point[bg.world.map.tiles.length][bg.world.map.tiles[0].length];
		
		this.minx = (int) px/32;
		this.maxx = (int) px/32;
		this.miny = (int) py/32;
		this.maxy = (int) py/32; //useful for slightly faster bounds checking


		points = new ArrayList<Point>();
		
		this.angles = half_number_of_rays; // ######## NUMBER OF RAYS EQUAL TO TWICE THIS INT ########
		firstpass();
		secondpass();
		thirdpass();
		
		
	}
	
	/*
	 * Here in firstpass, we cast out a number of rays equal to twice the value specified in "half_number_of_rays"
	 * Any and all end points (walls, screenedge, world edge, etc.) are appended to the points arraylist
	 */

	// ################ BEGIN FIRST PASS ################

	private void firstpass() {
		
		for(int trig = -angles; trig < angles; trig++) {

			float i = px;
			float j = py;
			
			while(i >= 0 && j >= 0 && i < bg.world.map.tiles.length*32 && j < bg.world.map.tiles[0].length*32) {
				Point p = new Point((int) Math.round(i/32), (int) Math.round((j)/32));
				
				if(gridpoints[(int)p.getX()][(int)p.getY()] == null) {

							if(bg.world.map.tiles[(int) p.getX()][(int) p.getY()].type == TileType.WALL 
									||((p.getX() < borderleft) || (borderright < p.getX()) || (p.getY() < bordertop) || (borderbottom < p.getY()) )
									||((p.getX() <= 0) || (bg.ScreenWidth <= p.getX()) || (p.getY() <= 0) || (bg.ScreenHeight <= p.getY()) )) {
								
								// we don't need floor tiles, we need the shape outline
								// we add the point obviously, but for fast checking we set the gridpoints array as a poor mans hash map
								points.add(p);
								gridpoints[(int)p.getX()][(int)p.getY()] = p;
								
								
								// kinda like the coarseGrained stuff, this sets the bounds of the raycast resultant shape
								if(p.getX() > maxx) maxx = (int) p.getX();
								else if(p.getX() < minx) minx = (int) p.getX();
								if(p.getY() > maxy) maxy = (int) p.getY();
								else if(p.getY() < miny) miny = (int) p.getY();
								
								break;
								}
							
							if(p.getY() +1 < bg.world.map.tiles[0].length && p.getX() +1 < bg.world.map.tiles.length && p.getX() - 1 >= 0 && p.getY() - 1>= 0) {
								if(p.getX() < Math.round(px/32)) { // left of player
									if(p.getY() < Math.round(py/32)) { // point above player
										// O X
										// X  
										if((bg.world.map.tiles[(int) p.getX() +1][(int) p.getY() -0].type == TileType.WALL /*!= TileType.FLOOR*/ || bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() +1].type == TileType.WALL /*!= TileType.FLOOR*/) && bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -0].type == TileType.FLOOR) {
											
											break;
										}
										if((bg.world.map.tiles[(int) p.getX() +1][(int) p.getY() -0].type == TileType.WALL /*!= TileType.FLOOR*/ || bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() +1].type == TileType.WALL /*!= TileType.FLOOR*/) && bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -0].type == TileType.WALL) {

											points.add(new Point(p.getX()-0, p.getY()-0));
											gridpoints[(int)p.getX()][(int)p.getY()] = p;
											break;
										}
									}
									else if(p.getY() > Math.round(py/32)) { // point below player
										// X 
										// O X
										if((bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -1].type == TileType.WALL /*!= TileType.FLOOR*/ || bg.world.map.tiles[(int) p.getX() +1][(int) p.getY() +0].type == TileType.WALL /*!= TileType.FLOOR*/) && bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -0].type == TileType.FLOOR) {
											
											break;
										}
										else if((bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -1].type == TileType.WALL /*!= TileType.FLOOR*/ || bg.world.map.tiles[(int) p.getX() +1][(int) p.getY() +0].type == TileType.WALL /*!= TileType.FLOOR*/) && bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -0].type == TileType.WALL) {

											points.add(new Point(p.getX()-0, p.getY()-0));
											gridpoints[(int)p.getX()][(int)p.getY()] = p;
											break;
										}
									}
									
								}
								else if(p.getX() > Math.round(px/32)) { // right of player
									if(p.getY() < Math.round(py/32)) { // point above player
										// X O
										//   X
										if((bg.world.map.tiles[(int) p.getX() -1][(int) p.getY() -0].type == TileType.WALL /*!= TileType.FLOOR*/ || bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() +1].type == TileType.WALL /*!= TileType.FLOOR*/) && bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -0].type == TileType.FLOOR) {

											break;
										}
										else if((bg.world.map.tiles[(int) p.getX() -1][(int) p.getY() -0].type == TileType.WALL /*!= TileType.FLOOR*/ || bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() +1].type == TileType.WALL /*!= TileType.FLOOR*/) && bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -0].type == TileType.WALL) {

											points.add(new Point(p.getX()-0, p.getY()-0));
											gridpoints[(int)p.getX()][(int)p.getY()] = p;
											break;
										}
									}
									else if(p.getY() > Math.round(py/32)) { // point below player
										//   X
										// X O
										if((bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -1].type == TileType.WALL /*!= TileType.FLOOR*/ || bg.world.map.tiles[(int) p.getX() -1][(int) p.getY() -0].type == TileType.WALL /*!= TileType.FLOOR*/) && bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -0].type == TileType.FLOOR) {
											
											break;
										}
										else if((bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -1].type == TileType.WALL /*!= TileType.FLOOR*/ || bg.world.map.tiles[(int) p.getX() -1][(int) p.getY() -0].type == TileType.WALL /*!= TileType.FLOOR*/) && bg.world.map.tiles[(int) p.getX() -0][(int) p.getY() -0].type == TileType.WALL) {
											
											points.add(new Point(p.getX()-0, p.getY()-0));
											gridpoints[(int)p.getX()][(int)p.getY()] = p;
											break;
										}
										
									}
								}
							}
				}
				else break;
				
				
				i += 32*Math.cos(Math.PI*trig/angles);
				j += 32*Math.sin(Math.PI*trig/angles);
			}
		}
	}
	// ################ END FIRST PASS ################
	

	// ################ BEGIN SECOND PASS################
	
	private void secondpass() {
		
		ArrayList<Point> extrapoints = new ArrayList<Point>();
		
		for(int i = 0; i < points.size(); i ++) {
			int bx = (int) points.get(i).getX();
			int by = (int) points.get(i).getY();
			
			double x = bx;
			double y = by;
			
			int ex = 0;
			int ey = 0;
			
			if(i != points.size() -1) {
			ex = (int) points.get(i+1).getX();
			ey = (int) points.get(i+1).getY();
				
			}
			else {
				ex = (int) points.get(0).getX();
				ey = (int) points.get(0).getY();
				
			}
			
			double m = Math.sqrt(Math.pow(ex - x, 2) + Math.pow(ey - y, 2));
			double theta;

			if(ex - bx == 0) {
				if(ey > by) {theta = Math.PI/2;}
				else {theta = 3*Math.PI/2;}
			}
			else if(ey - by == 0) {
				if(ex > bx) {theta = 0;}
				else {theta = Math.PI;}
			}
			else {
					double a = Math.abs(ey - by);
					double b = Math.abs(ex - bx);
					theta = Math.atan((a)/(b));
					
					if(ex - bx < 0 && ey - by < 0) theta = -theta + Math.PI; // quad 2
					else if(ex - bx > 0 && ey - by > 0) theta = -theta + 2*Math.PI; // quad 4
					else if(ex - bx < 0 && ey - by > 0) theta += Math.PI; // quad 3
					
					//System.out.println("e " + Math.atan((a)/(b)) + " " + a + " " + b + " " + a/b );
				}
			
			while(m > 1) {
				if(m <= 1) break;

				if(x != ex) {
					if(theta == Math.PI) x--;
					else if(theta == 0) x++;
					else if(Math.PI/2 < theta && theta < 3*Math.PI/2) x += Math.cos(theta);
					else if(Math.PI/2 > theta || theta > 3*Math.PI/2) x += Math.cos(theta);
				}
				
				if(y != ey) {
					if(theta == Math.PI/2) y++;
					else if(theta == 3*Math.PI/2)y--; 
					else if(0 < theta && theta < Math.PI) y -= Math.sin(theta);
					else if(Math.PI < theta && theta < 2*Math.PI) y -= Math.sin(theta);
				}
				Point p = new Point(Math.round(x), Math.round(y));
				if(gridpoints[(int)p.getX()][(int)p.getY()] == null) {gridpoints[(int)p.getX()][(int)p.getY()] = p; extrapoints.add(p);}
				
				//m = Math.sqrt(Math.pow(Math.abs(ex) - Math.abs(x), 2) + Math.pow(Math.abs(ey) - Math.abs(y), 2));
				m = ((Math.abs(ex) - Math.abs(x))*(Math.abs(ex) - Math.abs(x))) + ( (Math.abs(ey) - Math.abs(y))*(Math.abs(ey) - Math.abs(y)) );
			}
		}
		points.addAll(extrapoints);
		
	}
	// ################ END SECOND PASS ################
	
	
	// ################ BEGIN THIRD PASS ################
	
	private void thirdpass() {


		ArrayList<Point> extrapoints = new ArrayList<Point>(points);

		for(int i = 0; i < points.size(); i++) {
			int x = (int) points.get(i).getX();
			int y = (int) points.get(i).getY();

			if(x+1 < gridpoints.length) {
				if(gridpoints[x+1][y] != null) continue;
				else x++;
			}
			
			int nextx = x;
			int nexty = y;

			boolean left = false, right = false, up = false, down = false;
			
			
			while(minx <= nextx && nextx <= maxx && miny <= nexty && nexty <= maxy) {

				if(!right) nextx++;
				else if(!left)nextx--;
				else if(!down) nexty++;
				else if(!up) nexty--;
				else if(right && left && down && up) {
					right = false; left = false; down = false; up = false;
					x++; nextx = x; nexty = y;}
				
				
				if(minx > nextx || nextx > maxx || miny > nexty || nexty > maxy/* || gridpoints[x][y] != null*/) {
					
					break;
				}
				
				Point p = new Point(nextx, nexty);
				
				if(gridpoints[nextx][nexty] != null) { // if not null, something's there
					if(gridpoints[nextx][nexty].getX() == p.getX() && gridpoints[nextx][nexty].getY() == p.getY()) {
						if(!right) {right = true; nextx = x;}
						else if(!left) {left = true; nextx = x;}
						else if(!down) {down = true; nexty = y;}
						else if(!up)  {up = true;extrapoints.add(new Point(x, y));}
					}
				}
			}
		}
		points.addAll(extrapoints);
	}
	
	public ArrayList<Point> getPoints(){
		return points;
	}
	// ################ END THIRD PASS ################

}

