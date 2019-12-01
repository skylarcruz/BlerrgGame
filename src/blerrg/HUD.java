package blerrg;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class HUD extends Entity {
	private Image hpBar;
	private Vector velocity;
	private int HUDhp;
	
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
	
	public void renderHUD(Graphics g, Player p) {
		render(g);
		g.drawString(HUDhp + "/100", p.getX() - 590, p.getY() + 327);
	}
	
	public void update(Player p) {
		if (HUDhp != p.hp.getHealth()) {
			HUDhp = p.hp.getHealth();
			setHUDhealth(HUDhp);
		}
		this.setPosition(new Vector(p.getX(), p.getY()));
	}

}
