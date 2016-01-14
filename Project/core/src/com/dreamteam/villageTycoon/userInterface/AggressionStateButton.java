package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.characters.Soldier.AggressionState;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class AggressionStateButton extends UiElement {
	private AggressionState state;
	
	private String text;
	private String displayText;
	
	private Vector2 position;
	
	public AggressionStateButton(Vector2 position, AggressionState state) {
		super(new Rectangle(position.x, position.y, 300, 50));
		this.position = position;
		this.state = state;
		
		switch(state) {
		case STEALTH:
			this.text = "Stealth";
			break;
		case ATTACKING_AND_MOVING:
			this.text = "Attack";
			break;
		case DEFENSIVE:
			this.text = "Defend";
			break;
		}
		
		this.displayText = this.text;
	}
	
	public void Update(Soldier soldier) {
		super.update();
		
		displayText = (soldier.getAggressionState() == state) ? ">"+text : text;
		
		if(this.isPressed()) soldier.setAggressionState(state);
	}
	
	public void draw(SpriteBatch batch) {
		AssetManager.font.draw(batch, displayText, position.x, position.y);
	}
	
	public AggressionState getState() {
		return state;
	}
}
