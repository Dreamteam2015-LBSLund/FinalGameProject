package com.dreamteam.villageTycoon.frameworkTest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Scene;

public class TestObject extends GameObject {

	public TestObject(TextureRegion region) {
		super(new Vector2(0, 0), new Vector2(3, 3), new Animation(region));
		setDepth(10);
	}
	
	public void update(float deltaTime) {
		//setPosition(getPosition().add(.1f, 0));
	}
	
	public void drawUi(SpriteBatch batch) {
		batch.draw(getSprite().getTexture(), 5, 0, 1, 1);
	}
}
