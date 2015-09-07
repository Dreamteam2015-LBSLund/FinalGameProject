package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SabotageKit {
	private SabotageKitType sabotageKitType; 
	
	private boolean remove;
	
	public SabotageKit(SabotageKitType sabotageKitType) {
		this.sabotageKitType = sabotageKitType;
	}
	
	public void use() {
		remove = true;
	}
	
	public Sprite getIcon() {
		return sabotageKitType.getIcon();
	}
	
	public boolean getRemove() {
		return remove;
	}
}
