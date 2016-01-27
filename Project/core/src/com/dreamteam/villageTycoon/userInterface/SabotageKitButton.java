package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.characters.SabotageKit;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class SabotageKitButton extends UiElement {
	private Animation sprite;
	
	private SabotageKit sabotageKit;
	
	public SabotageKitButton(Rectangle area, Animation sprite) {
		super(area);
		sprite.setPosition(area.getX(), area.getY());
		sprite.setSize(area.getWidth(), area.getHeight());
		
		this.sprite = sprite;
		
		//this.sabotageKit = sabotageKit;
	}
	
	public void update(Soldier soldier) {
		super.update();
		if(isPressed()) {
			// TODO: Make it possible to also pick the sabotageKit 
			soldier.setPrepareSabotageKit(true);
		}
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
}
