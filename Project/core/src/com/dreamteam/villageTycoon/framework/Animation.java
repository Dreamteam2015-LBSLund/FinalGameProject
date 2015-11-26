package com.dreamteam.villageTycoon.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Animation extends Sprite {
	private int currentFrame;
	private int maxFrame;
	private int minFrame;
	
	private float animationTime;
	private float maxAnimationTime;
	
	private boolean vertical;
	
	public Animation(Texture sprite){
		super(sprite);
	}
	
	public Animation(TextureRegion region){
		super(new Sprite(region));
	}
	
	public Animation(Sprite sprite) {
		super(sprite);
	}
	
	public Animation(TextureRegion region, Vector2 size, Color color) {
		super(new Sprite(region));
		setSize(size.x, size.y);
		setColor(color);
	}
	
	public Animation(Sprite sprite, float maxAnimationTime, int maxFrame, int minFrame, boolean vertical){
		super(sprite);
		setAnimation(maxAnimationTime, maxFrame, minFrame, vertical);
	}
	
	public Vector2 getSize() {
		return this.getSize();
	}
	
	public void setAnimation(float maxAnimationTime, int maxFrame, int minFrame, boolean vertical) {
		this.maxAnimationTime = maxAnimationTime;
		this.maxFrame = maxFrame;
		this.minFrame = minFrame;
		this.vertical = vertical;
	}
	
	public void animate(float deltaTime) {
		animationTime += deltaTime;
		
		if(!vertical)
			this.setRegion(minFrame*this.getRegionWidth() + currentFrame*this.getRegionWidth() + 1 + currentFrame, 
					this.getRegionY(), this.getRegionWidth(), this.getRegionHeight());
		else
			this.setRegion(this.getRegionX(), minFrame*this.getRegionHeight() + 1 + currentFrame*this.getRegionHeight(), 
					this.getRegionWidth(), this.getRegionHeight());
		
		if(animationDone()) 
			currentFrame = 0;
		
		if(animationTime >= maxAnimationTime) {
			currentFrame += 1;
			animationTime = 0;
		}
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}
	
	public boolean animationDone() {
		return currentFrame == maxFrame;
	}
	
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
	
	public void setAnimationTime(float animationTime) {
		this.animationTime = animationTime;
	}
	
	public float getAnimationTime() {
		return animationTime;
	}
}
