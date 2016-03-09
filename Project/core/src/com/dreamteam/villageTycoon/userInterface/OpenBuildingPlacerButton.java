package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.characters.Controller;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class OpenBuildingPlacerButton extends TextButton {
	
	private PlayerController playerController;
	
	public OpenBuildingPlacerButton(Vector2 position, PlayerController playerController2) {
		super(new Rectangle(position.x, position.y, 500, 30), "place building");
		this.playerController = playerController2;
	}
	
	public void onClick() {
		playerController.openBuildingPlacer();
	}
}
