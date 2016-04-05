package com.dreamteam.villageTycoon.userInterface;

import java.awt.Button;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.game.AIFaceoffConfig;
import com.dreamteam.villageTycoon.game.GameScene;
import com.dreamteam.villageTycoon.game.PlayerVsAdvancedConfig;
import com.dreamteam.villageTycoon.game.PlayerVsSimpleConfig;

public class MenuScene extends Scene {
	private boolean showTitleScreen;
	
	private Sprite titleScreen;
	private Sprite background;
	
	private ArrayList<SetSceneButton> buttons;
	
	public MenuScene() {
		showTitleScreen = true;
		
		titleScreen = new Sprite(new Texture("textures/titlescreen.png"));
		titleScreen.setSize(Gdx.graphics.getWidth()*2, Gdx.graphics.getHeight()*2);
		titleScreen.setPosition(-Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		
		background = new Sprite(new Texture("textures/menuBackground.png"));
		background.setSize(Gdx.graphics.getWidth()*2, Gdx.graphics.getHeight()*2);
		background.setPosition(-Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		
		buttons = new ArrayList<SetSceneButton>();
		buttons.add(new SetSceneButton(new Vector2(0, -100), "Start player vs 'simple ai' game (harder)", new GameScene(new PlayerVsSimpleConfig())));
		buttons.add(new SetSceneButton(new Vector2(0, -200), "Start player vs 'advanced ai' game (easier)", new GameScene(new PlayerVsAdvancedConfig())));
		buttons.add(new SetSceneButton(new Vector2(0, -300), "Start 'advanced ai' vs 'simple ai' game", new GameScene(new AIFaceoffConfig())));
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(showTitleScreen) {
			if(Gdx.input.isKeyJustPressed(Keys.SPACE)) showTitleScreen = false;
		} else {
			for (SetSceneButton b : buttons) b.update();
		}
	}
	
	public void drawUi(SpriteBatch batch) {
		super.drawUi(batch);
		
		if(showTitleScreen) {
			titleScreen.draw(batch);
		} else {
			background.draw(batch);
			for (SetSceneButton b : buttons) b.draw(batch);
		}
	}
}
