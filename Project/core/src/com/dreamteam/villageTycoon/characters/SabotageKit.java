package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Scene;

public class SabotageKit {
	private SabotageKitType sabotageKitType; 
	
	private boolean remove;
	
	public SabotageKit(SabotageKitType sabotageKitType) {
		this.sabotageKitType = sabotageKitType;
	}
	
	public void use(Soldier soldier, Vector2 target, Scene scene) {
		this.sabotageKitType.createProjectile(new Vector2(soldier.getPosition().x, soldier.getPosition().y), target, soldier, scene);
		remove = true;
	}
	
	public Sprite getIcon() {
		return sabotageKitType.getIcon();
	}
	
	public boolean getRemove() {
		return remove;
	}
}
