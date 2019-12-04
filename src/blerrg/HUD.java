package blerrg;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class HUD extends Entity {
	private Image hpBar;
	private Vector velocity;
	private int HUDhp;
	
	private int p1Score;
	private int p2Score;
	private int p3Score;
	private int p4Score;
	
	public HUD(Player p) {
		super(p.getX(), p.getY());
		HUDhp = p.hp.getHealth();
		
		addImage(ResourceManager.getImage(BlerrgGame.HUD_HP_BORDER), new Vector (-560, 335));
		hpBar = ResourceManager.getImage(BlerrgGame.HUD_HP_BAR).getScaledCopy(130 * HUDhp/100, 24);
		addImage(hpBar, new Vector (-560 - (65 - 130 * HUDhp/200), 335));
	}
	
	public void setHUDhealth(int h) {
		removeImage(hpBar);
		hpBar = ResourceManager.getImage(BlerrgGame.HUD_HP_BAR).getScaledCopy(130 * HUDhp/100, 24);
		addImage(hpBar, new Vector (-560 - (65 - 130 * HUDhp/200), 335));
	}
	
	public void renderHUD(StateBasedGame game, Graphics g, Player p) {
		BlerrgGame bg = (BlerrgGame)game;
		
		render(g);
		g.drawString(HUDhp + "/100", p.getX() - 590, p.getY() + 327);
		
		g.drawString("P1 Score: " + p1Score, p.getX() + 520, p.getY() - 355);
		if (bg.p2Active)
			g.drawString("P2 Score: " + p2Score, p.getX() + 520, p.getY() - 335);
		if (bg.p3Active)
			g.drawString("P3 Score: " + p3Score, p.getX() + 520, p.getY() - 315);
		if (bg.p4Active)
			g.drawString("P4 Score: " + p4Score, p.getX() + 520, p.getY() - 295);
	}
	
	public void update(Player p) {
		if (HUDhp != p.hp.getHealth()) {
			HUDhp = p.hp.getHealth();
			setHUDhealth(HUDhp);
		}
		this.setPosition(new Vector(p.getX(), p.getY()));
	}
	
	public void setScore(String p, int s) {
		if (p == "p1") p1Score = s;
		if (p == "p2") p2Score = s;
		if (p == "p3") p3Score = s;
		if (p == "p4") p4Score = s;
	}
	
	public String checkForWinner(StateBasedGame game) {
		BlerrgGame bg = (BlerrgGame)game;
		
		if (p1Score >= bg.winScore) return "W:P1|";
		else if (p2Score >= bg.winScore) return "W:P2|";
		else if (p3Score >= bg.winScore) return "W:P3|";
		else if (p4Score >= bg.winScore) return "W:P4|";
		else return "noWin";
	}

}
