package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.characters.WeaponType;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.map.Resource;

public class TargetWeaponButton extends UiElement {
	private WeaponType weaponType;
	
	private String text;
	
	private boolean goForWeapon;
	
	public TargetWeaponButton(Vector2 position, WeaponType weaponType) {
		super(new Rectangle(position.x, position.y, 300, 50));
		
		this.weaponType = weaponType;
		this.text = this.weaponType.getName();
	}
	
	public void update(Soldier soldier) {
		super.update();
		
		if(wasPressed()) {
			soldier.setTargetWeapon(weaponType);
		}
	}
	
	public void draw(SpriteBatch batch) {
		AssetManager.font.draw(batch, text, getArea().getX(), getArea().getY() + getArea().getHeight());
	}
}
