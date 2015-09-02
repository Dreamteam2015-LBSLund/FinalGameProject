package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	
	private Rectangle selectionRectangle = new Rectangle(0, 0, 0, 0);
	private Sprite selectionSprite;
	private Vector2 selectionPoint = new Vector2(0, 0);
	
	public Character(Vector2 position, Animation sprite) {
		super(position, sprite);
		this.setSize(new Vector2(1, 1));
		deathAnimation = getSprite();
		health = 1;
		selectionSprite = new Sprite(new Texture("badlogic.jpg"));
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(Gdx.input.isTouched() && selectionRectangle == new Rectangle(0, 0, 0, 0)) {
			selectionPoint = getScene().getMouse();
		}
		
		if(Gdx.input.isTouched() && selectionRectangle != new Rectangle(0, 0, 0, 0)) {
			selectionRectangle = new Rectangle(getScene().getMouse().x-getScene().getMouse().x-selectionPoint.x, getScene().getMouse().y-getScene().getMouse().y-selectionPoint.y, getScene().getMouse().x-selectionPoint.x, getScene().getMouse().y-selectionPoint.y);
			selectionPoint = getScene().getMouse();
		}
		
		if(!Gdx.input.isTouched()) {
			selectionRectangle.set(0, 0, 0, 0);
		}
		
		selectionRectangle.printValues();
		
		if(health <= 0) {
			if(getSprite() != deathAnimation) setSprite(deathAnimation);
			System.out.println(getSprite() + ", " + getScene());
			if(getSprite().animationDone()) getScene().removeObject(this);
		}
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		selectionSprite.setX(selectionRectangle.getX());
		selectionSprite.setY(selectionRectangle.getY());
		selectionSprite.setSize(selectionRectangle.getWidth(), selectionRectangle.getHeight());
		selectionSprite.setColor(new Color(0, 1, 0, 0.3f));
		selectionSprite.draw(batch);
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
