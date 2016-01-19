package com.dreamteam.villageTycoon.userInterface;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.characters.Character;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;

public class CreateCharacterButton extends UiElement {
	
	private Vector2 position;
	
	private String text;
	
	private boolean canAdd;
	
	private Character character;
	
	ArrayList<Character> toAdd;
	
	public CreateCharacterButton(Vector2 position, Character character) {
		super(new Rectangle(position.x, position.y, 300, 50));
		
		this.position = position;
		this.character = character;
		
		toAdd = new ArrayList<Character>();
		
		canAdd = true;
		
		text = (this.character instanceof Soldier) ? "add soldier" : "add worker";
	}
	
	public void update(Scene scene) {
		super.update();
		
		if(wasPressed()) {
			toAdd.add(this.character);
		}
		
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
				System.out.println("added");
			}
			break;
		}
		
		toAdd.clear();
	}

	public void draw(SpriteBatch batch) {
		AssetManager.font.draw(batch, text, getArea().getX(), getArea().getY() + getArea().getHeight());
	}
}
