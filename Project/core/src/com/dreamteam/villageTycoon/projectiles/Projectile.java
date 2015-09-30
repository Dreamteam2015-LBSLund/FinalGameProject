package com.dreamteam.villageTycoon.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Projectile extends GameObject {
	private ProjectileType projectileType;
	
	private float currentSpeed;
	private float distanceTraveled;
	private float alpha;
	
	private float angle;
	
	private Vector2 target;
	private Vector2 velocity;
	
	public Projectile(Vector2 position, Vector2 target, ProjectileType projectileType) {
		super(position, projectileType.getSprite());
		
		this.projectileType = projectileType;
		this.target = target;
		
		angle = (float)Math.atan2(this.getPosition().y - target.y, this.getPosition().x - target.x);
		
		alpha = 1;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		distanceTraveled += currentSpeed * deltaTime;
		
		if(distanceTraveled >= projectileType.getMaxRange()) {
			alpha -= deltaTime/10;
		}
		
		this.getPosition().add(getVelocity(deltaTime));
	}
	
	public Vector2 getVelocity(float deltaTime) {
		return new Vector2((float)Math.cos(angle)*currentSpeed*deltaTime, (float)Math.sin(angle)*currentSpeed*deltaTime);
	}
}
