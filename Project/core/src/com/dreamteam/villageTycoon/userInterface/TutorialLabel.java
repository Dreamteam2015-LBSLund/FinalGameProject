package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;

public class TutorialLabel {
	final float NEXT_PART_TIME = 1;
	
	private Vector2 position;
	
	private String[] parts;
	
	private int currentPart;
	
	private float changePartCount;
	
	public TutorialLabel(Vector2 position) {
		this.position = position;
		
		parts = new String[4];
		
		parts[0] = "Left click, or left click and drag to select characters \nRight click to send them to a position or building \nThey attack and work autonomously";
		parts[1] = "Build factories to create weapons for soldiers \nRight click on selected character to bring up inventory \nPress on the sabotagekit to use it";
		parts[2] = "Destroy all houses and kill all characters to defeat a village \nMore characters at a house or barrack ups the production speed of characters";
		parts[3] = "Build buildings using the building placer \nZoom out with the arrows at Zoom \nChange game speed with the arrows at Speed";
	}
	
	public void update(float deltaTime) {
		if(currentPart < parts.length-1) {
			changePartCount += 1 * deltaTime;
		
			if(changePartCount >= NEXT_PART_TIME) {
				currentPart += 1;
				changePartCount = 0;
			}
		}
	}
	
	public void draw(SpriteBatch batch) {
		AssetManager.font.draw(batch, parts[currentPart], position.x, position.y);
	}
}
