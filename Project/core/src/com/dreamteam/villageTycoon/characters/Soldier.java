 package com.dreamteam.villageTycoon.characters;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.DistanceComparator;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.projectiles.Projectile;
import com.dreamteam.villageTycoon.projectiles.ProjectileType;
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
	
	private float maxAttackDistance;
	
	private int equipedSabotageKit;
	
	private boolean isMoving;
	private boolean enemy;
	private boolean isOnFire;
	private boolean useSabotageKit;
	
	private ArrayList<SabotageKit> sabotageKits;
	private ArrayList<Character> spottedEnemies;
	
	private Building targetBuilding;
	
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
		
		maxAttackDistance = 5;
		
		this.aggressionState = AggressionState.ATTACKING_AND_MOVING;
		
		spottedEnemies = new ArrayList<Character>();
		inventory = new SoldierInventory(this);
		setDepth(1);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		attack();
		weapon.update(deltaTime);
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
			getScene().addObject(new Projectile(getPosition(), new Vector2(0, 0), weapon.getWeaponType().getProjectileType(), this));
		}
		
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
		
		for(GameObject g : getScene().getObjects()) {
			if(g instanceof Character && g.distanceTo(this.getPosition()) <= this.maxAttackDistance && ((Character) g).getCity() != getCity()) {
				this.addSpottedEnemies(((Character)g));
			}
		}
		
		if(spottedEnemies.size() > 0) {
			float shortestRange = -1;
			float currentRange = 0;
			
			Character tempTarget = new Character(new Vector2(-1, -1), new Animation(AssetManager.getTexture("error")), new Animation(AssetManager.getTexture("error")), this.getCity());
			
			for(Character c : spottedEnemies) {
				currentRange = c.distanceTo(this.getPosition());
				if(shortestRange > currentRange && shortestRange != -1) {
					tempTarget = c;
					shortestRange = currentRange;
				}
			}
			
			this.currentTarget = this.spottedEnemies.get(0);
			
			if(currentTarget.getHealth() <= 0) {
				spottedEnemies.remove(currentTarget);
			}
			
			//System.out.println(this.currentTarget.getPosition());
			
			if(this.aggressionState == AggressionState.ATTACKING_AND_MOVING) {
				moveToTarget();
			}
		}
	}
	
	public void moveToTarget() {
		if(currentTarget.distanceTo(this.getPosition()) > this.maxAttackDistance) {
			float angle = (float)Math.atan2(currentTarget.getPosition().y - this.getPosition().y, currentTarget.getPosition().x - this.getPosition().x);
			this.setPath(new Vector2((float)Math.cos(angle)*this.maxAttackDistance, (float)Math.sin(angle)*this.maxAttackDistance));
		}
		if(currentTarget.distanceTo(this.getPosition()) > this.maxAttackDistance-1) {
			if(weapon.canShoot()) {
				getScene().addObject(new Projectile(new Vector2(this.getPosition().x+0.5f, this.getPosition().y +0.5f), currentTarget.getPosition(), weapon.getWeaponType().getProjectileType(), this));
				weapon.onShoot();
			}
		}
		
		if(this.spottedEnemies.size() <= 0 && this.targetBuilding != null) {
			if(targetBuilding.distanceTo(this.getPosition()) > this.maxAttackDistance) {
				float angle = (float)Math.atan2(targetBuilding.getPosition().y - this.getPosition().y, targetBuilding.getPosition().x - this.getPosition().x);
				this.setPath(new Vector2((float)Math.cos(angle)*this.maxAttackDistance, (float)Math.sin(angle)*this.maxAttackDistance));
			}
			if(targetBuilding.distanceTo(this.getPosition()) > this.maxAttackDistance-1) {
				if(weapon.canShoot()) {
					getScene().addObject(new Projectile(new Vector2(this.getPosition().x+0.5f, this.getPosition().y +0.5f), targetBuilding.getPosition(), weapon.getWeaponType().getProjectileType(), this));
					weapon.onShoot();
				}
			}
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
		boolean aldreadyExists = false;
		
		for(Character g : spottedEnemies) {
			if(c == g) {
				aldreadyExists = true;
				break;
			}
		}
		
		if(!aldreadyExists) spottedEnemies.add(c);
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
