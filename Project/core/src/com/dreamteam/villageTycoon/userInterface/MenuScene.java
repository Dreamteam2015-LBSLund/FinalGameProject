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
	private Sprite background;
	
	private SetSceneButton startGameButton;
	
	public MenuScene() {
		showTitleScreen = true;
		
		titleScreen = new Sprite(new Texture("textures/titlescreen.png"));
		titleScreen.setSize(Gdx.graphics.getWidth()*2, Gdx.graphics.getHeight()*2);
		titleScreen.setPosition(-Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		
		background = new Sprite(new Texture("textures/menuBackground.png"));
		background.setSize(Gdx.graphics.getWidth()*2, Gdx.graphics.getHeight()*2);
		background.setPosition(-Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		
		startGameButton = new SetSceneButton(new Vector2(0, 0), "Start game", new GameScene());
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
			background.draw(batch);
			startGameButton.draw(batch);
		}
	}
}
