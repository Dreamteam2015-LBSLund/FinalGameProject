package com.dreamteam.villageTycoon.effects;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Explosion extends GameObject {
	public enum Type { NAPALM, EXPLOSION };
	
	private Type type;
	
	private float radius;
	private float fireDuration;
	
	public Explosion(Vector2 position, Animation sprite, Type type, float radius, float fireDuration, int maxFrame) {
		super(position, sprite);
		this.type = type;
		this.radius = radius;
		this.getSprite().setAnimation(0.1f, maxFrame, 0, true);
		sprite.setSize(radius, radius);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		getSprite().animate(deltaTime);
		
		if(getSprite().animationDone()) {
			getScene().removeObject(this);
		}
	}
}
