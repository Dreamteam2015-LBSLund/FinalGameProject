package com.dreamteam.villageTycoon.characters;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.userInterface.Inventory;

public class Soldier extends Character {
	enum AggressionState { ATTACKING_AND_MOVING, STEALTH, DEFENSIVE };
	
	private AggressionState  aggressionState;
	private Weapon weapon;
	private SoldierType soldierType;
	private Inventory inventory;
	
	private float attackDistance;
	private float shootAngle;
	
	private float speed;
	private float accuracy;
	private float toughness;
	private float fireDurationTime;
	
	private boolean isMoving;
	private boolean enemy;
	private boolean showInventory;
	private boolean isOnFire;
	
	private ArrayList<SabotageKit> sabotageKits;
	private SabotageKit startSabotageKits[];
	
	public Soldier(Vector2 position, WeaponType weaponType, SoldierType soldierType, SabotageKit startSabotageKits[]) {
		super(position, new Animation(new Texture("badlogic.jpg")), new Animation(new Texture("badlogic.jpg")));
		this.weapon = new Weapon(weaponType);
		this.soldierType = soldierType;
		setSoldierType();
		getSprite().setSize(1, 1);
		sabotageKits = new ArrayList<SabotageKit>();
		for(SabotageKit s : startSabotageKits) {
			if(s != null) sabotageKits.add(s);
		}
		inventory = new Inventory(this);
		setDepth(1);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		attack();
		
		for(int i = 0; i < sabotageKits.size(); i++) {
			if(sabotageKits.get(i).getRemove()) {
				sabotageKits.remove(i);
				inventory.setSoldier(this);
			}
		}
	}
	
	public void attack() {
		
	}
	
	public void drawUi(SpriteBatch batch) {
		super.drawUi(batch);
		if(showInventory) inventory.drawUi(batch);
	}
	
	public ArrayList<SabotageKit> getSabotageKits() {
		return sabotageKits;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public boolean getShowInventroy() {
		return showInventory;
	}
	
	public void setShowInventory(boolean showInventory) {
		this.showInventory = showInventory;
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
