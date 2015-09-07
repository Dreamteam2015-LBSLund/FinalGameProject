package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SabotageKitType {
	public enum ActivationType { FUSE, REMOTE, INSTANT };
	public enum EffectType { FIRE, EXPLOSION, NAPALM };
	
	private ActivationType activationType;
	private EffectType effectType;
	
	// TODO: Add list of what type of resources is need to build it
	
	private Sprite icon;
	
	private String name;
	
	private float fireDuration; 
	private float explosionRadius;
	
	public SabotageKitType(String name, float fireDuration, float explosionRadius, Sprite icon, ActivationType activationType, EffectType effectType) {
		this.name = name;
		this.fireDuration = fireDuration;
		this.explosionRadius = explosionRadius;
		this.activationType = activationType;
		this.effectType = effectType;
		this.icon = icon;
		this.icon.setSize(2, 2);
	}
	
	public ActivationType getActivationType() {
		return activationType;
	}
	
	public EffectType getEffectType() {
		return effectType;
	}
	
	public float getExplosionRaduis() {
		return explosionRadius;
	}
	
	public float getfireDuration() {
		return fireDuration;
	}
	
	public String getName() {
		return name;
	}
	
	public Sprite getIcon() {
		return icon;
	}
	
	public String getDescription() {
		return "";
	}
}
