package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
	
	private float orginalSize;
	private float size;
	
	private Color color;
	
	public ArrowButton(Rectangle area, Direction direction) {
		super(area);
		this.icon = new Animation(AssetManager.getTexture("arrowButton"));
		
		this.icon.setPosition(area.getX(), area.getY());
		this.icon.setSize(area.getWidth(), area.getHeight());
		this.orginalSize = area.getWidth();
		this.size = orginalSize;
		this.color = new Color(1f, 1f, 1f, 1f);
		
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
		
		this.icon.setSize(size, size);
		this.icon.setColor(color);
		
		if(isHover()) {
			size = MathUtils.lerp(size, orginalSize/1.2f, 0.1f);
			color.lerp(new Color(0.5f, 0.5f, 0.5f, 1), 0.1f);
		} else {
			size = MathUtils.lerp(size, orginalSize, 0.1f);
			color.lerp(Color.WHITE, 0.1f);
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
