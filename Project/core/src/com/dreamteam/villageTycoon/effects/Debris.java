package com.dreamteam.villageTycoon.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Debris extends GameObject {
	private Vector2 velocity;
	private Vector2 orginalSize;
	
	private float distanceToGround;
	private float rotation;
	private float fallSpeed;
	private float alpha;
	
	private boolean dontRemove;
	
	private Animation shadow;
	
	public Debris(Vector2 position, Vector2 velocity, float fallSpeed, String sprite, Vector2 size) {
		super(position, new Animation(AssetManager.getTexture(sprite)));
		
		this.velocity = velocity;
		this.fallSpeed = fallSpeed;
		this.alpha = 1;
		
		this.orginalSize = size;
		this.setSize(size);
		
		shadow = new Animation(AssetManager.getTexture(sprite));
		shadow.setSize(size.x, size.y);
		
		dontRemove = true;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(distanceToGround > 0.1f) {
			setDepth(1+distanceToGround);
			getSprite().setRotation(rotation);
			dontRemove = false;
		}
		else {
			setDepthBasedOnPosition();
			if(!dontRemove) alpha = MathUtils.lerp(alpha, 0, 0.05f);
		}
		
		distanceToGround += fallSpeed * deltaTime;
		
		fallSpeed -= 2 * deltaTime;
		
		distanceToGround = MathUtils.clamp(distanceToGround, 0, 10);
		
		setPosition(getPosition().cpy().add(new Vector2(velocity.x*deltaTime, velocity.y*deltaTime)));
		
		velocity.x *= 0.95f;
		velocity.y *= 0.95f;
		
		setColor(new Color(1, 1, 1, alpha));
		shadow.setColor(new Color(0, 0, 0, alpha));
		
		getSprite().setPosition(getPosition().x, getPosition().y + distanceToGround);
		setSize(orginalSize.cpy().add(distanceToGround, distanceToGround));
		
		shadow.setPosition(getPosition().x, getPosition().y);
	}
	
	public void draw(SpriteBatch batch) {
		//shadow.draw(batch);
		super.draw(batch);
	}
}
