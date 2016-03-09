package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.Game;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;

public class SetSceneButton extends TextButton {
	
	private Scene targetScene;
	
	public SetSceneButton(Vector2 position, String text, Scene targetScene) {
		super(new Rectangle(position.x, position.y, 300, 50), text);
		this.targetScene = targetScene;
	}
	
	
	public void onClick() {
		Game.setScene(targetScene);
		System.out.println("setting scene");
	}
}
