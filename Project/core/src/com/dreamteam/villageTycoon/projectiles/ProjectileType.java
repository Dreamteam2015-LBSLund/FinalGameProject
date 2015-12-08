package com.dreamteam.villageTycoon.projectiles;

import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.effects.Explosion;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.Scene;

public class ProjectileType {
	public enum Type { SHOT, TRHOWABLE }; 
	
	private Type type;
	
	private Explosion explosion;
	
	private String sprite;
	
	private float maxRange;
	private float speed;
	
	public ProjectileType(Type type, float maxRange, float speed, String sprite) {
		this.type = type;
		this.maxRange = maxRange;
		this.speed = speed;
		this.sprite = sprite;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public String getSprite() {
		return sprite;
	}
	
	public Type getType() {
		return type;
	}
	
	public Explosion getExplosion() {
		return explosion;
	}
	
	public float getMaxRange() {
		return maxRange;
	}
}
