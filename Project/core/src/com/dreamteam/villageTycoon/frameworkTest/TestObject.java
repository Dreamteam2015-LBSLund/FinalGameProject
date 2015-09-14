package com.dreamteam.villageTycoon.frameworkTest;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.utils.PathFinder;

public class TestObject extends GameObject {

	ArrayList<Vector2> path;
	
	public TestObject(TextureRegion region) {
		super(new Vector2(.25f, .25f), new Vector2(.5f, .5f), new Animation(region));
		setDepth(10);
	}
	
	public void update(float deltaTime) {
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			path = new PathFinder(getPosition(), getScene().getWorldMouse(), ((TestScene)getScene()).getMap().getTiles()).getPath();
		}
		 
		if (path != null && !path.isEmpty()) {
			Vector2 next = path.get(0);
			stepTowards(next, .05f);
			if (distanceTo(next) < .1f) {
				path.remove(0);
			}
		}
	}
	
	private void stepTowards(Vector2 v, float speed) {
		Vector2 delta = v.cpy().sub(getPosition().cpy());
		delta.clamp(0, speed);
		setPosition(getPosition().add(delta));
	}
	
	private float distanceTo(Vector2 v) {
		Vector2 delta = v.cpy().sub(getPosition().cpy());
		float f = delta.len();
		System.out.println("distance to " + v + ": " + f);
		return f;
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		if (path != null) { 
			for (Vector2 v : path) {
				batch.draw(AssetManager.getTexture("test"), v.x, v.y, .2f, .2f);
			}
			//if (path.size() > 0) batch.draw(AssetManager.getTexture("test"), path.get(0).x, path.get(0).y, .2f, .2f);
		}
	}
}
