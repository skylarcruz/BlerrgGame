package blerrg;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import blerrg.Player.Projectile;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class HUD extends Entity {
	private Image hpBar;
	private int HUDhp;
	
	private Image stamBar;
	private int HUDstam;
	
	private int ammoRemaining;
	private int maxAmmo;
	
	private int p1Score;
	private int p2Score;
	private int p3Score;
	private int p4Score;
	
	public ArrayList<WeaponIcon> HUDweapons;
	
	public HUD(Player p) {
		super(p.getX(), p.getY());
		HUDhp = p.hp.getStat();
		HUDstam = p.stam.getStat();
		
		addImage(ResourceManager.getImage(BlerrgGame.HUD_HP_BORDER), new Vector (560, 335));
		hpBar = ResourceManager.getImage(BlerrgGame.HUD_HP_BAR).getScaledCopy(130 * HUDhp/100, 24);
		addImage(hpBar, new Vector (560 - (65 - 130 * HUDhp/200), 335));
		
		addImage(ResourceManager.getImage(BlerrgGame.HUD_HP_BORDER), new Vector (560, 300));
		stamBar = ResourceManager.getImage(BlerrgGame.HUD_STAM_BAR).getScaledCopy(130 * HUDstam/100, 24);
		addImage(stamBar, new Vector (560 - (65 - 130 * HUDstam/200), 300));
		
		ammoRemaining = p.weapons.get(p.getCurrentWeapon()).getBullets();
		maxAmmo = p.weapons.get(p.getCurrentWeapon()).getMaxAmmo();
		
		HUDweapons = new ArrayList<WeaponIcon>();
	}
	
	public void setHUDhealth(int h) {
		removeImage(hpBar);
		hpBar = ResourceManager.getImage(BlerrgGame.HUD_HP_BAR).getScaledCopy(130 * HUDhp/100, 24);
		addImage(hpBar, new Vector (560 - (65 - 130 * HUDhp/200), 335));
	}
	
	public void setHUDstam(int s) {
		removeImage(stamBar);
		stamBar = ResourceManager.getImage(BlerrgGame.HUD_STAM_BAR).getScaledCopy(130 * HUDstam/100, 24);
		addImage(stamBar, new Vector (560 - (65 - 130 * HUDstam/200), 300));
	}
	
	
	public void renderHUD(StateBasedGame game, Graphics g, Player p) {
		BlerrgGame bg = (BlerrgGame)game;
		
		render(g);
		g.drawString(HUDhp + "/100", p.getX() + 530, p.getY() + 327);
		g.drawString(HUDstam + "/100", p.getX() + 530, p.getY() + 291);
		
		if (ammoRemaining > 200 && maxAmmo > 200) { //knife.. no bullets.. should we get ∞ rendered here instead?
			g.drawString("Inf/Inf", p.getX() + 530, p.getY() + 265);
		} else {
			g.drawString(ammoRemaining + "/" + maxAmmo, p.getX() + 530, p.getY() + 265);
		}
		
		g.drawString("P1 Score: " + p1Score, p.getX() + 520, p.getY() - 355);
		if (bg.p2Active)
			g.drawString("P2 Score: " + p2Score, p.getX() + 520, p.getY() - 335);
		if (bg.p3Active)
			g.drawString("P3 Score: " + p3Score, p.getX() + 520, p.getY() - 315);
		if (bg.p4Active)
			g.drawString("P4 Score: " + p4Score, p.getX() + 520, p.getY() - 295);
		
		for(int i = 0; i < HUDweapons.size(); i++) {
			if (HUDweapons.get(i).active)
				g.drawImage(HUDweapons.get(i).getIconA(), p.getX() - 625 + (i * 60),p.getY() + 300);
			else
				g.drawImage(HUDweapons.get(i).getIconNA(), p.getX() - 625 + (i * 60),p.getY() + 300);
		}
	}
	
	public void update(Player p) {
		if (HUDhp != p.hp.getStat()) {
			HUDhp = p.hp.getStat();
			setHUDhealth(HUDhp);
		}
		if (HUDstam != p.stam.getStat()) {
			HUDstam = p.stam.getStat();
			setHUDstam(HUDstam);
		}
		if (ammoRemaining != p.weapons.get(p.getCurrentWeapon()).getBullets()) {
			ammoRemaining = p.weapons.get(p.getCurrentWeapon()).getBullets();
		}
		if (maxAmmo != p.weapons.get(p.getCurrentWeapon()).getMaxAmmo()) {
			maxAmmo = p.weapons.get(p.getCurrentWeapon()).getBullets();
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
		
		if (p1Score >= bg.winScore) return "E:P1|";
		else if (p2Score >= bg.winScore) return "E:P2|";
		else if (p3Score >= bg.winScore) return "E:P3|";
		else if (p4Score >= bg.winScore) return "E:P4|";
		else return "noWin";
	}
	
	public void addWeapon(Weapon w) {
		boolean isNew = true;
		int incAmmoLoc;
		
		for(int i = 0; i < HUDweapons.size(); i++) {
			if (w.type == HUDweapons.get(i).getType()) {
				isNew = false;
				incAmmoLoc = i;
				break;
			}
		}
		
		if (isNew) {
//			for(int i = 0; i < HUDweapons.size(); i++) {
//				HUDweapons.get(i).active = false;
//			}
			WeaponIcon newW = new WeaponIcon(w);
			HUDweapons.add(newW);
		}
		else {
			// Add Ammo to existing weapon 
		}
	}
	
	public void shiftWeapon(String dir) {
		int activeI = 0;
		for(int i = 0; i < HUDweapons.size(); i++) {
			if (HUDweapons.get(i).active == true) {
				activeI = i;
				break;
			}
		}
		
		if (dir == "Left")
			activeI -= 1;
		else
			activeI += 1;
		
		if (activeI < 0)
			activeI = HUDweapons.size() - 1;
		if (activeI > HUDweapons.size() - 1)
			activeI = 0;
		
		for(int i = 0; i < HUDweapons.size(); i++) {
			if (i != activeI)
				HUDweapons.get(i).active = false;
			else
				HUDweapons.get(i).active = true;
		}
	}
	
	public class WeaponIcon extends Entity {
		
		private String type;
		private boolean active;
		//private int AmmoLimit;
		//private int AmmoCount;
		
		private Image iconNA;
		public Image iconA;
		
		public WeaponIcon (Weapon w) {
			type = w.type;
			active = false;
			switch (type) {
				case "shotgun": iconNA = ResourceManager.getImage(BlerrgGame.SHOTGUN_ICON_NA);
								iconA = ResourceManager.getImage(BlerrgGame.SHOTGUN_ICON_A); break;
				default:  iconNA = ResourceManager.getImage(BlerrgGame.SHOTGUN_ICON_NA);
						  iconA = ResourceManager.getImage(BlerrgGame.SHOTGUN_ICON_A); break;
			}
			
			switch(type) {
				case BlerrgGame.WEAPON_CROSSBOW: iconNA = ResourceManager.getImage(BlerrgGame.CROSSBOW_ICON_NA);
					iconA = ResourceManager.getImage(BlerrgGame.CROSSBOW_ICON_A); break;
				case BlerrgGame.WEAPON_KNIFE: iconNA = ResourceManager.getImage(BlerrgGame.KNIFE_ICON_NA);
					iconA = ResourceManager.getImage(BlerrgGame.KNIFE_ICON_A); break;
				case BlerrgGame.WEAPON_RIFLE: iconNA = ResourceManager.getImage(BlerrgGame.RIFLE_ICON_NA);
					iconA = ResourceManager.getImage(BlerrgGame.RIFLE_ICON_A); break;
				case BlerrgGame.WEAPON_SHOTGUN: iconNA = ResourceManager.getImage(BlerrgGame.SHOTGUN_ICON_NA);
					iconA = ResourceManager.getImage(BlerrgGame.SHOTGUN_ICON_A); break;
				case BlerrgGame.WEAPON_SMG: iconNA = ResourceManager.getImage(BlerrgGame.SMG_ICON_NA);
					iconA = ResourceManager.getImage(BlerrgGame.SMG_ICON_A); break;
				default: iconNA = ResourceManager.getImage(BlerrgGame.KNIFE_ICON_NA);
					iconA = ResourceManager.getImage(BlerrgGame.KNIFE_ICON_A); break;
			}
		}
		
		public Image getIconA() {
			return iconA;
		}
		
		public Image getIconNA() {
			return iconNA;
		}
		
		public String getType() {
			return type;
		}
		
		public void setActive(boolean b) {
			active = b;
		}
		
		public boolean getActive() {
			return active;
		}
		
	}

}
