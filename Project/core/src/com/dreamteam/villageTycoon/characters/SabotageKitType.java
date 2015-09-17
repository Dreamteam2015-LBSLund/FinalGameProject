package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;

public class SabotageKitType {
	public enum ActivationType { FUSE, REMOTE, INSTANT };
	public enum EffectType { FIRE, EXPLOSION, NAPALM };
	
	private ActivationType activationType;
	private EffectType effectType;
	
	// TODO: Add list of what type of resources is need to build it
	
	private Sprite icon;
	private Animation inGameSprite;
	
	private String name;
	
	private float fireDuration; 
	private float explosionRadius;
	
	public SabotageKitType(String name, float fireDuration, float explosionRadius, String icon, ActivationType activationType, EffectType effectType) {
		this.name = name;
		this.fireDuration = fireDuration;
		this.explosionRadius = explosionRadius;
		this.activationType = activationType;
		this.effectType = effectType;
		this.icon = new Sprite(AssetManager.getTexture(icon));
		this.icon.setSize(2, 2);
		
		this.inGameSprite = new Animation(AssetManager.getTexture(icon+"inGame"));
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
		return name + "\n Activation Type: " + activationType;
	}
}
