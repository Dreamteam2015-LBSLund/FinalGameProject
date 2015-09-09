package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.characters.SabotageKit;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Inventory {
	// TODO: add actual UI graphics like frames and dropdown description when the user hovers the mouse over a cell
	
	private Soldier soldier;
	
	public Inventory(Soldier soldier) {
		this.soldier = soldier;
		soldier.getWeapon().getIcon().setPosition(-4, 0);
		for(int i = 0; i < soldier.getSabotageKits().size(); i++) {
			soldier.getSabotageKits().get(i).getIcon().setPosition(4, -2*i);
		}
	}
	
	public void update(float deltaTime) {
	}
	
	public void setSoldier(Soldier soldier) { 
		this.soldier = soldier;
		for(int i = 0; i < soldier.getSabotageKits().size(); i++) {
			soldier.getSabotageKits().get(i).getIcon().setPosition(4, -2*i);
		}
	}
	
	public void drawUi(SpriteBatch batch) {
		// TODO: Make the sabotagekits "buttons" so that you can equip the one you want
		soldier.getWeapon().getIcon().draw(batch);
		for(SabotageKit s : soldier.getSabotageKits()) {
			s.getIcon().draw(batch);
		}
	}
}
