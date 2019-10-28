package blerrg;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartState extends BasicGameState {

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		BlerrgGame bg = (BlerrgGame)game;
		g.drawString("Press Space to start", bg.ScreenWidth/2 - 30, bg.ScreenHeight/2);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		BlerrgGame bg = (BlerrgGame)game;

		if (input.isKeyDown(Input.KEY_SPACE)) {
			bg.enterState(BlerrgGame.PLAYINGSTATE);
		}
	}

	@Override
	public int getID() {
		return BlerrgGame.STARTSTATE;
	}

	

}
