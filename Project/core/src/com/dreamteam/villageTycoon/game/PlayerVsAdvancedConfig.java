package com.dreamteam.villageTycoon.game;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.ai.AIController2;
import com.dreamteam.villageTycoon.ai.AIController3;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.framework.Scene;

public class PlayerVsAdvancedConfig implements PlayerConfig {
	
	public City[] getCities(Scene scene) {
		City playerCity = new City(scene, "you", new PlayerController(), new Vector2(3, 3));
		
		City aiCity1 = new City(scene, "AIController2 aka general AI", new AIController2(playerCity), new Vector2(5, 3));
		//aiCity2 = new City(scene, "AIController3 aka simple AI", new AIController3(aiCity1), new Vector2(25, 15));
				
		return new City[] {
				playerCity,
				aiCity1
		};
	}
}
