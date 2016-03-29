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
	}
	
	// TODO: write the tutuorial
	
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
