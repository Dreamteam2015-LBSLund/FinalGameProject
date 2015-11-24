package com.dreamteam.villageTycoon.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
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
	
	public Projectile(Vector2 position, Vector2 target, float speed, ProjectileType projectileType) {
		super(position, projectileType.getSprite());
		
		this.projectileType = projectileType;
		this.target = target;
		
		angle = (float)Math.atan2(this.getPosition().y - target.y, this.getPosition().x - target.x);
		this.speed = speed;
		
		setDepth(1);
		
		alpha = 1;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		distanceTraveled += currentSpeed * deltaTime;
		
		if(distanceTraveled >= projectileType.getMaxRange()) {
			alpha -= deltaTime/10;
		}
		
		setPosition(getPosition().cpy().add(new Vector2(getVelocity(deltaTime).x*deltaTime, getVelocity(deltaTime).y*deltaTime)));
	}
	
	public Character getOwner() {
		return owner;
	}
	
	public Vector2 getVelocity(float deltaTime) {
		return new Vector2((float)Math.cos(angle)*currentSpeed*deltaTime, (float)Math.sin(angle)*currentSpeed*deltaTime);
	}
	
	public int getDamege() {
		return damege;
	}
}
