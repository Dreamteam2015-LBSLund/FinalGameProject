package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class ProgressBar extends UiElement {

	public float progress;
	public String hint;
	
	public ProgressBar(Rectangle area) {
		super(area);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(AssetManager.getTexture("light-pixel"), getArea().getX(), getArea().getY(), getArea().getWidth(), getArea().getHeight());
		batch.draw(AssetManager.getTexture("dark-pixel"), getArea().getX(), getArea().getY(), getArea().getWidth() * progress, getArea().getHeight());
		if (getIsHover()) AssetManager.font.draw(batch, hint, getScene().getUiMouse().x, getScene().getUiMouse().y);
	}
}
