package worldModel;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

import jig.Entity;

public class QuadNode {

	//Allow this to be set, should be 2x tile size at least
	public static int minSize = 64;
	
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
		
		if(width > minSize) {
			//Split horizontally
			if(height > minSize) {
				//Also split vertically
				//Create 4 child nodes with size width/2 X height/2
				children = new ArrayList<QuadNode>();
				//Upper left
				QuadNode ul = new QuadNode(area.getCenterX() - area.getWidth()/2,
						area.getCenterY() - area.getHeight()/2, area.getWidth()/2, area.getHeight()/2);
				children.add(ul);
				
				//Upper right
				QuadNode ur = new QuadNode(area.getCenterX() + area.getWidth()/2,
						area.getCenterY() - area.getHeight()/2, area.getWidth()/2, area.getHeight()/2);
				children.add(ur);
				
				//Lower Left
				QuadNode ll = new QuadNode(area.getCenterX() - area.getWidth()/2,
						area.getCenterY() - area.getHeight()/2, area.getWidth()/2, area.getHeight()/2);
				children.add(ll);
				
				//Lower Right
				QuadNode lr = new QuadNode(area.getCenterX() + area.getWidth()/2,
						area.getCenterY() + area.getHeight()/2, area.getWidth()/2, area.getHeight()/2);
				children.add(lr);
			}
			else {
				//Do not split vertically, only 2 nodes
				//Create 2 child nodes with size width/2 X height
				children = new ArrayList<QuadNode>();
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
				children = new ArrayList<QuadNode>();
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
	
}
