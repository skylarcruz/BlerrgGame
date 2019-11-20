package worldModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import org.newdawn.slick.geom.Rectangle;

import blerrg.BlerrgGame;
import jig.Entity;
import jig.Shape;

public class QuadNode {

	//Allow this to be set, should be 2x tile size at least
	public static int minSize = 32;
	
	//Child nodes
	ArrayList<QuadNode> children;
	
	//Game Entities that are not in child nodes
	ArrayList<Entity> entities;
	
	//Parent node
	QuadNode parent;
	
	//Section of the map covered by this node
	Rectangle area;
	
	public QuadNode(float pos_x, float pos_y, float width, float height) {
		
		//Create  the area rectangle
		area = new Rectangle(pos_x, pos_y, width, height);
		
		entities = new ArrayList<Entity>();
		children = new ArrayList<QuadNode>();
		
		buildSubtree(width, height);
		
	}

	public void buildSubtree(float width, float height) {
		if(width > minSize) {
			//Split horizontally
			if(height > minSize) {
				//Also split vertically
				//Create 4 child nodes with size width/2 X height/2
				
				//Upper left
				QuadNode ul = new QuadNode(area.getCenterX() - area.getWidth()/2,
						area.getCenterY() - area.getHeight()/2, area.getWidth()/2, area.getHeight()/2);
				children.add(ul);
				
				//Upper right
				QuadNode ur = new QuadNode(area.getCenterX(),
						area.getCenterY() - area.getHeight()/2, area.getWidth()/2, area.getHeight()/2);
				children.add(ur);
				
				//Lower Left
				QuadNode ll = new QuadNode(area.getCenterX() - area.getWidth()/2,
						area.getCenterY(), area.getWidth()/2, area.getHeight()/2);
				children.add(ll);
				
				//Lower Right
				QuadNode lr = new QuadNode(area.getCenterX(),
						area.getCenterY(), area.getWidth()/2, area.getHeight()/2);
				children.add(lr);
			}
			else {
				//Do not split vertically, only 2 nodes
				//Create 2 child nodes with size width/2 X height
				
				//Left
				QuadNode l = new QuadNode(area.getCenterX() - area.getWidth()/2,
						area.getCenterY(), area.getWidth()/2, area.getHeight());
				children.add(l);
				
				//Right
				QuadNode r = new QuadNode(area.getCenterX() + area.getWidth()/2,
						area.getCenterY(), area.getWidth()/2, area.getHeight());
				children.add(r);
			}
		}
		else {
			//Do not split horizontally
			if(height > minSize) {
				//Split vertically
				//Create 2 child nodes with size width X height/2
				
				//Upper right
				QuadNode u = new QuadNode(area.getCenterX(),
						area.getCenterY() - area.getHeight()/2, area.getWidth(), area.getHeight()/2);
				children.add(u);
				//Lower Left
				QuadNode l = new QuadNode(area.getCenterX(),
						area.getCenterY() + area.getHeight()/2, area.getWidth(), area.getHeight()/2);
				children.add(l);
			}
			
			//Do not split vertically - leaf node
			
		}
	}
	
	
	//Attempt to an entity to this node.
	//Check if it can be added to child nodes
	public boolean addEntity(Entity ent) {
		
		//add to this node
		//entities.add(ent);
		
		
		//if the entity is not within the bounds, it should belong to the parent
		if(!entityInBounds(ent)) {
			BlerrgGame.debugPrint("Entity not in bounds");
			return false;
		}
		
		BlerrgGame.debugPrint("Entity is within bounds");
		
		//Entity is in bounds, attempt to add to child nodes
		boolean addedToChild = false;
		for(QuadNode c: children) {
			
			addedToChild = c.addEntity(ent);
			
			//Entity was successfully added to child or child's subtree
			if(addedToChild) {
				BlerrgGame.debugPrint("Entity was added to child node");
				return true;
			}
		}
		
		BlerrgGame.debugPrint("\tEntity not added to child node. Adding to this node");
		//Entity could not be added to any children, add to this node
		entities.add(ent);
		return true;
	}
	
	
	//Checks whether an entity is fully within the bounds of this node
	public boolean entityInBounds(Entity ent) {
		
		//Check the bounding box
		
		LinkedList<Shape> shapes = ent.getGloballyTransformedShapes();
		
		for(Shape s: shapes) {
			float points[] = s.getPoints();
			//System.out.println("Shape points: "+points.toString());
			for(int i=0; i<points.length-1; i+= 2) {
				//BlerrgGame.debugPrint(String.format("ShapePoint: (%f, %f)", points[i], points[i+1]));
				if(!area.contains(points[i],points[i+1])) return false;
			}
		}
		
		return true;
		
	}
	

	public ArrayList<Entity> getAllEntities(){
		
		//Create copy of this node's entities
		ArrayList<Entity> ents = (ArrayList<Entity>) entities.clone();
		
		//Recursively get the entities from child nodes
		for(QuadNode child: children) {
			ents.addAll(child.getAllEntities());
		}
		
		return ents;
	}
	
	public void printNodeTree(int depth) {
		//create indent based on depth
		String indent = String.join("", Collections.nCopies(depth, "- "));
		
		//print this node's info
		if(children.size() > 0 || entities.size() > 0) {
			BlerrgGame.debugPrint(indent, String.format("(%d, %d, %d, %d)", (int)area.getMinX(),
					(int)area.getMinY(), (int)area.getMaxX(), (int)area.getMaxY()), 
					" Children: "+children.size()+" ",
					"Entities: "+entities.size());
		}
		
		//Print each of the children
		for(QuadNode child : children) {
			child.printNodeTree(depth+1);
		}
	}
}
