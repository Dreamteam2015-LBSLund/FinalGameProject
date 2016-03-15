package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class DestroyBuildingButton extends TextButton {
	
	private Building building;
	
	public DestroyBuildingButton(Vector2 position, Building building ) {
		super(new Rectangle(position.x, position.y, 400, 50), "Destroy building");
		this.building = building;
	}
	
	public void onClick() {
		building.getScene().removeObject(building);
	}
}
