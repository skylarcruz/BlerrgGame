package worldModel;

import java.util.ArrayList;

import blerrg.BlerrgGame;
import blerrg.Tile;
import jig.Entity;

public class QuadTree {

	QuadNode rootNode;
	
	//Build a quadtree to cover the entire map
	public QuadTree(int p_width, int p_height) {
		
		rootNode = new QuadNode(-Tile.size,-Tile.size,p_width + Tile.size, p_height+Tile.size);
	}
	
	
	//insert an entity into the tree
	public void addEntity(Entity ent) {
		
		if(!rootNode.addEntity(ent)) {
			BlerrgGame.debugPrint("Error: Quadtree unable to add object: "+ent.toString());
		}
		
	}
	
	
	public ArrayList<Entity> nearbyEntities(Entity mainEntity){
		
		//get the deepest node in the tree
		
		QuadNode currNode = rootNode;
		
		boolean inBounds = currNode.entityInBounds(mainEntity);
		int depth = 0;
		//Walk through the quadtree to find the last node that fully contains the entity 
		while(inBounds) {
			
			//check the next nodes
			for(QuadNode qn : currNode.children) {
				inBounds = qn.entityInBounds(mainEntity);
				if(inBounds) {
					currNode = qn;
					depth++;
					continue;
				}
			}
		}
		
		//BlerrgGame.debugPrint("Tree depth probed: ",depth);
		
		//currNode is the last node that fully contains the entity
		// now get all entities in this node and in it's children
		return currNode.getAllEntities();
		
	}
	
	
	public void printTree() {
		
		//start with root node
		BlerrgGame.debugPrint("Printing QuadTree ---------------------");
		
		rootNode.printNodeTree(0);
		
		BlerrgGame.debugPrint("---------------------------------------");
		
	}
}
