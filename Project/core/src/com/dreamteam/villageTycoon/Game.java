package com.dreamteam.villageTycoon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import framework.Scene;
import frameworkTest.TestScene;

public class Game extends ApplicationAdapter {
	SpriteBatch batch, uiBatch;
	
	private Scene currentScene;
	
	public Scene getScene() { return currentScene; }
	public void setScene(Scene scene) {
		currentScene.onPause();
		currentScene = scene;
		currentScene.onResume();
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		currentScene = new TestScene();
	}
	
	public void update() {
		currentScene.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render () {
		update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.setProjectionMatrix(currentScene.getCamera().combined);
		currentScene.draw(batch);
		batch.setProjectionMatrix(currentScene.getUiCamera().combined);
		currentScene.drawUi(batch);
		batch.end();
	}
}
