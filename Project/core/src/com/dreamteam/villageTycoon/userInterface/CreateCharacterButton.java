package com.dreamteam.villageTycoon.userInterface;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.characters.Character;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;

public class CreateCharacterButton extends TextButton {
	
	private Vector2 position;
	
	private boolean canAdd;
	private boolean added;
	private boolean pressed;
	
	private Building building;
	
	ArrayList<Character> toAdd;

	private CharSequence text;
	
	public CreateCharacterButton(Vector2 position, Building building) {
		super(new Rectangle(position.x, position.y, 300, 50), (!building.getType().getName().equals("house")) ? "add soldier" : "add worker");
		
		this.position = position;
		this.building = building;
		
		toAdd = new ArrayList<Character>();
		
		canAdd = true;
	}
	
	
	public void update(Scene scene) {
		super.update();
		
		added = false;
		
		if(wasPressed() && !pressed) {
			Character c = building.getCharacterToSpawn();
			toAdd.add(c);
			canAdd = true;
			
			pressed = true;
		}
		
		if(!isPressed() && pressed) pressed = false;
		
		for(Character c : toAdd) {
			for(GameObject o : scene.getObjects()) {
				if(o instanceof Character) {
					if(o == c) {
						canAdd = false;
						break;
					}
				}
			}
			
			if(canAdd) { 
				scene.addObject(c);
				added = true;
			}
		}
		
		toAdd.clear();
		canAdd = true;
		
		super.update();
	}
	
	public boolean getAdded() {
		return this.added;
	}
	
	public void setCanAdd(boolean canAdd) {
		this.canAdd = canAdd;
	}
}
