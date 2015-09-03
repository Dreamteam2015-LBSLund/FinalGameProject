package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
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
	
	private boolean mousePressed;

	public Controller() {
		super(new Vector2(0, 0), new Animation(new Texture("badlogic.jpg")));
		setColor(new Color(0, 1, 0, 0.3f));
		setDepth(1000);
		selectionPoint = new Vector2();
	}
	
	void onMousePressed() {
		System.out.println("mouse pressed");
		selectionPoint = new Vector2(getScene().getMouse());
	}
	
	void onMouseReleased() {
		System.out.println("mouse released");
		for (GameObject g : getScene().getObjects()) {
			if (g instanceof Character && ((Character) g).getHitbox().collision(selectionRectangle)) {
				((Character)g).setSelected(true);
			}
		}
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (!Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (mousePressed) {
				onMouseReleased();
			}
			mousePressed = false;
		} else {
			if (!mousePressed) {
				onMousePressed();
			}
			mousePressed = true;
		}
	
		Vector2 rel = getScene().getMouse().sub(selectionPoint);
		selectionRectangle = new Rectangle(selectionPoint.x, selectionPoint.y, rel.x, rel.y);
		if (selectionRectangle.getWidth() < 0) selectionRectangle.set(selectionRectangle.getX() + selectionRectangle.getWidth(), selectionRectangle.getY(), -selectionRectangle.getWidth(), selectionRectangle.getHeight());
		if (selectionRectangle.getHeight() < 0) selectionRectangle.set(selectionRectangle.getX(), selectionRectangle.getY() + selectionRectangle.getHeight(), selectionRectangle.getWidth(), -selectionRectangle.getHeight());
	}
	
	public void draw(SpriteBatch batch) {
		if (mousePressed) {
			getSprite().setBounds(selectionRectangle.getX(), selectionRectangle.getY(), selectionRectangle.getWidth(), selectionRectangle.getHeight());
			super.draw(batch);
		}
	}
	
	public boolean getActive() {
		return false;
	}
	
	public Rectangle getSelectionRectangle() {
		return selectionRectangle;
	}
}
