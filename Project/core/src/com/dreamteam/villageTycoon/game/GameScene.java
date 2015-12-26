package com.dreamteam.villageTycoon.game;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.AIController;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.Map;

public class GameScene extends Scene {
	City cities[];
	Map map;
	
	public GameScene() {
		super();
		AssetManager.load();
		map = new Map(this);
		
		cities = new City[] {
				new City(this, new PlayerController(), new Vector2(3, 3)), 
				new City(this, new AIController(), new Vector2(25, 3))
		};
		
		for(int i = 0; i < cities.length; i++) {
			addObject(cities[i]);
		}
		
		map.setupGame(cities, this);
	}
	
	public void update(float dt) {
		super.update(dt);
	}
}
