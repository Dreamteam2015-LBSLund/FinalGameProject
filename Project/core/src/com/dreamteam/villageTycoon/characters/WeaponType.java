package com.dreamteam.villageTycoon.characters;

public class WeaponType {
	public enum Type { RIFE, HANDGUN, ROCKETLAUNCER, MELEE};
	
	private Type type;
	
	private int damege;
	private int maxFireRate;
	private int clipSize;
	
	private float recoil;
	
	private String name;
	
	public WeaponType(int damege, int maxFireRate, int clipSize, float recoil, String name, Type type) {
		this.damege = damege;
		this.maxFireRate = maxFireRate;
		this.clipSize = clipSize;
		this.recoil = recoil;
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return name + "\nType: " + type + "\n Damege: " + damege + "\n Firerate: " + maxFireRate
				+ "\n Clip Size: " + clipSize + "\n Recoil: " + recoil;
	}
}
