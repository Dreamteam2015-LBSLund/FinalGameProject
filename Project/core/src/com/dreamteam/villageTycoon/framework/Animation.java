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
	
	private boolean animationIsDone;
	
	private float orginalRegionX;
	
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
		this.orginalRegionX = this.getRegionX();
	}
	
	public Animation(Sprite sprite, float maxAnimationTime, int maxFrame, boolean vertical){
		super(sprite);
		setAnimation(maxAnimationTime, maxFrame, vertical);
		this.orginalRegionX = this.getRegionX();
	}
	
	public Vector2 getSize() {
		return this.getSize();
	}
	
	public void setAnimation(float maxAnimationTime, int maxFrame, boolean vertical) {
		this.maxAnimationTime = maxAnimationTime;
		this.maxFrame = maxFrame;
		this.minFrame = this.getRegionX() / this.getRegionWidth();
		this.vertical = vertical;
		this.orginalRegionX = this.getRegionX();
	}
	
	public int frame(int frame, int size) {
		return 1+frame+size*frame;
	}
	
	public void animate(float deltaTime) {
		animationTime += 2*deltaTime;
		
		if(!vertical)
			this.setRegion(frame(minFrame, this.getRegionWidth()) + currentFrame*this.getRegionWidth()+1+currentFrame, 
					this.getRegionY(), this.getRegionWidth(), this.getRegionHeight());
		else
			this.setRegion(this.getRegionX(), minFrame*this.getRegionHeight()+ currentFrame*this.getRegionWidth(), 
					this.getRegionWidth(), this.getRegionHeight());
		
		
		if(currentFrame == maxFrame+1) {
			currentFrame = 0;
			animationIsDone = true;
		}
		
		if(animationTime >= maxAnimationTime) {
			currentFrame += 1;
			if(currentFrame == maxFrame) 
				currentFrame = 0;
			animationTime = 0;
		}
	}
	
	public boolean getAnimationIsDone() {
		return this.animationIsDone;
	}
	
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
	
	public int getMaxFrame() {
		return this.maxFrame;
	}
	
	public int getMinFrame() {
		return this.minFrame;
	}
	
	public int getCurrentFrame() {
		return this.currentFrame;
	}
	
	public void setAnimationTime(float animationTime) {
		this.animationTime = animationTime;
	}
}