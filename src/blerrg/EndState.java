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
	
	private int p1Score;
	private int p2Score;
	private int p3Score;
	private int p4Score;
	
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
		
		g.drawImage(ResourceManager.getImage(BlerrgGame.BLERRG_LOGO).getScaledCopy((float) 0.5), 425, 50);
		
		g.drawString("Blerrg Champion: " + winner, bg.ScreenWidth/2 - 100, bg.ScreenHeight/2 - 40);
		g.drawString("Player 1: " + p1Score + " kills", bg.ScreenWidth/2 - 100, bg.ScreenHeight/2);
		if (bg.p2Active)
			g.drawString("Player 2: " + p2Score + " kills", bg.ScreenWidth/2 - 100, bg.ScreenHeight/2 + 20);
		if (bg.p3Active)
			g.drawString("Player 3: " + p3Score + " kills", bg.ScreenWidth/2 - 100, bg.ScreenHeight/2 + 40);
		if (bg.p4Active)
			g.drawString("Player 4: " + p4Score + " kills", bg.ScreenWidth/2 - 100, bg.ScreenHeight/2 + 60);
		
	}
	
	public void setFinalScore(StateBasedGame game) {
		BlerrgGame bg = (BlerrgGame)game;
		p1Score = bg.world.player.score;
		if(bg.p2Active)
			p2Score = bg.world.player2.score;
		if(bg.p3Active)
			p3Score = bg.world.player3.score;
		if(bg.p4Active)
			p4Score = bg.world.player4.score;
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
