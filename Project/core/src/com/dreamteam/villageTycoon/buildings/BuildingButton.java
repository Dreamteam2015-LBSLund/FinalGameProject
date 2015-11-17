package com.dreamteam.villageTycoon.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.userInterface.UiElement;
import com.dreamteam.villageTycoon.utils.Debug;

public class BuildingButton extends UiElement {
	private BuildingType type;
	
	public BuildingButton(BuildingType type, Rectangle position) {
		super(position);
		this.type = type;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(AssetManager.getTexture("button"), getArea().getX(), getArea().getY(), getArea().getWidth(), getArea().getHeight());
		if (isHover()) {
			batch.draw(type.getSprite(), getNewMouse().x, getNewMouse().y);
		}
		AssetManager.font.draw(batch, type.getName(), getArea().getX(), getArea().getY() + getArea().getHeight());
	}

	public BuildingType getType() {
		return type;
	}
}
