package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class Character extends GameObject {
	// So that all characters owned by the player can be controlled without reapting code
	private boolean selected;
	
	private int health;
	private Animation deathAnimation;
	
	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);
	
	public Character(Vector2 position, Animation sprite) {
		super(position, sprite);
		this.setSize(new Vector2(1, 1));
		deathAnimation = getSprite();
		health = 1;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		hitbox = new Rectangle(getPosition(), getSize());
		
		if(selected) {
			setColor(new Color(1, 0.5f, 1, 0.1f));
		} else {
			setColor(new Color(1, 1, 1, 1));
		}
		
		if(health <= 0) {
			if(getSprite() != deathAnimation) setSprite(deathAnimation);
			if(getSprite().animationDone()) getScene().removeObject(this);
		}
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getHealth() {
		return health;
	}
	
	public boolean getDead() {
		return health <= 0;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean getSelected() {
		return selected;
	}
}
