package com.dreamteam.villageTycoon.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.characters.Character;

public class Projectile extends GameObject {
	private ProjectileType projectileType;
	
	private int damege;
	
	private float currentSpeed;
	private float distanceTraveled;
	private float alpha;
	
	private float speed;
	private float angle;

	private Character owner;
	
	private Vector2 target;
	
	public Projectile(Vector2 position, Vector2 target, float speed, ProjectileType projectileType, Character owner) {
		super(position, new Animation(AssetManager.getTexture(projectileType.getSprite())));
		
		getSprite().setSize(0.3f, 0.3f);
		
		this.projectileType = projectileType;
		this.target = target;
		
		angle = (float)Math.atan2(this.getPosition().y - target.y, this.getPosition().x - target.x);
		this.speed = speed;
		
		this.owner = owner;
		
		setDepth(1);
		
		alpha = 1;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		distanceTraveled += currentSpeed * deltaTime;
		
		if(distanceTraveled >= projectileType.getMaxRange()) {
			alpha -= deltaTime/10;
		}
		
		System.out.println(currentSpeed);
		
		setPosition(getPosition().cpy().add(new Vector2(getVelocity().x*deltaTime, getVelocity().y*deltaTime)));
	}
	
	public Character getOwner() {
		return owner;
	}
	
	public Vector2 getVelocity() {
		return new Vector2((float)Math.cos(angle)*speed, (float)Math.sin(angle)*speed);
	}
	
	public int getDamege() {
		return damege;
	}
}
