package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public abstract class Soldier extends Character {
	enum AggressionState { ATTACKING_AND_MOVING, STEALTH, DEFENSIVE };
	
	private AggressionState  aggressionState;
	private Weapon weapon;
	
	private float attackDistance;
	private float shootAngle;
	
	private boolean isMoving;
	
	public Soldier(Vector2 position, Animation sprite, Weapon weapon) {
		super(position, sprite);
		this.weapon = weapon;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		attack();
	}
	
	public void attack() {
		
	}
}
