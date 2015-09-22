package com.dreamteam.villageTycoon.characters;

import java.util.ArrayList;

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
	private Vector2[] waypoints;
	
	private boolean mousePressed;
	private boolean rightMouseIsPressed;
	private boolean canMoveUnits;
	
	private float cameraSpeed;
	final float MOVE_CAMERA_FIELD = 16;
	
	ArrayList<Character> selectedCharacters;
	
	public Controller() {
		super(new Vector2(0, 0), new Animation(new Texture("badlogic.jpg")));
		setColor(new Color(0, 1, 0, 0.3f));
		setDepth(1000);
		selectionPoint = new Vector2();
		cameraSpeed = 5;
		
		selectedCharacters = new ArrayList<Character>();
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
		
		for (GameObject c : getScene().getObjects()) {
			if (c instanceof Character) {
				if(((Character)c).getSelected())
						selectedCharacters.add((Character) c);
			}
		}
		
		waypoints = new Vector2[selectedCharacters.size()];
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			for (GameObject s : getScene().getObjects()) {
				if (s instanceof Soldier) {
					if(!((Soldier)s).getShowInventroy()) {
						if(((Soldier)s).getHitbox().collision(new Rectangle(getScene().getWorldMouse(), new Vector2(0.3f, 0.3f))) && canMoveUnits) {
							((Soldier)s).setShowInventory(true);
						}
					} 
				}
			}
		}
		
		for (GameObject c : getScene().getObjects()) {
			if (c instanceof Soldier) {
				canMoveUnits = !((Soldier)c).getShowInventroy();
			
				if(!canMoveUnits) {
					break;
				}
			}
		}		
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) { 
			if(!rightMouseIsPressed) {
				if(canMoveUnits) addWaypoints();
			}
			rightMouseIsPressed = true;
		} else {
			rightMouseIsPressed = false;
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
		
		selectedCharacters.clear();
	}
	
	public void addWaypoints() {
		// TODO: Move a waypoint if it lands on a unwalkable tile
		
		int currentUnit = 0;
		int newLineCount = 0;
		int currentLine = 0;
		int lineOffset = 0;
		
		for(int i = 0; i < waypoints.length; i++) {
			waypoints[i] = new Vector2(i-lineOffset, currentLine).add(getScene().getWorldMouse());
			
			if(newLineCount >= waypoints.length/2) {
				currentLine += 1;
				lineOffset = currentLine * waypoints.length/2; 
				newLineCount = 0;
			}
			
			newLineCount += 1;
		}
		
		for (GameObject c : getScene().getObjects()) {
			if (c instanceof Character) {
				if(((Character)c).getSelected()) {
					if(canMoveUnits) {
						((Character)c).setPath(waypoints[currentUnit]);
					}
					
					currentUnit += 1;
				}
			}
		}
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
