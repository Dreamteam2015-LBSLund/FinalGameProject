package com.dreamteam.villageTycoon.characters;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Point;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.utils.PathFinder;

public class Character extends GameObject {
	private boolean selected;
	
	private int health;
	private Animation deathAnimation;
	
	private ArrayList<Vector2> path;
	
	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);
	
	private Animation selectedSign;
	
	public Character(Vector2 position, Animation sprite, Animation deathAnimation) {
		super(position, sprite);
		this.setSize(new Vector2(1, 1));
		selectedSign = new Animation(AssetManager.getTexture("test"), new Vector2(0.3f, 0.3f), new Color(0, 0, 1, 0.5f));
		this.deathAnimation = deathAnimation;
		health = 1;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		hitbox = new Rectangle(new Vector2(getPosition().x-getSprite().getScaleX()/2, getPosition().y-getSprite().getScaleY()/2), getSize());

		selectedSign.setPosition(getPosition().x-selectedSign.getScaleX()/6, getPosition().y+selectedSign.getScaleY()/2);
		
		if(health <= 0) {
			if(getSprite() != deathAnimation) setSprite(deathAnimation);
			if(getSprite().animationDone()) getScene().removeObject(this);
			getSprite().animate(deltaTime);
		}
		
		followPath();
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		if(selected) selectedSign.draw(batch);
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getHealth() {
		return health;
	}
	
	public boolean getDead() {
		return health <= 0;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean getSelected() {
		return selected;
	}

	protected void followPath() {
		if (path != null && !path.isEmpty()) {
			Vector2 next = path.get(0);
			stepTowards(next, .05f);
			if (distanceTo(next) < .1f) {
				path.remove(0);
			}
		}
	}
	
	protected void stepTowards(Vector2 v, float speed) {
		Vector2 delta = v.cpy().sub(getPosition().cpy());
		delta.clamp(0, speed);
		setPosition(getPosition().add(delta));
	}
	
	protected ArrayList<Vector2> getPath() {
		return path;
	}
	
	protected void setPath(Vector2 target) {
		path = new PathFinder(getPosition(), target, ((TestScene) (getScene())).getMap().getTiles()).getPath();
	}
}
