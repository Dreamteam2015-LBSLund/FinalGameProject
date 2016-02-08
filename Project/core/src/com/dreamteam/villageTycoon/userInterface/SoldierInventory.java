package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.characters.SabotageKit;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class SoldierInventory {
	// TODO: add actual UI graphics like frames and dropdown description when the user hovers the mouse over a cell
	
	private Soldier soldier;
	
	private Vector2 mouse;
	
	private int equipedSabotageKit;
	
	private AggressionStateButton[] aggressionStateButtons; 
	
	private Animation foodIcon;
	
	public SoldierInventory(Soldier soldier) {
		this.soldier = soldier;
		soldier.getWeapon().getIcon().setPosition(-4, 0);
		for(int i = 0; i < soldier.getSabotageKits().size(); i++) {
			soldier.getSabotageKits().get(i).getIcon().setPosition(4, -2*i);
		}
		
		foodIcon = new Animation(AssetManager.getTexture("foodIcon"));
		foodIcon.setPosition(200, 200);
		foodIcon.setSize(100, 100);
		
		aggressionStateButtons = new AggressionStateButton[3];
		aggressionStateButtons[0] = new AggressionStateButton(new Vector2(-Gdx.graphics.getWidth()+50, -Gdx.graphics.getHeight()+50), Soldier.AggressionState.ATTACKING_AND_MOVING);
		aggressionStateButtons[1] = new AggressionStateButton(new Vector2(-Gdx.graphics.getWidth()+50, -Gdx.graphics.getHeight()+100), Soldier.AggressionState.STEALTH);
		aggressionStateButtons[2] = new AggressionStateButton(new Vector2(-Gdx.graphics.getWidth()+50, -Gdx.graphics.getHeight()+150), Soldier.AggressionState.DEFENSIVE);
	}
	
	public void update(float deltaTime) {
		mouse = soldier.getScene().getUiMouse();
		
		for(int i = 0; i < aggressionStateButtons.length; i++) {
			aggressionStateButtons[i].update(soldier);
		}
		
		for(int i = 0; i < soldier.getSabotageKits().size(); i++) {
			// När man spaghettiar till det och man ba' :^(
		}
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
		if(soldier.getSabotageKits().size() > 0) {
			for(SabotageKit s : soldier.getSabotageKits()) {
				s.getIcon().draw(batch);
			}
		}

		for(int i = 0; i < aggressionStateButtons.length; i++) {
			aggressionStateButtons[i].draw(batch);
		}
		
		AssetManager.font.draw(batch, "IN CLIP: " + soldier.getWeapon().getType().getClipSize() + "/" + soldier.getWeapon().getClipCount(), -Gdx.graphics.getWidth()+450, -Gdx.graphics.getHeight()+125);
		
		batch.draw(soldier.getWeapon().getIcon(), -Gdx.graphics.getWidth()+450, -Gdx.graphics.getHeight()+225, 100, 100);
		
		if(soldier.getFoodReserve() > 0) {
			AssetManager.font.draw(batch, "x" + soldier.getFoodReserve(), -Gdx.graphics.getWidth()+450+100, -Gdx.graphics.getHeight()+500);
			foodIcon.setPosition(-Gdx.graphics.getWidth()+450-10, -Gdx.graphics.getHeight()+430);
			foodIcon.draw(batch);
		}
	}
	
	public int getEquipedSabotageKit() {
		return equipedSabotageKit;
	}
}
