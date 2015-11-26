package com.dreamteam.villageTycoon.characters;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.effects.Explosion;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Point;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.projectiles.Projectile;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.utils.PathFinder;

public class Character extends GameObject {
	private boolean selected;
	
	private int health;
	
	private Animation deathAnimation;
	
	private ArrayList<Vector2> path;
	
	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);
	
	private Animation selectedSign;
	
	private boolean showInventory;
	private boolean isInBuilding;
	private City city;
	private Building building;
	
	protected Vector2 lastPathTarget;
	
	public Character(Vector2 position, Animation sprite, Animation deathAnimation, City city) {
		super(position, new Vector2(1, 1), sprite);
		
		selectedSign = new Animation(AssetManager.getTexture("test"), new Vector2(0.3f, 0.3f), new Color(0, 0, 1, 0.5f));
		this.deathAnimation = deathAnimation;
		health = 1;
		this.city = city;
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
		
		followPath(deltaTime);
		
		for(GameObject g : getScene().getObjects()) {
			if(g instanceof Projectile) {
				if(this != ((Projectile)g).getOwner() && this.city != ((Projectile)g).getOwner().getCity()) {
					if(g.getHitbox().collision(getHitbox())) {
						onHit(((Projectile)g));
						getScene().removeObject(g);
					}
				}
			}
			
			if(g instanceof Explosion) {
				if(getPosition().sub(g.getPosition()).len() <= ((Explosion)g).getRadius() && g.getSprite().getCurrentFrame() == 0 && g.getSprite().getAnimationTime() <= 0) {
					health -= ((Explosion) g).getMaxDamege();
				}
			}
		}
	}
	
	public void onHit(Projectile projectile) {
		health -= projectile.getDamege();
	}
	
	public City getCity() {
		return city;
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
	
	// called when the player tells a character to go somewhere
	public void onPlayerInput(Vector2 destination) {
		setPath(destination);
	}
	
	public boolean getShowInventroy() {
		return showInventory;
	}
	
	public void setShowInventory(boolean showInventory) {
		this.showInventory = showInventory;
	}
	
	public void setIsInBuilding(boolean isInBuilding) {
		this.isInBuilding = isInBuilding;
	}
	
	public boolean getIsInBuilding() {
		return this.isInBuilding;
	}
	
	public void setBuilding(Building building) {
		this.building = building;
	}
	
	public Building getBuilding() {
		return this.building;
	}
	
	protected void followPath(float deltaTime) {
		//Debug.print(this, "following path");
		if (path != null && !path.isEmpty()) {
			Vector2 next = path.get(0);
			Debug.print(this, "next node: " + next + ". distance " + (next.cpy().sub(getPosition()).len()));
			if (stepTowards(next, 1 * deltaTime)) {
				//Debug.print(this, "arrived at node");
				path.remove(0);
				Debug.print(this, "at node, removing");
				//Debug.print(this, path.size() + " nodes left");
			}
		} else {
			Debug.print(this, "path is null or empty, " + path);
			if (lastPathTarget != null) Debug.print(this, "distance to target: " + distanceTo(lastPathTarget));
		}
	}
	
	protected boolean isAtPathEnd() {
		//Debug.print(this, "distance to path target = " + distanceTo(lastPathTarget));
		return distanceTo(lastPathTarget) < .2f;
	}
	
	protected boolean stepTowards(Vector2 v, float speed) {
		Vector2 delta = v.cpy().sub(getPosition().cpy());
		delta.clamp(0, speed);
		setPosition(getPosition().add(delta));
		Debug.print(this, "stepped towards " + v + ", new position = " + getPosition() + ", distance " + delta.len());
		return delta.len() < .01 * speed;
	}
	
	protected ArrayList<Vector2> getPath() {
		return path;
	}
	
	protected void setPath(Vector2 target) {
		setPath(target, null);
	}
	
	// set path if not already set
	protected void setPath(Vector2 target, Building ignore) {
		if (lastPathTarget == target && path != null) {
			Debug.print(this, "lastPath = target, returning"); 
			if (!path.isEmpty()) {
				Debug.print(this, "path is not empty, returning");
				return;
			} else {
				Debug.print(this, "path is empty, setting new");
			}
			/*
			if (path != null) {
				Debug.print(this, "lastPath != null");
				if (distanceTo(target) < .3f) {
					Debug.print(this, "path is empty and target is my location");
					return;
				} else {
					Debug.print(this, "Distance to target > .3, getting new path");
				}
			}*/
		}
		Debug.print(this, "setting a new path");
		lastPathTarget = target;
		path = new PathFinder(getPosition(), target, ((TestScene) (getScene())).getMap().getTiles(), false, this).getPath(true, ignore);		
	}
}
