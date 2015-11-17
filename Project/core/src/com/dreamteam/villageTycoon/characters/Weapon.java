package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Weapon {
	private WeaponType type;
	
	private int clipCount;
	
	private float reloadCount;
	private float currentFireRate;
	
	public Weapon(WeaponType type) {
		this.type = type;
	}
	
	public void update(float deltaTime) {
		if(currentFireRate > 0) coolDown(deltaTime);
		
		if(clipCount < 1) reload(deltaTime); 
	}
	
	public void coolDown(float deltaTime) {
		currentFireRate += deltaTime;

		if(currentFireRate >= type.getMaxFireRate()) {
			currentFireRate = 0;
		}
	}

	public void reload(float deltaTime) {
		reloadCount += deltaTime;
		if(reloadCount >= type.getReloadTime()) {
			clipCount = type.getClipSize();
			reloadCount = 0;
		}
	}
	
	public void onShoot() {
		clipCount -= 1;
		currentFireRate = 0.1f;
	}
	
	public Sprite getIcon() {
		return getType().getIcon();
	}
	
	public boolean canShoot() {
		return clipCount < 1 && currentFireRate <= 0;
	}
	
	public float getCurrentFireRate() {
		return currentFireRate;
	}
	
	public WeaponType getType() {
		return type;
	}
}
