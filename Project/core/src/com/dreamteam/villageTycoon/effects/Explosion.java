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
		this.setSize(new Vector2(radius, radius));
		this.getSprite().setAnimation(0.5f, 6, false);
		this.maxDamege = maxDamege;
		AssetManager.getSound("explosion").play();
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(getSprite().getCurrentFrame() >= 5) {
			getScene().removeObject(this);
		}
		
		getSprite().animate(deltaTime);
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
