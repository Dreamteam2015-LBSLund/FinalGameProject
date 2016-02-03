package com.dreamteam.villageTycoon.effects;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Explosion extends GameObject {
	public enum Type { FIRE, EXPLOSION };
	
	private Type type;
	
	private float radius;
	private float fireDuration;
	
	private int maxDamege;
	
	public Explosion(Vector2 position, Type type, float radius, float fireDuration, int maxDamege) {
		super(position, new Animation(AssetManager.getTexture("explosion")));
		this.type = type;
		this.radius = radius;
		this.getSprite().setAnimation(0.1f, 7, true);
		this.getSprite().setSize(radius, radius);
		this.maxDamege = maxDamege;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		getSprite().animate(deltaTime);
		
		if(getSprite().animationDone()) {
			getScene().removeObject(this);
		}
	}
	
	public Type getType() {
		return type;
	}
	
	public int getMaxDamege() {
		return maxDamege;
	}
	
	public float getRadius() {
		return radius;
	}
}
