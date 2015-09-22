package com.dreamteam.villageTycoon.projectiles;

import com.dreamteam.villageTycoon.effects.Explosion;

public class ProjectileType {
	public enum Type { SHOT, TRHOWABLE }; 
	
	private Type type;
	
	private Explosion explosion;
	
	private int damege;
	
	private float maxRange;
	
	public ProjectileType(Type type, int damege, float maxRange) {
		
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
