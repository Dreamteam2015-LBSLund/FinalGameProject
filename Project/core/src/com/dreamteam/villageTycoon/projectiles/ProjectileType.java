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
	private int damege;
	
	public ProjectileType(Type type, float maxRange, float speed, int damege, String sprite) {
		this.type = type;
		this.maxRange = maxRange;
		this.speed = speed;
		this.sprite = sprite;
		this.damege = damege;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public int getDamege() {
		return damege;
	}
	
	public String getSprite() {
		return sprite;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setExplosion(Explosion explosion) {
		this.explosion = explosion;
	}
	
	public Explosion getExplosion() {
		return explosion;
	}
	
	public float getMaxRange() {
		return maxRange;
	}
}
