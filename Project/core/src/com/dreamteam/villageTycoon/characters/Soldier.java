 package com.dreamteam.villageTycoon.characters;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.DistanceComparator;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.projectiles.Projectile;
import com.dreamteam.villageTycoon.userInterface.SoldierInventory;

public class Soldier extends Character {
	enum AggressionState { ATTACKING_AND_MOVING, STEALTH, DEFENSIVE };
	enum AlertLevel { ALERT, PASSIVE };
	
	private AggressionState  aggressionState;
	private AlertLevel alertLevel;
	private Weapon weapon;
	private SoldierType soldierType;
	private SoldierInventory inventory;
	
	private float attackDistance;
	private float shootAngle;
	
	private float speed;
	private float accuracy;
	private float toughness;
	private float fireDurationTime;
	
	private int equipedSabotageKit;
	
	private boolean isMoving;
	private boolean enemy;
	private boolean isOnFire;
	private boolean useSabotageKit;
	
	private ArrayList<SabotageKit> sabotageKits;
	private ArrayList<Character> spottedEnemies;
	
	private Character currentTarget;
	
	private SabotageKit startSabotageKits[];
	
	public Soldier(City city, Vector2 position, WeaponType weaponType, SoldierType soldierType, SabotageKit startSabotageKits[]) {
		super(position, new Animation(AssetManager.getTexture("soldier")), new Animation(AssetManager.getTexture("soldier")), city);
		this.weapon = new Weapon(weaponType);
		this.soldierType = soldierType;
		setSoldierType();
		sabotageKits = new ArrayList<SabotageKit>();
		for(SabotageKit s : startSabotageKits) {
			if(s != null) sabotageKits.add(s);
		}
		
		spottedEnemies = new ArrayList<Character>();
		inventory = new SoldierInventory(this);
		setDepth(1);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		attack();
		weapon.update(deltaTime);
		
		if(getShowInventroy()) {
			for (GameObject c : getScene().getObjects()) {
				if (c instanceof Character) {
					((Character) c).setSelected(false);
				}
			}
			
			inventory.update(deltaTime);
			equipedSabotageKit = inventory.getEquipedSabotageKit();
		}
		
		for(int i = 0; i < sabotageKits.size(); i++) {
			if(sabotageKits.get(i).getRemove()) {
				if(i == equipedSabotageKit) {
					equipedSabotageKit = 0;
				}
				
				sabotageKits.remove(i);
				inventory.setSoldier(this);
			}
		}
		
		if(spottedEnemies.size() > 0) {
			float shortestRange = -1;
			float currentRange = 0;
			
			Character tempTarget = new Character(Vector2.Zero, new Animation(AssetManager.getTexture("error")), new Animation(AssetManager.getTexture("error")), this.getCity());
			
			for(Character c : spottedEnemies) {
				currentRange = c.distanceTo(this.getPosition());
				if(shortestRange > currentRange && shortestRange != -1) {
					tempTarget = c;
					shortestRange = currentRange;
				}
			}
			
			this.currentTarget = tempTarget;
		}
	}
	
	public void attack() {
		if(useSabotageKit) {
			if(sabotageKits.get(equipedSabotageKit) != null) {
				sabotageKits.get(equipedSabotageKit).use();
			}
		}
	}

	public void onHit(Projectile projectile) {
		super.onHit(projectile);
		alertLevel = AlertLevel.ALERT;
		for (Character c : spottedEnemies) {
			if(c != projectile.getOwner()) {
				addSpottedEnemies(projectile.getOwner());
			}
		}
	}
	
	public void addSpottedEnemies(Character c) {
		spottedEnemies.add(c);
	}
	
	public void drawUi(SpriteBatch batch) {
		super.drawUi(batch);
		if(getShowInventroy()) inventory.drawUi(batch);
	}
	
	public ArrayList<SabotageKit> getSabotageKits() {
		return sabotageKits;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

	public void shoot(Vector2 target, float speed) {
		getScene().addObject(new Projectile(getPosition(), target, speed, weapon.getType().getProjectileType()));
		weapon.onShoot();
	}
	
	public void setSoldierType() {
		this.setSprite(soldierType.getTypeSprite());
		this.setHealth(soldierType.getStartHealth());
		this.speed = soldierType.getSpeed();
		this.accuracy = soldierType.getAccuracy();
		this.toughness = soldierType.getToughness();
		
		this.enemy = soldierType.getEnemy();
		this.setPosition(getPosition());
	}
}
