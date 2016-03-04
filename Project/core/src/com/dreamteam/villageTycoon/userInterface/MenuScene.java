package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.game.GameScene;

public class MenuScene extends Scene {
	private boolean showTitleScreen;
	
	private Sprite titleScreen;
	
	private MenuButton startGameButton;
	
	public MenuScene() {
		showTitleScreen = true;
		
		titleScreen = new Sprite(new Texture("textures/titlescreen.png"));
		
		startGameButton = new MenuButton(new Vector2(0, 0), "Start game", new GameScene());
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(showTitleScreen) {
			if(Gdx.input.isKeyJustPressed(Keys.SPACE)) showTitleScreen = false;
		} else {
			startGameButton.update();
		}
	}
	
	public void drawUi(SpriteBatch batch) {
		super.drawUi(batch);
		
		if(showTitleScreen) {
			titleScreen.draw(batch);
		} else {
			startGameButton.draw(batch);
		}
	}
}
