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
	public enum AggressionState { ATTACKING_AND_MOVING, STEALTH, DEFENSIVE };
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
	private boolean prepareSabotageKit;
	
	private ArrayList<SabotageKit> sabotageKits;
	private ArrayList<Character> spottedEnemies;
	
	private Building targetBuilding;
	
	private Character currentTarget;
	
	private SabotageKit startSabotageKits[];
	
	private Vector2 sabotageKitTarget;
	
	public Soldier(City city, Vector2 position, WeaponType weaponType, SoldierType soldierType, SabotageKit startSabotageKits[]) {
		super(position, new Animation(AssetManager.getTexture("soldier")), new Animation(AssetManager.getTexture("soldier")), city);
		this.weapon = new Weapon(weaponType);
		this.soldierType = soldierType;
		setSoldierType();
		sabotageKits = new ArrayList<SabotageKit>();
		
		for(SabotageKit s : startSabotageKits) {
			if(s != null) sabotageKits.add(s);
		}
		
		city.addSoldier(this);
		
		maxAttackDistance = 5;
		
		this.aggressionState = AggressionState.ATTACKING_AND_MOVING;
		
		spottedEnemies = new ArrayList<Character>();
		inventory = new SoldierInventory(this);
		setDepth(1);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(prepareSabotageKit) attack();
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
			//equipedSabotageKit = inventory.getEquipedSabotageKit();
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
			
			if(this.aggressionState == AggressionState.STEALTH) {
				for(Character c : spottedEnemies) {
					if(c instanceof Soldier) {
						for(Character c2 : ((Soldier)c).getSpottedEnemies()) {
							if(c2 == this) this.aggressionState = AggressionState.ATTACKING_AND_MOVING;
						}
					}
				}
			}
		}
	}
	
	public void moveToTarget() {
		if(currentTarget.distanceTo(this.getPosition()) > this.maxAttackDistance && this.aggressionState != AggressionState.DEFENSIVE) {
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
			if(targetBuilding.distanceTo(this.getPosition()) > this.maxAttackDistance && this.aggressionState != AggressionState.DEFENSIVE) {
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
		
		if(getBuilding() != null && getBuilding().getCity() == getCity()) targetBuilding = getBuilding();
	}
	
	public void attack() {
		if(useSabotageKit && sabotageKitTarget != null) {
			if(sabotageKits.get(equipedSabotageKit) != null) {
				sabotageKits.get(equipedSabotageKit).use();
				prepareSabotageKit = false;
			}
		} else {
			if(Gdx.app.getInput().isTouched()) {
				sabotageKitTarget = getScene().getWorldMouse();
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
		
		if(prepareSabotageKit) {
			AssetManager.font.draw(batch, "PICK TARGET POSISTION", 0, 0);
		}
	}
	
	public void setEquipedSabotageKit(int equipedSabotageKit) {
		this.equipedSabotageKit = equipedSabotageKit;
	}
	
	public void setPrepareSabotageKit(boolean prepareSabotageKit) {
		this.prepareSabotageKit = prepareSabotageKit;
	}
	
	public void setUseSabotageKit(boolean useSabotageKit) {
		this.useSabotageKit = useSabotageKit;
	}
	
	public ArrayList<SabotageKit> getSabotageKits() {
		return sabotageKits;
	}
	
	public ArrayList<Character> getSpottedEnemies() {
		return spottedEnemies;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}	
	
	public AggressionState getAggressionState() {
		return aggressionState;
	}
	
	public void setAggressionState(AggressionState aggressionState) {
		this.aggressionState = aggressionState;
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
