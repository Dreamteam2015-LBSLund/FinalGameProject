package com.dreamteam.villageTycoon.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.buildings.City;

public class AIController extends CityController {
	City city;
	
	public void update() {
		
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
}