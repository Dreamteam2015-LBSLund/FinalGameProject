package com.dreamteam.villageTycoon.characters;

public class SabotageKit {
	private SabotageKitType sabotageKitType; 
	
	private boolean remove;
	
	public SabotageKit() {
		
	}
	
	public SabotageKit(SabotageKitType sabotageKitType) {
		this.sabotageKitType = sabotageKitType;
	}
	
	public void use() {
		remove = true;
	}
	
	public boolean getRemove() {
		return remove;
	}
}
