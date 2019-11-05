package blerrg;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.Vector;
import worldModel.WorldModel;

public class PlayingState extends BasicGameState {

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		//TODO: Add to world Model
		bg.player = new Player(bg.ScreenWidth/2, bg.ScreenHeight/2, 0, 0, 0);
		
		//TODO: Implement in world model, create world model here
		bg.world = new WorldModel();
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		//Translate Camera to achieve scrolling
		bg.cameraX = bg.player.getPosition().getX() - bg.ScreenWidth/2;
		bg.cameraY = bg.player.getPosition().getY() - bg.ScreenHeight/2;
		g.translate(-bg.cameraX, -bg.cameraY);
		
		//Render Tiles
//		for (Tile t : bg.tiles) { t.render(g); }
		bg.world.render(g);
		
		//Render player
		bg.player.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		BlerrgGame bg = (BlerrgGame)game;
		
		//START PLAYER MOVEMENT
		boolean a = input.isKeyDown(Input.KEY_A) ? true : false;
		boolean w = input.isKeyDown(Input.KEY_W) ? true : false;
		boolean d = input.isKeyDown(Input.KEY_D) ? true : false;
		boolean s = input.isKeyDown(Input.KEY_S) ? true : false;
		
		
		if (a) { //moving left, top-left, bottom-left
			bg.player.setStopped(false);
			if (w) {
				bg.player.setVelocity(new Vector(-0.20f, -0.20f));
				bg.player.setDirection(7);
			} else if (s) {
				bg.player.setVelocity(new Vector(-0.20f, +0.20f));
				bg.player.setDirection(5);
			} else {
				bg.player.setVelocity(new Vector(-0.25f, 0));
				bg.player.setDirection(6);
			}
		} else if (d) { //moving right, top-right, bottom-right
			bg.player.setStopped(false);
			if (w) {
				bg.player.setVelocity(new Vector(+0.20f, -0.20f));
				bg.player.setDirection(1);
			} else if (s) {
				bg.player.setVelocity(new Vector(+0.20f, +0.20f));
				bg.player.setDirection(3);
			} else {
				bg.player.setVelocity(new Vector(+0.25f, 0));
				bg.player.setDirection(2);
			}			
		} else if (w) { //moving up, top-right, top-left
			bg.player.setStopped(false);
			if (a) {
				bg.player.setVelocity(new Vector(-0.20f, -0.20f));
				bg.player.setDirection(7);
			} else if (d) {
				bg.player.setVelocity(new Vector(+0.20f, -0.20f));
				bg.player.setDirection(1);
			} else {
				bg.player.setVelocity(new Vector(0, -0.25f));
				bg.player.setDirection(0);
			}
		} else if (s) { //moving down, bottom-left, bottom-right
			bg.player.setStopped(false);
			if (a) {
				bg.player.setVelocity(new Vector(-0.20f, +0.20f));
				bg.player.setDirection(5);
			} else if (d) {
				bg.player.setVelocity(new Vector(+0.20f, +0.20f));
				bg.player.setDirection(3);
			} else {
				bg.player.setVelocity(new Vector(0, +0.25f));
				bg.player.setDirection(4);
			}
		} else {
			bg.player.setStopped(true);
			bg.player.setVelocity(new Vector(0, 0));
		}
		//END PLAYER MOVEMENT
		
		//Update entities
		bg.player.update(delta);
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return BlerrgGame.PLAYINGSTATE;
	}

	
}
