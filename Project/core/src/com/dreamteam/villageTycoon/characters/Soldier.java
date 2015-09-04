package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Soldier extends Character {
	enum AggressionState { ATTACKING_AND_MOVING, STEALTH, DEFENSIVE };
	
	private AggressionState  aggressionState;
	private Weapon weapon;
	private SoldierType soldierType;
	
	private float attackDistance;
	private float shootAngle;
	
	private float speed;
	private float accuracy;
	private float toughness;
	
	private boolean isMoving;
	private boolean enemy;
	
	public Soldier(Vector2 position, WeaponType weaponType, SoldierType soldierType) {
		super(position, new Animation(new Texture("badlogic.jpg")), new Animation(new Texture("badlogic.jpg")));
		this.weapon = new Weapon(weaponType);
		this.soldierType = soldierType;
		setSoldierType();
		getSprite().setSize(1, 1);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		attack();
	}
	
	public void attack() {
		
	}
	
	public void drawUi(SpriteBatch batch) {
		super.drawUi(batch);
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
