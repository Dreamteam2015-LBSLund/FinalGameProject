package com.dreamteam.villageTycoon.characters;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.effects.Explosion;
import com.dreamteam.villageTycoon.effects.Explosion.Type;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Point;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.game.GameScene;
import com.dreamteam.villageTycoon.map.Prop;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.projectiles.Projectile;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.utils.PathFinder;

public class Character extends GameObject {
	final float HUNGER_FORCE = 1;
	
	private boolean selected;
	
	private float health;
	
	private Animation deathAnimation;
	
	private ArrayList<Vector2> path;
	
	private Rectangle hitbox = new Rectangle(0, 0, 0, 0);
	
	private Animation selectedSign;
	
	private boolean showInventory;
	private boolean isInBuilding;
	private City city;
	private Building building;
	
	protected Vector2 lastPathTarget;
	
	private float onFireTime;
	private float onFireIntervall;
	
	private float amountFull;
	private float maxFull;
	
	private int countFireDamegeTime;
	private int maxCountFireDamegeTime;
	
	private boolean needToEat;
	private boolean flip;
	
	public Character(Vector2 position, Animation sprite, Animation deathAnimation, City city) {
		super(position, new Vector2(1, 1), sprite);
		//this.getSprite().setAnimation(0.1f, 4, false);
		
		selectedSign = new Animation(AssetManager.getTexture("test"), new Vector2(0.3f, 0.3f), new Color(0, 0, 1, 0.5f));
		this.deathAnimation = deathAnimation;
		this.health = 3;
		this.amountFull = maxFull = 1000;
		this.city = city;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		this.getSprite().setFlip(flip, false);
		
		this.setDepthBasedOnPosition();
		
		if(getSprite().getMaxFrame() > 0) getSprite().animate(deltaTime);
		
		hitbox = new Rectangle(new Vector2(getPosition().x-getSprite().getScaleX()/2, getPosition().y-getSprite().getScaleY()/2), getSize());

		selectedSign.setPosition(getPosition().x-selectedSign.getScaleX()/6, getPosition().y+selectedSign.getScaleY()/2);
		
		if(getCity().getController() instanceof PlayerController) this.getMovmentVector(deltaTime);
		
		if(health <= 0) {
			getScene().addObject(new Corpse(this.getPosition(), deathAnimation));
			getScene().removeObject(this);
		}
		
		if(amountFull > 0) {
			amountFull -= HUNGER_FORCE * deltaTime;
		} else {
			health -= 0.3f * deltaTime;
		}
		
		followPath(deltaTime);
		
		for(GameObject g : getScene().getObjects()) {
			if(g instanceof Projectile) {
				if(this != ((Projectile)g).getOwner() && this.city != ((Projectile)g).getOwner().getCity()) {
					if(g.getHitbox().collision(getHitbox())) {
						onHit(((Projectile)g));
						((Projectile)g).onHit();
					}
				}
			}
			
			if(g instanceof Explosion) {
				if(this.distanceTo(g.getPosition()) <= ((Explosion)g).getRadius() && g.getSprite().getCurrentFrame() == 0) {
					health -= ((Explosion) g).getMaxDamege();
					if(((Explosion)g).getType() == Type.FIRE) {
						countFireDamegeTime = 1;
					}
				}
			}
		}
		
		if(this.countFireDamegeTime > 0) onFire(deltaTime);
	}
	
