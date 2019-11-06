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
		
		bg.world = new WorldModel(bg.ScreenWidth, bg.ScreenHeight);
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		bg.world.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		//Process input
		bg.world.player.processInput(container.getInput());

		//Update the world
		bg.world.update(delta);
	}

	

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return BlerrgGame.PLAYINGSTATE;
	}

	
}
