package com.dreamteam.villageTycoon.effects;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Explosion extends GameObject {
	public enum Type { NAPALM, EXPLOSION };
	
	private Type type;
	
	private float radius;
	private float fireDuration;
	
	public Explosion(Vector2 position, Animation sprite, Type type, float radius, float fireDuration) {
		super(position, sprite);
		this.type = type;
		this.radius = radius;
		sprite.setSize(radius, radius);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(getSprite().animationDone()) {
			getScene().removeObject(this);
		}
	}
}
