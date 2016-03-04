package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.Game;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;

public class MenuButton extends UiElement {
	private Vector2 position;
	
	private String text;
	
	private Scene scene;
	
	public MenuButton(Vector2 position, String text, Scene scene) {
		super(new Rectangle(position.x, position.y, 300, 50));
		
		this.position = position;
		this.text = text;
		
		this.scene = scene;
	}
	
	public void update() {
		if(isPressed()) {
			Game.setScene(scene);
		}
		
		super.update();
	}

	public void draw(SpriteBatch batch) {
		AssetManager.font.draw(batch, text, getArea().getX(), getArea().getY() + getArea().getHeight());
	}
}
