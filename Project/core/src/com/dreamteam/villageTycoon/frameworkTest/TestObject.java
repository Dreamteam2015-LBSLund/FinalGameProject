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
			stepTowards(path.get(0));
			if (distanceTo(path.get(0)) < .1f) path.remove(0);
		}
	}
	
	private void stepTowards(Vector2 v) {
		Vector2 delta = v.sub(getPosition());
		delta.nor();
		setPosition(getPosition().add(delta));
	}
	
	private float distanceTo(Vector2 v) {
		Vector2 delta = v.sub(getPosition());
		return delta.len2();
	}
	
	public void drawUi(SpriteBatch batch) {
		batch.draw(getSprite().getTexture(), 5, 0, 1, 1);
	}
}
