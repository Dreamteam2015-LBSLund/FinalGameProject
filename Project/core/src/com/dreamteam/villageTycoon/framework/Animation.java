package com.dreamteam.villageTycoon.framework;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	
	public Animation(Sprite sprite, float maxAnimationTime, int maxFrame, int minFrame, boolean vertical){
		super(sprite);
		setAnimation(maxAnimationTime, maxFrame, minFrame, vertical);
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
			this.setRegion(minFrame*this.getRegionWidth() + currentFrame*this.getRegionWidth(), 
					this.getRegionY(), this.getRegionWidth(), this.getRegionHeight());
		else
			this.setRegion(this.getRegionX(), minFrame*this.getRegionHeight()+ currentFrame*this.getRegionWidth(), 
					this.getRegionWidth(), this.getRegionHeight());
		
		if(animationDone()) 
			currentFrame = 0;
		
		if(animationTime >= maxAnimationTime) {
			currentFrame += 1;
			animationTime = 0;
		}
	}
	
	public boolean animationDone() {
		return currentFrame == maxFrame;
	}
	
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
	
	public void setAnimationCount(int animationCount) {
		this.animationTime = animationCount;
	}
}
