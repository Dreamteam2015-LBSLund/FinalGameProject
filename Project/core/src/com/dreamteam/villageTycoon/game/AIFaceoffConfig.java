package com.dreamteam.villageTycoon.game;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.ai.AIController2;
import com.dreamteam.villageTycoon.ai.AIController3;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.framework.Scene;

public class AIFaceoffConfig implements PlayerConfig {
	public City[] getCities(Scene scene) {
		
		City aiCity1, aiCity2;
		
		aiCity1 = new City(scene, "AIController2 aka generall AI", new PlayerController(), new Vector2(5, 3));
		aiCity2 = new City(scene, "AIController3 aka simple AI", new AIController3(aiCity1), new Vector2(25, 15));
		
		aiCity1.setController(new AIController2(aiCity2));
		
		return new City[] {
				//playerCity,
				//new City(this, AIControllerFactory.getController(playerCity), new Vector2(25, 3)),
				aiCity1, aiCity2
		};
	}
}
