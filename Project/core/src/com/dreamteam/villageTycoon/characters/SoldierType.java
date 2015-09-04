package com.dreamteam.villageTycoon.characters;

import com.dreamteam.villageTycoon.framework.Animation;

public class SoldierType {
	// Classes basically
	private float speed;
	private float accuracy;
	private float toughness;
	
	private int startHealth;
	
	private boolean enemy;
	
	private Animation typeSprite;
	
	public SoldierType(float speed, float accuracy, float toughness, int startHealth, Animation typeSprite) {
		this.speed = speed;
		this.accuracy = accuracy;
		this.toughness = toughness;
		this.startHealth = startHealth;
		
		this.typeSprite = typeSprite;
	}
	
	public float getAccuracy() {
		return accuracy;
	}
	
	public float getSpeed() {
		return speed;
	}

	public float getToughness() {
		return toughness;
	}

	public int getStartHealth() {
		return startHealth;
	}
	
	public boolean getEnemy() {
		return enemy;
	}
	
	public Animation getTypeSprite() {
		return typeSprite;
	}
	
}
