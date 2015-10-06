package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.map.Tile;

public class Worker extends Character {

	private Building workplace;
	
	public Worker(Vector2 position, Animation sprite, Animation deathAnimation) {
		super(position, sprite, deathAnimation);
	}
	
	public void update(float deltaTime) {
		if (getTile().getBuilding() != null) {
			workplace = getTile().getBuilding();
		} else {
			workplace = null;
		}
	}
	
	private void onStartWork() {
		workplace.addWorker(this);
	}
	
	private void onEndWork() {
		workplace.removeWorker(this);
	}
}
