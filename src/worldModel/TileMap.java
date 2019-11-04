package maps;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import blerrg.Tile;
import gameModel.TiledTestGame;
//import characters.GameCharacter;
import jig.Vector;
import maps.Tile.TileType;

public class TileMap {

	//Actual tile objects
	public Tile tiles[][];
	
	//Scaling factor for tile images and bounds
	float tileScale = 1.0f;
	
	Integer rows, columns;
	Integer frameHeight, frameWidth;
	
	Vector mapOrigin = new Vector(0,0);
	
	//Will be defined in level files
	List<Tile> catSpawnTiles;
	public Tile entryTile, exitTile;
	
	
	/**
	 * Proper Constructor. Attempts to load the map from the provided filename
	 * @param mapName - name of the map to be loaded from file.
	 */
	public TileMap(String mapName, int p_width, int p_height) {
		
		frameHeight = p_height;
		frameWidth = p_width;
		
		loadMap(mapName);
		
		printTileGrid();
	}
	

	
	
	private void ScaleMapToSize(int p_width, int p_height) {
		
		//need to determine number of tiles and base tile size
		int mapWidth = columns*Tile.TILE_BASE_SIZE;
		int mapHeight = rows*Tile.TILE_BASE_SIZE;
		
		
		System.out.println("Scaling: Map("+mapWidth+", "+mapHeight+")");
		System.out.println("Frame: ("+p_width+", "+p_height+")");
		
		Tile.Scale = Math.min((float)p_height/mapHeight , (float)p_width/mapWidth);
		
		centerMap( p_width, p_height, (int)(columns*Tile.TILE_BASE_SIZE*Tile.Scale), 
				(int)(rows*Tile.TILE_BASE_SIZE*Tile.Scale) );
		
	}
	
	
	private void centerMap(int p_width, int p_height, int m_width, int m_height) {
		//Assuming map already scaled
		
		System.out.println("Centering map");
		System.out.println("Frame: "+p_width+", "+p_height);
		System.out.println("Map: "+m_width+", "+m_height);
		
		//get actual center
		int fcenterX = p_width/2;
		int fcenterY = p_height/2;
		
		//Map center
		int mcenterX = m_width/2;
		int mcenterY = m_height/2;
		
		//Now compute the new origin
		mapOrigin = mapOrigin.setX(fcenterX - mcenterX);
		mapOrigin = mapOrigin.setY(fcenterY - mcenterY);
		
		System.out.println("New map origin: "+mapOrigin.toString());
		
	}

	
	
	/**
	 * Obtains a tile at a given position within the tile grid
	 * @param row
	 * @param col
	 * @return The Tile object at the given position
	 */
	public Tile getTileAt(int col, int row) {
		
		//Ensure Tile Position is within bounds
		if(row >= tiles.length || col >= tiles[0].length) {
			System.err.println("Tile position "+row+", "+col+" is out of bounds");
			return null;
		}
		
		if(row < 0|| col < 0) {
			System.err.println("Tile position "+row+", "+col+" is out of bounds");
			return null;
		}
		
		return tiles[col][row];
		
	}
	
	public Tile[] getAdjacentTiles(Tile base) {
		
		Tile adj[] = new Tile[8]; //maximum of 8 adjacent tiles
		
		int rowColPairs[] = {-1, 0, -1, 1,
							0, 1, 1, 1,
							1, 0, 1, -1,
							0, -1, -1, -1};
		
		for(int i=0; i<adj.length; i++) {
			adj[i] = getTileAt(base.row + rowColPairs[2*i], 
								base.col + rowColPairs[2*i + 1]);
		}
		
		return adj;
		
		
	}
	
	
	public void populateTiles(TiledMap t_map) {
		
		
		//int tileSize = 64;
		//Scale the tile size. This depends on the frame size
		int tileSize = (int) (Tile.TILE_BASE_SIZE*Tile.Scale);
		
		//Get the number of layers
		int layerCount = t_map.getLayerCount();
		
		
		//Layers that we care about
		String floorLayerName = "Floor";
		String wallLayerName = "Walls";
		
		
		//Iterate through each layer in the document
		for(int layer_num=0; layer_num < layerCount; layer_num++) {
			//Get the width and height of the layer in tiles
			
			//TODO: This part is not working for some reason.
			//Not parsing correctly
			
			//Attempting to process for each layer, but isn't working
//			int width = Integer.parseInt(
//					t_map.getLayerProperty(layer_num, "width", "-1"));
//			int height = Integer.parseInt(
//					t_map.getLayerProperty(layer_num, "height", "-1"));
//			
			
			int width = t_map.getWidth();
			int height = t_map.getHeight();
			
			//Assuming all tiles have same type in layer, get the tile type
			TileType type = tileTypeFromLayer(t_map, layer_num);
			
			
			//Skip this layer if the width and height are not present
			if(width == -1 || height == -1) continue;
			
			
			//Move through the entries, creating the correct tile type
			for(int r=0; r < height; r++) {
				for(int c=0; c < width; c++) {	
					
					//Get the GID of the map tile in this layer
					int t_gid = t_map.getTileId(c, r, layer_num);

					if(t_gid == 0) {
						//There is no tile defined at this point in this layer. skip
						continue;
					}
					
					//use the gid to determine tile type and image
					// multiple gids will correspond to the same type
					//TileType type = Tile.typeFromGID(t_gid);
					
					
					System.out.println("GID: "+t_gid+", type= "+type);
					
					//Get the newest tile image, scaled
					Image t_image = t_map.getTileImage(c, r, layer_num).getScaledCopy(Tile.Scale);
					
					
					Tile t;
					//Check If a tile is already in this position
					if(tiles[c][r] != null) {
						//Set t to point to the tile
						t = tiles[c][r];
						
						//update the tile type
						t.setType(type);
						
						//add the image on top of previous images
						if(t.tileType != TileType.FLOOR) {
							t.addImageWithBoundingBox(t_image);
						}
						else {
							t.addImageWithBoundingBox(t_image);
						}
						
					}
					else {
						//No tile exists, create new tile
						t = new Tile(type, t_image);
						tiles[c][r] = t;
						t.setPosition(mapOrigin.getX() + tileSize/2 + c*tileSize,
								mapOrigin.getY() + tileSize/2 + r*tileSize);
						
						t.row = r;
						t.col = c;
						
					}
					
					
				}
			}
		}
		
	}


