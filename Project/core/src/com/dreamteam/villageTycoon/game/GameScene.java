package com.dreamteam.villageTycoon.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.AIController;
import com.dreamteam.villageTycoon.ai.AIController2;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Controller;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.Map;

public class GameScene extends Scene {
	City cities[];
	Map map;
	
	public GameScene() {
		super();
		AssetManager.load();
		map = new Map(this);
		
		City playerCity = new City(this, new PlayerController(), new Vector2(3, 3));
		
		cities = new City[] {
				playerCity,
				// Vi har fan gått full spaghetti min goda kamrat, Johannes "The spaghetti bagare" Larsson
				new City(this, new AIController2(playerCity), new Vector2(25, 3))
		};
		
		for(int i = 0; i < cities.length; i++) {
			addObject(cities[i]);
		}
		
		map.setupGame(cities, this);
		
		addObject(new Controller());
	}
	
	public void initialize() {
		
	}
	
	public void update(float dt) {
		super.update(dt*3);
		this.getCamera().zoom = 2f;
	}
	
	public void draw(SpriteBatch batch) {
		map.draw(batch);
		super.draw(batch);
	}
	
	public Map getMap() {
		return this.map;
	}
	
	public boolean canBuild(Vector2 position) {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 3; y++) {
				if (!map.getTiles()[x + (int)position.x][y + (int)position.y].isBuildable()) return false;
			}
		}
		return true;
	}
}
