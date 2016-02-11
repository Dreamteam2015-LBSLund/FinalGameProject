 package com.dreamteam.villageTycoon.characters;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.WeaponType.Type;
import com.dreamteam.villageTycoon.effects.Explosion;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.DistanceComparator;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.projectiles.Projectile;
import com.dreamteam.villageTycoon.projectiles.ProjectileType;
import com.dreamteam.villageTycoon.userInterface.SabotageKitButton;
import com.dreamteam.villageTycoon.userInterface.SoldierInventory;
import com.dreamteam.villageTycoon.userInterface.TargetWeaponButton;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class Soldier extends Character {
	public enum AggressionState { ATTACKING_AND_MOVING, STEALTH, DEFENSIVE };
	enum AlertLevel { ALERT, PASSIVE };
	
	private final WeaponType WOOD_SWORD = new WeaponType("wooden-sword", 1, 1, 1, 0, 0, new ProjectileType(ProjectileType.Type.SHOT, 1, 1, 1, null, "projectile"), new Sprite(AssetManager.getTexture("soldier")), Type.MELEE);
	private final WeaponType IRON_SWORD = new WeaponType("iron-sword", 1, 1, 1, 0, 0, new ProjectileType(ProjectileType.Type.SHOT, 1, 1, 3, null, "projectile"), new Sprite(AssetManager.getTexture("soldier")), Type.MELEE);
	
	private WeaponType allWeaponTypes[] = new WeaponType[2];
	private ArrayList<WeaponType> avaibleWeaponTypes;
	
	private WeaponType targetWeapon;
	
	private ArrayList<TargetWeaponButton> targetWeaponButtons;
	private Vector2 targetWeaponButtonPosition;
	
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
	
	private int foodReserve;
	
	private Building targetBuilding;
	
	private Character currentTarget;
	
	private SabotageKit startSabotageKits[];
	
	private Vector2 sabotageKitTarget;
	
	private SabotageKitButton sabotageKitButton;
	
	private float sabotageKitDelay;
	
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
		
		maxAttackDistance = 8;
		
		this.sabotageKitButton = new SabotageKitButton(new Rectangle(100, 100, 100, 100), new Animation(AssetManager.getTexture("firekit")));
		
		this.aggressionState = AggressionState.ATTACKING_AND_MOVING;
		
		spottedEnemies = new ArrayList<Character>();
		inventory = new SoldierInventory(this);
		setDepth(1);
		
		if(this.getCity().getController() instanceof PlayerController) {
			this.setSprite(new Animation(AssetManager.getTexture("soldier")));
			this.setDeathAnimation(new Animation(AssetManager.getTexture("playerCorpse")));
		} else {
			this.setSprite(new Animation(AssetManager.getTexture("enemySoldier")));
			this.setDeathAnimation(new Animation(AssetManager.getTexture("enemyCorpse")));
		}
		
		avaibleWeaponTypes = new ArrayList<WeaponType>();
		allWeaponTypes[0] = WOOD_SWORD;
		allWeaponTypes[1] = IRON_SWORD;
		
		targetWeaponButtons = new ArrayList<TargetWeaponButton>();
		targetWeaponButtonPosition = new Vector2(0, 0);
		
		foodReserve = 10;
	}
	
	public void checkForWeapons() {
		for(int i = 0; i < allWeaponTypes.length; i++) {
			if (getCity().getNoMaterials(Resource.get(allWeaponTypes[i].getName())) > 0 ) {
				if(!avaibleWeaponTypes.contains(allWeaponTypes[i])) {
					avaibleWeaponTypes.add(allWeaponTypes[i]);
					targetWeaponButtons.add(new TargetWeaponButton(targetWeaponButtonPosition.cpy().add(0, i*64), allWeaponTypes[i]));
				}
			}
		}
		
		if(targetWeapon != null) {
			findResource(Resource.get(targetWeapon.getName()), null);
			
			if(findResource(Resource.get(targetWeapon.getName()), null)) {
				weapon = new Weapon(targetWeapon);
				targetWeapon = null;
			}
		}
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		checkForWeapons();
		
		for(int i = 0; i < avaibleWeaponTypes.size(); i++) {
			System.out.println(avaibleWeaponTypes.get(i).getName() + " - " + avaibleWeaponTypes.size());
		}
		
		equipedSabotageKit = inventory.getEquipedSabotageKit();
		equipedSabotageKit = MathUtils.clamp(equipedSabotageKit, 0, this.sabotageKits.size());
		
		if (getAmountFull() / getMaxFull() < .3f) {
			if(foodReserve > 0) {
				consume();
			} else {
				if (findResource(Resource.get("food"), null)) {
					setAmountFull(getMaxFull());
				}
			}
		}
		
		if(prepareSabotageKit && sabotageKits.size() > 0) attack(deltaTime);
		weapon.update(deltaTime);
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
			getScene().addObject(new Projectile(getPosition(), new Vector2(0, 0), weapon.getWeaponType().getProjectileType(), this));
		}
		
		if(getShowInventroy()) {
			this.sabotageKitButton.update(this);
			
			for(TargetWeaponButton t : targetWeaponButtons) {
				t.update(this);
			}
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
			if(g instanceof Character && g.distanceTo(this.getPosition().cpy()) <= this.maxAttackDistance && ((Character) g).getCity() != getCity()) {
				this.addSpottedEnemies(((Character)g));
			}
		}
		
		attackBuilding();
		
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
	
	public void attackCity(City city) {
		aggressionState = AggressionState.ATTACKING_AND_MOVING; 
		setPath(city.getPosition());
	}
	
	public void moveToTarget() {
		System.out.println(currentTarget.distanceTo(this.getPosition()));
		
		if(currentTarget.distanceTo(this.getPosition()) > this.maxAttackDistance && this.aggressionState != AggressionState.DEFENSIVE && !getSelected()) {
			float angle = (float)Math.atan2(currentTarget.getPosition().y - this.getPosition().y, currentTarget.getPosition().x - this.getPosition().x);
			this.setPath(new Vector2((float)Math.cos(angle)*this.maxAttackDistance, (float)Math.sin(angle)*this.maxAttackDistance));
		}
		if(currentTarget.distanceTo(this.getPosition()) < this.maxAttackDistance-1) {
			if(weapon.canShoot()) {
				getScene().addObject(new Projectile(new Vector2(this.getPosition().x+0.5f, this.getPosition().y +0.5f), currentTarget.getPosition(), weapon.getWeaponType().getProjectileType(), this));
				weapon.onShoot();
			}
		}
		//if(getBuilding() != null && getBuilding().getCity() == getCity()) targetBuilding = getBuilding();
	}
	
	public void attackBuilding() {
		if(this.spottedEnemies.size() <= 0 && this.targetBuilding != null) {
			if(targetBuilding.distanceTo(this.getPosition()) > this.maxAttackDistance && this.aggressionState != AggressionState.DEFENSIVE) {
				float angle = (float)Math.atan2(targetBuilding.getPosition().y - this.getPosition().y, targetBuilding.getPosition().x - this.getPosition().x);
				//this.setPath(new Vector2((float)Math.cos(angle)*this.maxAttackDistance, (float)Math.sin(angle)*this.maxAttackDistance));
			}
			if(/*targetBuilding.distanceTo(this.getPosition()) < this.maxAttackDistance*/true) {
				if(weapon.canShoot()) {
					getScene().addObject(new Projectile(new Vector2(this.getPosition().x+0.5f, this.getPosition().y +0.5f), targetBuilding.getPosition().cpy().add(new Vector2(1, 1)), weapon.getWeaponType().getProjectileType(), this));
					weapon.onShoot();
				}
			}
		}
		
		if(targetBuilding != null) {
			if(!this.getSelected()) setPath(targetBuilding.getPosition().cpy().add(new Vector2(2, 2)));
			if(targetBuilding.distanceTo(this.getPosition().cpy()) > this.maxAttackDistance || targetBuilding.getHealth() <= 0) targetBuilding = null;
		}
		
		for(GameObject g : getScene().getObjects()) {
			if(g instanceof Building) {
				if(((Building) g).getCity() != this.getCity() && g.distanceTo(getPosition().cpy()) < this.maxAttackDistance) { 
					targetBuilding = (Building) g;
				}
			}
		}
	}
	
	public void attack(float deltaTime) {
		this.sabotageKitDelay += deltaTime*10;

		if(sabotageKitTarget != null) {
			if(sabotageKits.get(equipedSabotageKit) != null) {
				sabotageKits.get(equipedSabotageKit).use(this, sabotageKitTarget, getScene());
				prepareSabotageKit = false;
				sabotageKitTarget = null;
				sabotageKitDelay = 0;
			}
		} else {
			if(Gdx.app.getInput().isTouched() && sabotageKitDelay >= 16) {
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
		if(getShowInventroy()) {
			inventory.drawUi(batch);
			sabotageKitButton.draw(batch);
			for(TargetWeaponButton t : targetWeaponButtons) {
				t.draw(batch);
			}
		}
		
		if(prepareSabotageKit && sabotageKits.size() > 0) {
			AssetManager.font.draw(batch, "PICK TARGET POSISTION", 0, 0);
		}
	}
	
	public void consume() {
		if(foodReserve > 0) {
			setAmountFull(getMaxFull());
			foodReserve -= 1;
		}
	}
	
	public int getFoodReserve() {
		return this.foodReserve;
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
	
	public void setTargetWeapon(WeaponType targetWeapon) {
		this.targetWeapon = targetWeapon;
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
