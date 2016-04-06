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
	
	private boolean hasHit;
	
	public Projectile(Vector2 position, Vector2 target, ProjectileType projectileType, Character owner) {
		super(position, new Animation(AssetManager.getTexture(projectileType.getSprite())));
		
		this.setSize(new Vector2(0.3f, 0.3f));

		this.projectileType = projectileType;
		this.speed = this.projectileType.getSpeed();
		this.damege = this.projectileType.getDamege();
		this.target = target;
		
		angle = (float)Math.atan2(target.y - this.getPosition().y, target.x - this.getPosition().x);
		
		this.owner = owner;
		
		setDepth(1);
		
		alpha = 1;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		distanceTraveled += speed * deltaTime;
		
		this.setDepthBasedOnPosition();
		
		if(distanceTraveled >= projectileType.getMaxRange()) {
			getScene().removeObject(this);
		}
		
		setPosition(getPosition().cpy().add(new Vector2(getVelocity().x*deltaTime, getVelocity().y*deltaTime)));
	}
	
	public void onHit() {
		getScene().removeObject(this);
		
		if(projectileType.getExplosion() != null) {
			projectileType.getExplosion().setPosition(this.getPosition());
			getScene().addObject(projectileType.getExplosion());
		}
	}
	
	public void setHasHit(boolean hasHit) {
		this.hasHit = hasHit;
	}
	
	public boolean getHasHit() {
		return this.hasHit;
	}
	
	public void setTarget(Vector2 target) {
		this.target = target;
		this.angle = (float)Math.atan2(target.y - this.getPosition().y, target.x - this.getPosition().x);
	}
	
	public void setOwner(Character owner) {
		this.owner = owner;
	}
	
	public Character getOwner() {
		return owner;
	}
	
	public ProjectileType getProjectileType() {
		return projectileType;
	}
	
	public Vector2 getVelocity() {
		return new Vector2((float)Math.cos(angle)*speed, (float)Math.sin(angle)*speed);
	}
	
	public float getSpeed() {
		return this.speed;
	}
	
	public int getDamage() {
		return damege;
	}
}
