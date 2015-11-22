package com.dreamteam.villageTycoon.effects;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Explosion extends GameObject {
	public enum Type { NAPALM, EXPLOSION };
	
	private Type type;
	
	private float radius;
	private float fireDuration;
	
	public Explosion(Vector2 position, Type type, float radius, float fireDuration) {
		super(position, new Animation(AssetManager.getTexture("explosion")));
		this.type = type;
		this.radius = radius;
		this.getSprite().setAnimation(0.1f, 7, 0, true);
		getSprite().setSize(radius, radius);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		getSprite().animate(deltaTime);
		
		if(getSprite().animationDone()) {
			getScene().removeObject(this);
		}
	}
}
