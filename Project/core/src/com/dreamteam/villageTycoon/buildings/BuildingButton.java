package com.dreamteam.villageTycoon.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.userInterface.TextButton;
import com.dreamteam.villageTycoon.userInterface.UiElement;
import com.dreamteam.villageTycoon.utils.Debug;

public class BuildingButton extends TextButton {
	private BuildingType type;
	
	public BuildingButton(BuildingType type, Rectangle position) {
		super(position, type.getName());
		this.type = type;
		getArea().set(position.getX(), position.getY(), position.getWidth(), 0);
		setFont(AssetManager.smallFont);
	}
	
	public void draw(SpriteBatch batch) {
		if (isHover()) {
			batch.draw(type.getSprite(), getArea().getX() + getArea().getWidth(), getArea().getY());
			AssetManager.smallFont.draw(batch, type.getInfoText(), getArea().getX() + getArea().getWidth(), getArea().getY());
		}
		super.draw(batch);
	}

	public BuildingType getType() {
		return type;
	}
}
