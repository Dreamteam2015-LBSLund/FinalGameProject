package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class DestroyBuildingButton extends UiElement {
	private String text;
	
	Vector2 position;
	
	public DestroyBuildingButton(Vector2 position) {
		super(new Rectangle(position.x, position.y, 300, 50));
		text = "Destroy this building";
		this.position = position;
	}
	
	public void update(Building building) {
		super.update();
		
		if(this.isPressed()) {
			building.getScene().removeObject(building);
		}
	}

	public void draw(SpriteBatch batch) {
		AssetManager.font.draw(batch, text, position.x, position.y + getArea().getHeight());
	}
}
