package com.dreamteam.villageTycoon.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.buildings.City;

public class AIController extends CityController {
	
	private static BuildingType[] buildOrder = new BuildingType[] {
		BuildingType.getTypes().get("basicMine")
	};
	
	private int buildOrderIndex;
	Building building;
	
	City city;
	
	City playerCity; // in the future it should have other ai's as enemies too?
	
	public AIController() {
		buildOrderIndex = 0;
	}
	
	public void update() {
		System.out.println("new city at " + city.getPosition());
		if (playerCity != null) {
			attackPlayer();
		} else {
			scout();
		}
		
		build();
	}
	
	private boolean build(BuildingType type, Vector2 position) {
		position.x = (int)position.x;
		position.y = (int)position.y;
		if (city.getScene().canBuild(position)) {
			city.addBuilding(new Building(position, type, city), true);
			return true;
		} else {
			return false;
		}
	}
	
	private void build() {
		if (building == null || building.isBuilt()) {
			if (buildOrderIndex < buildOrder.length) {
				city.addBuilding(building = new Building(getNextBuildingPosition(), buildOrder[buildOrderIndex++], city), true);
			}
		}
	}
	
	private Vector2 getNextBuildingPosition() {
		return city.getPosition();
	}
	
	private void scout() {
		
	}
	
	private void attackPlayer() {
		
	}
}