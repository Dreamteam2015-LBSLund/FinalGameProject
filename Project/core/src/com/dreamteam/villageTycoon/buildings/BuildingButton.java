package com.dreamteam.villageTycoon.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.utils.Debug;

public class BuildingButton {
	private BuildingType type;
	private Rectangle position;
	
	public boolean clicked;
	
	public BuildingButton(BuildingType type, Rectangle position) {
		this.type = type;
		this.position = position;
	}
	
	public void update(Scene scene) {
		clicked = false;
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			Debug.print(this, "mouse button is pressed");
			if (position.collision(scene.getUiMouse())) {
				clicked = true;
				Debug.print(this, "i was clicked");
			}
		}
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(type.getSprite(), position.getX(), position.getY());
		AssetManager.font.draw(batch, type.getName(), position.getX(), position.getY());
	}

	public BuildingType getType() {
		return type;
	}
}
