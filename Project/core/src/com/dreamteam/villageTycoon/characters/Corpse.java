package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Corpse extends GameObject {
	private float deteriorateTime;
	private float alpha;
	
	final float MAX_DETERIORATE_TIME = 10;
	
	public Corpse(Vector2 position, Animation sprite) {
		super(position, sprite);
		this.setSize(new Vector2(1, 1));
		alpha = 1;
	}

	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(getSprite().getMaxFrame() > 0) getSprite().animate(deltaTime);
		
		deteriorateTime += deltaTime*5;
		
		if(deteriorateTime >= MAX_DETERIORATE_TIME) {
			alpha -= deltaTime*2;
		}

		setColor(new Color(1, 1, 1, alpha));
		
		if(alpha <= 0) getScene().removeObject(this);
	}
}
