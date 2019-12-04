package blerrg;

import org.newdawn.slick.state.BasicGameState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

import worldModel.WorldModel;

public class EndState extends BasicGameState {
	
	private String winner;
	private int waitTimer;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		BlerrgGame bg = (BlerrgGame)game;
		waitTimer = 200;

		
	}
	
	public void setWinner(String p) {
		winner = p;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		g.drawString("Winner is " + winner, 600, 400);
		
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		
		waitTimer -= 1;
		if (waitTimer <= 0)
			bg.enterState(BlerrgGame.MENUSTATE);
		
	}
	
	@Override
	public int getID() {
		return BlerrgGame.ENDSTATE;
	}

}
