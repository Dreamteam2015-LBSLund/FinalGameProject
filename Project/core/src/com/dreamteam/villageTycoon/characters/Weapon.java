package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.Gdx;

public class Weapon {
	private WeaponType type;
	
	private int clipCount;
	
	private float reloadCount;
	private float currentFireRate;
	
	private boolean reloading;
	
	public Weapon(WeaponType type) {
		this.type = type;
	}
	
	public void update() {
		if(currentFireRate > 0) coolDown();
		
		if(clipCount < 1) reload(); 
	}
	
	public void coolDown() {
		currentFireRate += Gdx.graphics.getDeltaTime()*100;

		if(currentFireRate >= type.getMaxFireRate()) {
			currentFireRate = 0;
		}
	}
	
	public void reload() {
		reloading = true;
		
		reloadCount += Gdx.graphics.getDeltaTime()*100;
		if(reloadCount >= type.getReloadTime()) {
			clipCount = type.getClipSize();
			reloadCount = 0;
			reloading = false;
		}
	}
	
	public boolean canShoot() {
		return !reloading && currentFireRate <= 0;
	}
	
	public float getCurrentFireRate() {
		return currentFireRate;
	}
	
	public boolean getReloading() {
		return reloading;
	}
	
	public WeaponType getType() {
		return type;
	}
}
