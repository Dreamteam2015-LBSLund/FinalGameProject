package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class Character extends GameObject {
	private boolean selected;
	
	private int health;
	private Animation deathAnimation;
	
	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);
	
	private Animation selectedSign;
	
	public Character(Vector2 position, Animation sprite, Animation deathAnimation) {
		super(position, sprite);
		this.setSize(new Vector2(1, 1));
		selectedSign = new Animation(AssetManager.getTexture("test"), new Vector2(0.3f, 0.3f), new Color(0, 0, 1, 0.5f));
		this.deathAnimation = deathAnimation;
		health = 1;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		hitbox = new Rectangle(getPosition(), getSize());
		
		selectedSign.setPosition(getPosition().x+getSprite().getWidth()/2-selectedSign.getWidth()/2, getPosition().y+getSprite().getHeight());
		
		if(health <= 0) {
			if(getSprite() != deathAnimation) setSprite(deathAnimation);
			if(getSprite().animationDone()) getScene().removeObject(this);
			getSprite().animate(deltaTime);
		}
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		if(selected) selectedSign.draw(batch);
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
