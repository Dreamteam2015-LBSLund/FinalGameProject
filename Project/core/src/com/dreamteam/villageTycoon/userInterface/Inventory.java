package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Inventory {
	// TODO: add actual UI graphics like frames and dropdown description when the user hovers the mouse over a cell
	
	private Soldier soldier;
	
	public Inventory(Soldier soldier) {
		this.soldier = soldier;
		soldier.getWeapon().getIcon().setPosition(-1, 0);
	}
	
	public void update(float deltaTime) {
	}
	
	public void setSoldier(Soldier soldier) { 
		this.soldier = soldier;
	}
	
	public void drawUi(SpriteBatch batch) {
		soldier.getWeapon().getIcon().draw(batch);
	}
}
