package com.dreamteam.villageTycoon.projectiles;

import com.dreamteam.villageTycoon.effects.Explosion;
import com.dreamteam.villageTycoon.framework.Animation;

public class ProjectileType {
	public enum Type { SHOT, TRHOWABLE }; 
	
	private Type type;
	
	private Explosion explosion;
	
	private Animation sprite;
	
	private float maxRange;
	private float speed;
	
	public ProjectileType(Type type, float maxRange, float speed, Animation sprite) {
		this.type = type;
		this.maxRange = maxRange;
		this.speed = speed;
		this.sprite = sprite;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public Animation getSprite() {
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
