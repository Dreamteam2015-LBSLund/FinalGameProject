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

public class Controller extends GameObject {
	private Rectangle selectionRectangle = new Rectangle(0, 0, 0, 0);

	private Vector2 selectionPoint;
	
	private boolean active;

	public Controller() {
		super(new Vector2(0, 0), new Animation(new Texture("badlogic.jpg")));
		setColor(new Color(0, 1, 0, 0.3f));
		setDepth(1000);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		setSize(new Vector2(selectionRectangle.getWidth(), selectionRectangle.getHeight()));
		if(selectionPoint != null) setPosition(selectionPoint);
		
		selectionRectangle.printValues();
		
		if(Gdx.input.isTouched() && selectionRectangle != new Rectangle(0, 0, 0, 0)) {
			active = true;
			selectionRectangle = new Rectangle(getScene().getMouse().x-selectionRectangle.getWidth(), getScene().getMouse().y-selectionRectangle.getHeight(), 
					getScene().getMouse().x-selectionPoint.x, getScene().getMouse().y-selectionPoint.y);
		}
		
		if(!Gdx.input.isTouched()) {
			active = false;
			selectionRectangle.set(0, 0, 0, 0);
			selectionPoint = getScene().getMouse();
		}
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}
	
	public boolean getActive() {
		return active;
	}
	
	public Rectangle getSelectionRectangle() {
		return selectionRectangle;
	}
}
