package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class WeaponType {
	public enum Type { RIFE, HANDGUN, ROCKETLAUNCER, MELEE };
	
	Sprite icon;
	
	private Type type;
	
	private int damege;
	private int clipSize;
	
	private float maxFireRate;
	private float reloadTime;
	private float recoil;
	
	private String name;
	
	public WeaponType(String name, int damege, float maxFireRate, int clipSize, float recoil, float reloadTime, Sprite icon, Type type) {
		this.damege = damege;
		this.maxFireRate = maxFireRate;
		this.clipSize = clipSize;
		this.recoil = recoil;
		this.reloadTime = reloadTime;
		this.name = name;
		this.type = type;
		this.icon = icon;
		
		icon.setSize(2, 2);
	}
	
	public int getClipSize() {
		return clipSize;
	}
	
	public float getMaxFireRate() {
		return maxFireRate;
	}
	
	public float getReloadTime() {
		return reloadTime;
	}
	
	public String getName() {
		return name;
	}
	
	public Sprite getIcon() {
		return icon;
	}
	
	public String getDescription() {
		return name + "\nType: " + type + "\n Damege: " + damege + "\n Firerate: " + maxFireRate
				+ "\n Clip Size: " + clipSize + "\n Recoil: " + recoil;
	}
}
