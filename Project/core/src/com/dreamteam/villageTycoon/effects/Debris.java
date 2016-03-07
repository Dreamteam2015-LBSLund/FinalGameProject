package com.dreamteam.villageTycoon.effects;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Debris extends GameObject {
	final float FALL_SPEED = 0.5f;
	
	private Vector2 velocity;
	private Vector2 orginalSize;
	
	private float distanceToGround;
	
	public Debris(Vector2 position, Vector2 velocity, Vector2 size) {
		super(position, new Animation(AssetManager.getTexture("debris1")));
		
		this.orginalSize = size;
		this.setSize(size);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		getSprite().setPosition(getPosition().x, getPosition().y + distanceToGround);
		setSize(orginalSize.cpy().add(distanceToGround, distanceToGround));
	}
}
