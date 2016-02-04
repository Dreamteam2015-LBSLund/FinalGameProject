package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class OpenBuildingPlacerButton extends UiElement {
	private Vector2 position;
	
	private String text;
	
	private boolean pressed;
	
	public OpenBuildingPlacerButton(Vector2 position) {
		super(new Rectangle(position.x, position.y, 500, 30));
		this.position = position;
		
		text = "BUILDING PLACER";
	}
	
	public void update(PlayerController playerController) {
		super.update();
		
		if(wasPressed()) {
			playerController.openBuildingPlacer();
		}
	}
	
	public void draw(SpriteBatch batch) {
		AssetManager.font.draw(batch, text, position.x, position.y + getArea().getHeight());
	}
}
