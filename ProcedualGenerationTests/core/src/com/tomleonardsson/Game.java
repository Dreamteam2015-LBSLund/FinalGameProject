package com.tomleonardsson;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	
	public static World testWorld;
	
	public static ArrayList<Walker> walkers = new ArrayList<Walker>();
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		AssetManager.load();
		testWorld = new World(Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/8);
		//Globals.floodFill(30, 30, 3, 1);
		//Globals.addVillege(20, 20, 5);
		testWorld.create();
		Globals.line(20, 20, -1, 1, 3);
		//walkers.add(new Walker(20, 20, 2, 10000, 3, 150));
	}
	
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		if(Gdx.input.isKeyPressed(Keys.SPACE)){
			testWorld.create();
		}
		
		for(Walker w : walkers){
			w.update();
		}
		
		for(int i = 0; i < walkers.size(); i++){
			if(walkers.get(i).destroy) walkers.remove(i);
		}
	}
	
	@Override
	public void render () {
		update();
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		testWorld.draw(batch);
		batch.end();
	}
}
