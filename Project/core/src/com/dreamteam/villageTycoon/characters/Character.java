package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class Character extends GameObject {
	// So that all characters owned by the player can be controlled without reapting code
	private boolean selected;
	
	private int health;
	private Animation deathAnimation;
	
	private Rectangle selectionRectangle;
	
	public Character(Vector2 position, Animation sprite) {
		super(position, sprite);
		this.setSize(new Vector2(1, 1));
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(health <= 0) {
			if(getSprite() != deathAnimation) setSprite(deathAnimation);
			if(getSprite().animationDone()) getScene().removeObject(this);
		}
	}
	
	public Vector3 getMouse() {
		return null;
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
