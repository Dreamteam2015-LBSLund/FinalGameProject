package com.dreamteam.villageTycoon.ai;

import com.dreamteam.villageTycoon.buildings.City;

public class AIControllerFactory {
	public static CityController getController(City targetCity) {
		return new AIController2(targetCity);
	}
}
