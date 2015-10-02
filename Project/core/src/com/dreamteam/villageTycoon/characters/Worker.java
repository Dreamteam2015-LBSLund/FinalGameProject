package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.map.Tile;

public class Worker extends Character {

	public Worker(Vector2 position, Animation sprite, Animation deathAnimation) {
		super(position, sprite, deathAnimation);
	}
	
	
	// @Tom låt Controller skicka ett event till alla markerade Characters. 
	// De hanterar det själva (till viss del iaf) eftersom olika enhetstyper ska tolka samma input olika.
	public void onControllInput(Vector2 target) {
		Tile t = ((TestScene)getScene()).getMap().tileAt(target);
		Building b = t.getBuilding();
		if ( b!= null) workAt(b);
	}
	
	private void workAt(Building b) {
		
	}
}