	private TileType tileTypeFromLayer(TiledMap t_map, int layer_num) {
		String typeString = t_map.getLayerProperty(layer_num, "LayerType", "Floor");
		
		TileType type = null;
		switch(typeString){
			case "Floor": type = TileType.FLOOR; break;
			case "Walls": type = TileType.WALL; break;
			default: type = TileType.FLOOR;
		}
		
		return type;
	}
	
	public static boolean tilesAdjacent(Tile tileA, Tile tileB) {
		
		//tiles are adjacent if they have the same column and a difference of 1 in row,
		if(tileA.col == tileB.col) {
			
			if(Math.abs(tileA.row - tileB.row) == 1) return true;
			else {
				return false;
			}
		}
		
		
		//or if they have the same row and difference of 1 in column
		if(tileA.row == tileB.row) {
			
			if(Math.abs(tileA.col - tileB.col) == 1) return true;
			else return false;
		}
		
		return false;
		
		
	}
	
	
	
	/***
	 * Load a map created with the Tiled tool.
	 * @param fileName
	 * @return True if loading was successful, false otherwise
	 */
	private boolean loadMap(String fileName) {
		
		try {
			//Attempt to get the map from file
			//NOTE: This is not the actual map, but the required TiledMap representation
			//  This allows pulling all of the map data
			TiledMap t_map = new TiledMap(fileName);
		
			System.out.println("Successfully loaded tiledMap: "+fileName);
			
			//Get basic data about the map
			int tileHeight = t_map.getTileHeight();
			int tileWidth = t_map.getTileWidth();
			
			//Create the tile set
			rows = t_map.getHeight();
			columns = t_map.getWidth();
			tiles = new Tile[rows][columns];
	
			//Scale map to fit the window
			ScaleMapToSize(frameWidth, frameHeight);
		
			
			
			//Add images and types to the tiles
			populateTiles(t_map);
		
			
			//Deal with objects on the map
			//t_map.getObjectCount(arg0);
			
			
		
			return true;
		
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to load map ");
			return false;
		}
		
		
	}
	
	
	
	public void render(GameContainer arg0, Graphics g) 
			throws SlickException {
		// TODO Auto-generated method stub
		
		//Draw each tile
		for(Tile[] _t:tiles) {
			for(Tile t: _t) {
				t.render(g);
			}
		}
		

	}
	
	

	
	//TODO: Implement this logic, but build a string for portability instead of directly
	// printing to system.out
	/**
	 * Testing function to ensure grid model functions as expected
	 */
	private void printTileGrid() {
		
		StringBuilder sb = new StringBuilder("Positions: \n");
		
		System.out.println("Printing Tile Grid\n-----------------------------\n");
		System.out.print("\t");
		//Iterating through each row
		for( int row=0; row < rows; row++) {
			//Iterating through each tile in the row
			for(int col=0; col < columns; col++) {
			
				Tile t = tiles[col][row];
				
				switch(t.tileType) {
				
				case FLOOR:
					System.out.print(" O ");
					break;
					
				case WALL:
					System.out.print(" X ");
					break;
					
				default:
					System.out.print(" O ");
					break;
				}
				
				
				sb.append("["+col+"]["+row+"] - ("+t.getPosition().getX()+", "+t.getPosition().getY()+")\n");
				
			}
			System.out.print("\n\t");
		}
		
		
		System.out.println("\n-----------------------------\n");
		
		System.out.println(sb);
		
	}

	
//	public void drawPath(GameCharacter c, Graphics g) {
//		
//		//For each tile on the path, draw a shaded rectangle
//		g.setColor(Color.orange);
//		
//		for(Tile t:c.getDestinations()) {
//			
//			g.drawRect(t.getX() - t.getCoarseGrainedWidth()/2, t.getY()- t.getCoarseGrainedHeight()/2,
//					t.getCoarseGrainedWidth(), t.getCoarseGrainedHeight());
//		}
//	}
}
