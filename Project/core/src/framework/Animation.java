package framework;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Animation extends Sprite {
	private int currentFrame;
	private int animationCount;
	private int maxAnimationCount;
	private int maxFrame;
	private int minFrame;
	private boolean vertical;
	
	private Sprite sprite;
	
	public Animation(Sprite sprite){
		this.sprite = sprite;
	}
	
	public Animation(Sprite sprite, int maxAnimationCount, int maxFrame, int minFrame, boolean vertical){
		this.sprite = sprite;
		setAnimation(maxAnimationCount, maxFrame, minFrame, vertical);
	}
	
	public void setAnimation(int maxAnimationCount, int maxFrame, int minFrame, boolean vertical) {
		this.maxAnimationCount = maxAnimationCount;
		this.maxFrame = maxFrame;
		this.minFrame = minFrame;
		this.vertical = vertical;
	}
	
	public void animate() {
		animationCount += 1;
		
		if(!vertical)
			sprite.setRegion(minFrame*sprite.getRegionWidth() + currentFrame*sprite.getRegionWidth(), 
					sprite.getRegionY(), sprite.getRegionWidth(), sprite.getRegionHeight());
		else
			sprite.setRegion(sprite.getRegionX(), minFrame*sprite.getRegionHeight()+ currentFrame*sprite.getRegionWidth(), 
					sprite.getRegionWidth(), sprite.getRegionHeight());
		
		if(currentFrame == maxFrame)
			currentFrame = 0;
		
		if(animationCount >= maxAnimationCount) {
			currentFrame += 1;
			animationCount = 0;
		}
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
	
	public void setAnimationCount(int animationCount) {
		this.animationCount = animationCount;
	}
}
