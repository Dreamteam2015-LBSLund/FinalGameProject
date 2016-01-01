package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.effects.Explosion;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.projectiles.Projectile;
import com.dreamteam.villageTycoon.projectiles.ProjectileType;
import com.dreamteam.villageTycoon.projectiles.ProjectileType.Type;

public class SabotageKitType {
	public enum ActivationType { FUSE, REMOTE, INSTANT };
	public enum EffectType { FIRE, EXPLOSION };
	
	private ActivationType activationType;
	private EffectType effectType;
	
	// TODO: Add list of what type of resources is need to build it
	
	private Sprite icon;
	private Animation inGameSprite;
	
	private String name;
	
	private float fireDuration; 
	private float explosionRadius;
	
	private Projectile projectile;
	private Explosion explosion;
	
	//public Explosion(Vector2 position, Type type, float radius, float fireDuration, int maxDamege) {
	
	public SabotageKitType(String name, float fireDuration, float explosionRadius, String icon, ActivationType activationType, EffectType effectType) {
		this.name = name;
		this.fireDuration = fireDuration;
		this.explosionRadius = explosionRadius;
		this.activationType = activationType;
		this.effectType = effectType;
		this.icon = new Sprite(AssetManager.getTexture(icon));
		this.icon.setSize(2, 2);
		
		this.projectile = constructProjectile();
		
		this.inGameSprite = new Animation(AssetManager.getTexture(icon+"inGame"));
	}
	
	public Projectile constructProjectile() {		
		Explosion.Type type = (effectType == EffectType.FIRE) ? Explosion.Type.FIRE : Explosion.Type.EXPLOSION;
		ProjectileType projectileType = new ProjectileType(ProjectileType.Type.TRHOWABLE, 32, 2, 0, new Explosion(new Vector2(0, 0), type, this.explosionRadius, this.getExplosionRaduis(), 1), "test");
		return new Projectile(Vector2.Zero, Vector2.Zero, projectileType, null);
	}
	
	public void createProjectile(Vector2 position, Character owner, Scene scene) {
		projectile.setPosition(position);
		projectile.setOwner(owner);
		
		scene.addObject(projectile);
	}
	
	public Projectile getProjectile() {
		return projectile;
	}
	
	public ActivationType getActivationType() {
		return activationType;
	}
	
	public EffectType getEffectType() {
		return effectType;
	}
	
	public float getExplosionRaduis() {
		return explosionRadius;
	}
	
	public float getfireDuration() {
		return fireDuration;
	}
	
	public String getName() {
		return name;
	}
	
	public Sprite getIcon() {
		return icon;
	}
	
	public String getDescription() {
		return name + "\n Activation Type: " + activationType;
	}
}
