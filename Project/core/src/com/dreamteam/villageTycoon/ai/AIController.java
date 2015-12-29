package com.dreamteam.villageTycoon.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.utils.Debug;

public class AIController extends CityController {
	
	private static BuildingType[] buildOrder = new BuildingType[] {
		BuildingType.getTypes().get("mine"),
		BuildingType.getTypes().get("mine"),
		BuildingType.getTypes().get("mine"),
		BuildingType.getTypes().get("mine"),
		BuildingType.getTypes().get("mine"),
		BuildingType.getTypes().get("mine")
	};
	
	private int buildOrderIndex;
	Building building;
	
	
	City playerCity; // in the future it should have other ai's as enemies too?
	
	public AIController() {
		buildOrderIndex = 0;
	}
	
	private boolean canAttack() {
		return getCity().getSoldiers().size() > 3;
	}
	
	private void makeSoldiers() {
		if (canMakeSoldiers()) {
			makeSoldiers();
		}
	}
	
	private boolean canMakeSoldiers() {
		return getCity().hasBuildingType(BuildingType.getTypes().get("armyBarack"));
	}

	public void aiUpdate() {
		if (canAttack()) {
			
		} else {
			makeSoldiers();
		}
		
		
		
		/*if (playerCity != null) {
			attackPlayer();
		} else {
			scout();
		}
		
		build();
	*/}
	
	public void update(float dt)  {
		aiUpdate();
	}
	
	private boolean build(BuildingType type, Vector2 position) {
		Debug.print(this, "city ai update");
		position.x = (int)position.x;
		position.y = (int)position.y;
		if (getCity().getScene().canBuild(position)) {
			getCity().addBuilding(new Building(position, type, getCity()), true);
			return true;
		} else {
			return false;
		}
	}
	
	private void build() {
		if (building == null || building.isBuilt()) {
			if (buildOrderIndex < buildOrder.length) {
				getCity().addBuilding(building = new Building(getNextBuildingPosition(), buildOrder[buildOrderIndex++], getCity()), true);
			}
		}
	}
	
	private Vector2 getNextBuildingPosition() {
		return getCity().getPosition();
	}
	
	private void scout() {
		
	}
	
	private void attackPlayer() {
		
	}
}