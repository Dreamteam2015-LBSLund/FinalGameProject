package com.dreamteam.villageTycoon.characters;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.map.Map;

public class Controller extends GameObject {
	private Rectangle selectionRectangle = new Rectangle(0, 0, 0, 0);

	private Vector2 selectionPoint;
	private Vector2[] waypoints;
	
	private boolean mousePressed;
	private boolean rightMouseIsPressed;
	private boolean canMoveUnits;
	// keeps track if units are moving into a building
	private boolean sentToBuilding;
	
	// building the units are moving into
	private Building building;
	
	private float cameraSpeed;
	// if mouse is 16 units within the border of the screen the camera moves
	final float MOVE_CAMERA_FIELD = 16;
	
	ArrayList<Character> selectedCharacters;
	
	public Controller() {
		super(new Vector2(0, 0), new Animation(AssetManager.getTexture("selectionRectangle")));
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
		deselectAll();
		
		Character c = null;
		for (GameObject g : getScene().getObjects()) {
			if (g instanceof Character) {
				c = (Character)g;
				if (c.getHitbox().collision(selectionRectangle)){
					select(c);
				}
			}
		}
	}
	
	private void select(Character c) {
		if (!selectedCharacters.contains(c)) selectedCharacters.add(c);
		c.setSelected(true);
	}
	
	private void deselect(Character c) {
		selectedCharacters.remove(c);
	}
	
	private void deselectAll() {
		for (Character c : selectedCharacters) {
			c.setSelected(false);
		}
		
		selectedCharacters.clear();
		
		for (int i = selectedCharacters.size() - 1; i >= 0; i--) {
			deselect(selectedCharacters.get(i));
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

		waypoints = new Vector2[selectedCharacters.size()];
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			for (GameObject c : getScene().getObjects()) {
				if (c instanceof Character) {
					if(!((Character)c).getShowInventroy()) {
						if(((Character)c).getHitbox().collision(new Rectangle(getScene().getWorldMouse(), new Vector2(0.3f, 0.3f))) && canMoveUnits) {
							((Character)c).setShowInventory(true);
						}
					} 
				}
			}
		}
		
		for (Character c : selectedCharacters) {
			canMoveUnits = !((Character)c).getShowInventroy();
			if(!canMoveUnits) {
				break;
			}
		}		
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) { 
			if(!rightMouseIsPressed) {
				if(canMoveUnits) {
					for (GameObject b : getScene().getObjects()) {
						if (b instanceof Building) {
							// if the player presses on a building then the units are sent to that building
							if(new Rectangle(getScene().getWorldMouse(), new Vector2(0.3f, 0.3f)).collision(b.getHitbox())) {
								this.sentToBuilding = true;
								this.building = ((Building)b);
							}
							else
							{
								this.sentToBuilding = false;
							}
						}
					}
					addWaypoints();
				}
			}
			rightMouseIsPressed = true;
		} else {
			rightMouseIsPressed = false;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.Q)) {
			for (GameObject c : getScene().getObjects()) {
				if (c instanceof Character) {
					if(((Character)c).getShowInventroy()) {
						((Character)c).setShowInventory(false);
					}
				}
			}
		}
		
		Vector2 rel = getScene().getWorldMouse().sub(selectionPoint);
		selectionRectangle = new Rectangle(selectionPoint.x, selectionPoint.y, rel.x, rel.y);
		selectionRectangle.normalize();
	}
	
	public void addWaypoints() {
		int currentUnit = 0;
		int newLineCount = 0;
		int currentLine = 0;
		int lineOffset = 0;
		
		for(int i = 0; i < waypoints.length; i++) {
			// either send units to a bulding or to their waypoints
			if(!sentToBuilding) waypoints[i] = new Vector2(i-lineOffset, currentLine).add(getScene().getWorldMouse());
			else {
				waypoints[i] = building.getPosition();
			}
			
			// put waypoints in a formation
			if(newLineCount >= waypoints.length/2) {
				currentLine += 1;
				lineOffset = currentLine * waypoints.length/2; 
				newLineCount = 0;
			}
			
			newLineCount += 1;
		}
		
		for (Character c : selectedCharacters) {
			((Character)c).setPath(waypoints[currentUnit]);
			currentUnit += 1;
		}
	}
	
	public void cameraMovment(float deltaTime) {
		// I tried to replicate the camera movment from red alert.
		// TODO: Make the camera not when the mouse is over UI
		
		if(getScene().getScreenMouse().x >= Gdx.graphics.getWidth()-MOVE_CAMERA_FIELD  && getScene().getCamera().position.x <= Map.WIDTH - getScene().getCamera().viewportWidth/2 - deltaTime*cameraSpeed) {
			getScene().getCamera().translate(new Vector2(deltaTime*cameraSpeed, 0));
		}
		
		if(getScene().getScreenMouse().x <= MOVE_CAMERA_FIELD && getScene().getCamera().position.x >= getScene().getCamera().viewportWidth/2 - -deltaTime*cameraSpeed) {
			getScene().getCamera().translate(new Vector2(-deltaTime*cameraSpeed, 0));
		}
		
		if(getScene().getScreenMouse().y >= Gdx.graphics.getHeight()-MOVE_CAMERA_FIELD && getScene().getCamera().position.y >= getScene().getCamera().viewportHeight/2 - -deltaTime*cameraSpeed) {
			getScene().getCamera().translate(new Vector2(0, -deltaTime*cameraSpeed));
		}
		
		if(getScene().getScreenMouse().y <= MOVE_CAMERA_FIELD && getScene().getCamera().position.y <= Map.HEIGHT - getScene().getCamera().viewportHeight/2 - deltaTime*cameraSpeed) {
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
