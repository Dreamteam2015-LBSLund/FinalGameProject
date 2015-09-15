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
import com.dreamteam.villageTycoon.characters.Character;

public class TestObject extends Character {
	public TestObject(TextureRegion region) {
		super(new Vector2(.25f, .25f), new Animation(region), new Animation(region));
		setDepth(10);
	}
	
	public void update(float deltaTime) {
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			setPath(getScene().getWorldMouse());
		}
		followPath();
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		if (getPath() != null) { 
			for (Vector2 v : getPath()) {
				batch.draw(AssetManager.getTexture("test"), v.x, v.y, .2f, .2f);
			}
		}
	}
}
