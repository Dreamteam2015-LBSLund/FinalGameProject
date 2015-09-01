package framework;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Animation extends Sprite {
	private int currentFrame;
	private int animationCount;
	private int maxAnimationCount;
	private int maxFrame;
	private int minFrame;
	private boolean vertical;
	
	public Animation(Texture sprite){
		super(sprite);
	}
	
	public Animation(Sprite sprite, int maxAnimationCount, int maxFrame, int minFrame, boolean vertical){
		super(sprite);
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
			this.setRegion(minFrame*this.getRegionWidth() + currentFrame*this.getRegionWidth(), 
					this.getRegionY(), this.getRegionWidth(), this.getRegionHeight());
		else
			this.setRegion(this.getRegionX(), minFrame*this.getRegionHeight()+ currentFrame*this.getRegionWidth(), 
					this.getRegionWidth(), this.getRegionHeight());
		
		if(currentFrame == maxFrame)
			currentFrame = 0;
		
		if(animationCount >= maxAnimationCount) {
			currentFrame += 1;
			animationCount = 0;
		}
	}
	
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
	
	public void setAnimationCount(int animationCount) {
		this.animationCount = animationCount;
	}
}
