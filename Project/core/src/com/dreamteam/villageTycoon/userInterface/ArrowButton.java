package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class ArrowButton extends UiElement {
	public enum Direction { UP, DOWN };
	
	private boolean flip;
	private boolean pressed;
	
	private int value;
	
	private Animation icon;
	
	private Direction direction;
	
	public ArrowButton(Rectangle area, Direction direction) {
		super(area);
		this.icon = new Animation(AssetManager.getTexture("arrowButton"));
		
		this.icon.setPosition(area.getX(), area.getY());
		this.icon.setSize(area.getWidth(), area.getHeight());
		
		this.direction = direction;
		
		this.flip = (direction == Direction.DOWN);
	}
	
	public void update() {
		super.update();
		
		if(pressed) value = 0;
		
		if(wasPressed() && !pressed) {
			value = (direction == Direction.DOWN) ? -1 : 1;
			pressed = true;
		}
		
		if(!isPressed() && pressed) {
			pressed = false;
			value = 0;
		}

		icon.setFlip(false, flip);
	}
	
	public void draw(SpriteBatch batch) {
		icon.draw(batch);
	}
	
	public int getValue() {
		return this.value;
	}
	
	public Direction getDirection() {
		return this.getDirection();
	}
}