	public void onFire(float deltaTime) {
		onFireTime += 1 * deltaTime;
		
		if(onFireTime >= onFireIntervall) {
			health -= 1;
			countFireDamegeTime += 1;
			onFireTime = 0;
		}
		
		if(countFireDamegeTime >= maxCountFireDamegeTime) {
			countFireDamegeTime = 0;
		}
		
		for(GameObject g : getScene().getObjects()) {
			if(g instanceof Character) {
				if(g.getHitbox().collision(this.getHitbox())) {
					((Character) g).setCountFireDamegeTime(1);
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
		if(selected) { 
			selectedSign.draw(batch);
		}
	}
	
	public void setHealth(float health) {
		this.health = health;
	}
	
	public float getHealth() {
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
	
	public float getOnFireTime() {
		return onFireTime;
	}

	public float getOnFireIntervall() {
		return onFireIntervall;
	}

	public int getCountFireDamegeTime() {
		return countFireDamegeTime;
	}

	public int getMaxCountFireDamegeTime() {
		return maxCountFireDamegeTime;
	}

	public void setOnFireTime(float onFireTime) {
		this.onFireTime = onFireTime;
	}

	public void setOnFireIntervall(float onFireIntervall) {
		this.onFireIntervall = onFireIntervall;
	}

	public void setCountFireDamegeTime(int countFireDamegeTime) {
		this.countFireDamegeTime = countFireDamegeTime;
	}

	public void setMaxCountFireDamegeTime(int maxCountFireDamegeTime) {
		this.maxCountFireDamegeTime = maxCountFireDamegeTime;
	}
	
	public void setDeathAnimation(Animation deathAnimation) {
		this.deathAnimation = deathAnimation;
	}
	
	public float getAmountFull() {
		return this.amountFull;
	}
	
	public float getMaxFull() {
		return this.maxFull;
	}
	
	public void setAmountFull(float amountFull) {
		this.amountFull = amountFull;
	}
	
	protected void followPath(float deltaTime) {
		//Debug.print(this, "following path");
		if (path != null && !path.isEmpty()) {
			Vector2 next = path.get(0);
			//Debug.print(this, "next node: " + next + ". distance " + (next.cpy().sub(getPosition()).len()));
			if (stepTowards(next, 1 * deltaTime)) {
				//Debug.print(this, "arrived at node");
				path.remove(0);
				//Debug.print(this, "at node, removing");
				//Debug.print(this, path.size() + " nodes left");
			}
		} else {
			//Debug.print(this, "path is null or empty, " + path);
			//if (lastPathTarget != null) Debug.print(this, "distance to target: " + distanceTo(lastPathTarget));
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
		//Debug.print(this, "stepped towards " + v + ", new position = " + getPosition() + ", distance " + delta.len());
		return delta.len() < .01 * speed;
	}
	
	protected ArrayList<Vector2> getPath() {
		return path;
	}
	
	public void setPath(Vector2 target) {
		setPath(target, null);
	}
	
	// set path if not already set
	protected void setPath(Vector2 target, Building ignore) {
		if (lastPathTarget == target && path != null) {
			//Debug.print(this, "lastPath = target, returning"); 
			if (!path.isEmpty()) {
				//Debug.print(this, "path is not empty, returning");
				return;
			} else {
				//Debug.print(this, "path is empty, setting new");
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
		//System.out.println("scene: " + getScene());// + ", map: " + ((TestScene)getScene()).getMap());
		path = new PathFinder(getPosition(),
				target,
				((GameScene) (getScene())).getMap().getTiles(), 
				false, 
				this)
			.getPath(true, ignore);		
	}
	
	public boolean hasPath() {
		return path != null;
	}
	
	public Vector2 getMovmentVector(float deltaTime) {
		Vector2 delta = Vector2.Zero;
		
		if (path != null && !path.isEmpty()) {
			Vector2 next = path.get(0);
			delta = next.cpy().sub(getPosition().cpy());
			System.out.println(delta);
			delta.clamp(0, 1*deltaTime);
		}
		
		return delta;
	}
	
// finds the resource and puts it in the inventory. returns true if done
	public boolean findResource(Resource r, Inventory<Resource> inventory) {
		print("finding resource " + r.getName());
		if (inventory != null && inventory.count(r) > 0) {
			print("is already in inventory");
			return true;
		} else {
			Object t = getCity().findResource(r, getPosition()); // this doesn't need to happen every frame
			if (t != null) {
				if (t instanceof GameObject) {
					GameObject o = (GameObject)t;
					Vector2 target = o.getPosition();
					print("found resource at " + target + ", setting path");
				
					setPath(target);
					
					if (isAtPathEnd()) {
						print("got to end of path");
						if (t instanceof Building) {
							print("thing is building, taking resource");
							Building b = (Building)t;
							if (b.getOutputInventory().count(r) > 0) {
								b.getOutputInventory().remove(r, 1);
								if (inventory != null) inventory.add(r, 1);
								//Debug.print(this, "has resource, putting");
								return true;
							}
						} else if (t instanceof Prop) {
							print("thing is prop, destroying it");
							if (((Prop)t).getType().getResource() == r) {
								getScene().removeObject(o);
								if (inventory != null) inventory.add(r, 1);
								return true;
							} else {
								print("Wrong prop :^( resource = " + ((Prop)t).getType().getResource().getName());
								return false;
							}
						}
					}
				} else if (t instanceof Tile) {
					Debug.print(this, "getting water tile");
					setPath(((Tile) t).getPosition());
					//target = ((Tile)t).getPosition();
					if (isAtPathEnd()) {
						if (inventory != null) inventory.add(Resource.get("water"), 1);
						return true;
					}
				} else {
					print("resource is weird");
				}
			} else {
				print("couldn't find resource");
			}
		}
		return false;
	}

	private void print(String string) {
		// TODO Auto-generated method stub
		if (false) Debug.print(this, string);
	}
}
