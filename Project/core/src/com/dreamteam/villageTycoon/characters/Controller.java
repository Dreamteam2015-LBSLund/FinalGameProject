package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
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
	private boolean rightMouseIsPressed;

	private float cameraSpeed;
	final float MOVE_CAMERA_FIELD = 16;
	
	public Controller() {
		super(new Vector2(0, 0), new Animation(new Texture("badlogic.jpg")));
		setColor(new Color(0, 1, 0, 0.3f));
		setDepth(1000);
		selectionPoint = new Vector2();
		cameraSpeed = 5;
	}
	
	void onMousePressed() {
		selectionPoint = new Vector2(getScene().getWorldMouse());
	}
	
	void onMouseReleased() {
		for (GameObject g : getScene().getObjects()) {
			if (g instanceof Character) {
				((Character)g).setSelected(((Character) g).getHitbox().collision(selectionRectangle));
			}
		}
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		cameraMovment(deltaTime);
		
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
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) { 
			if(!rightMouseIsPressed) {
				for (GameObject c : getScene().getObjects()) {
					if (c instanceof Character) {
						if(((Character)c).getSelected()) {
							((Character)c).setPath(getScene().getWorldMouse());
						}
					}
				}
			}
			rightMouseIsPressed = true;
		} else {
			rightMouseIsPressed = false;
		}
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			for (GameObject s : getScene().getObjects()) {
				if (s instanceof Soldier) {
					if(!((Soldier)s).getShowInventroy()) {
						if(((Soldier)s).getHitbox().collision(new Rectangle(getScene().getWorldMouse(), new Vector2(0.3f, 0.3f)))) {
							((Soldier)s).setShowInventory(true);
						}
					} 
					if(((Soldier)s).getShowInventroy()) {
						if(Gdx.input.isKeyJustPressed(Keys.Q)) {
							((Soldier)s).setShowInventory(false);
						}
					}
				}
			}
		}
	
		for (GameObject s : getScene().getObjects()) {
			if (s instanceof Soldier) {
				if(((Soldier)s).getShowInventroy()) {
					if(Gdx.input.isKeyJustPressed(Keys.Q)) {
						((Soldier)s).setShowInventory(false);
					}
				}
			}
		}
		
		Vector2 rel = getScene().getWorldMouse().sub(selectionPoint);
		selectionRectangle = new Rectangle(selectionPoint.x, selectionPoint.y, rel.x, rel.y);
		selectionRectangle.normalize();
	}
	
	public void cameraMovment(float deltaTime) {
		// I tried to replicate the camera movment from red alert.
		// TODO: Make the camera not when the mouse is over UI
		if(getScene().getScreenMouse().x >= Gdx.graphics.getWidth()-MOVE_CAMERA_FIELD) {
			getScene().getCamera().translate(new Vector2(deltaTime*cameraSpeed, 0));
		}
		
		if(getScene().getScreenMouse().x <= MOVE_CAMERA_FIELD) {
			getScene().getCamera().translate(new Vector2(-deltaTime*cameraSpeed, 0));
		}
		
		if(getScene().getScreenMouse().y >= Gdx.graphics.getHeight()-MOVE_CAMERA_FIELD) {
			getScene().getCamera().translate(new Vector2(0, -deltaTime*cameraSpeed));
		}
		
		if(getScene().getScreenMouse().y <= MOVE_CAMERA_FIELD) {
			getScene().getCamera().translate(new Vector2(0, deltaTime*cameraSpeed));
		}
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
