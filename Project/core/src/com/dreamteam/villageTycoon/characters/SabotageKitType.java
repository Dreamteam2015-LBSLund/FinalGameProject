package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SabotageKitType {
	enum ActivationType { FUSE, REMOTE, INSTANT };
	enum EffectType { FIRE, EXPLOSION, NAPALM };
	
	private ActivationType activationType;
	private EffectType effectType;
	
	// TODO: Add list of what type of resources is need to build it
	
	private Sprite icon;
	
	private String name;
	
	private float fireDuration; 
	private float explosionRadius;
	
	public SabotageKitType() {
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
