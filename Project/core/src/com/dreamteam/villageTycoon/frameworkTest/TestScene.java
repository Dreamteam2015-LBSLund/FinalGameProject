package com.dreamteam.villageTycoon.frameworkTest;

import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.characters.Controller;
import com.dreamteam.villageTycoon.characters.SabotageKit;
import com.dreamteam.villageTycoon.characters.SabotageKitType;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.characters.SoldierType;
import com.dreamteam.villageTycoon.characters.WeaponType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.ActivationType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.EffectType;
import com.dreamteam.villageTycoon.characters.WeaponType.Type;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.ai.AIController;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingPlacer;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.Map;
import com.dreamteam.villageTycoon.projectiles.ProjectileType;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.workers.Worker;

public class TestScene extends Scene {
	private Map map;
	private City playerCity;
	private City city;
	private City enemyCity;
	
	public TestScene() {
		super();

		AssetManager.load();
		
		map = new Map(this);
		
		//city = map.generateCity(new Vector2(30, 30), 10, 1, this);
		
		Debug.print(this, "map == null =  " + (map == null));
		
		addObject(playerCity = new City(this, new PlayerController(), new Vector2(3, 3)));
		addObject(enemyCity = new City(this, new AIController(), new Vector2(25, 3)));
		
		//addObject(new TestObject(AssetManager.getTexture("grassTile")));
		addObject(new Controller());
		//addObject(new TestObject(AssetManager.getTexture("test")));
		//addObject(new Character(new Vector2(0, 0), new Animation(AssetManager.getTexture("test"))));
		//addObject(new Character(new Vector2(0, -1.5f), new Animation(AssetManager.getTexture("test"))));
		//addObject(new Character(new Vector2(0, -2.5f), new Animation(AssetManager.getTexture("test"))));
		//addObject(new Character(new Vector2(0, -4.5f), new Animation(AssetManager.getTexture("test"))));
		
		// x-types will be pre-defined so it won't look this messy later
		
		// gör en fucking for loop Tom din jävla sosse
		for(int i = 0; i < 1; i++) {
			addObject(new Soldier(playerCity, new Vector2(2, 2), new WeaponType("pistol", 1, 1, 1, 1, 1, new ProjectileType(ProjectileType.Type.SHOT, 5, 5, 5, "projectile"), new Sprite(AssetManager.getTexture("gun")), Type.HANDGUN), 
					new SoldierType(1, 1, 1, 1, new Animation(AssetManager.getTexture("soldier"))), 
					new SabotageKit[]{ 
							new SabotageKit(new SabotageKitType("motolv coctalil", 1, 1, "firekit", ActivationType.INSTANT, EffectType.FIRE)), 
							new SabotageKit(new SabotageKitType("motolv coctalil", 1, 1, "firekit", ActivationType.INSTANT, EffectType.FIRE)) 
			}));
			addObject(new Worker(new Vector2(10, 10), new Animation(AssetManager.getTexture("worker")),  new Animation(AssetManager.getTexture("test")), playerCity));
			addObject(new Worker(new Vector2(15, 10), new Animation(AssetManager.getTexture("worker")),  new Animation(AssetManager.getTexture("test")), enemyCity));
		}
		
	}
	
	public void initialize() {
		for(int i = 0; i < 1; i++)
		//addObject(new Building(new Vector2(3+i*3, 3), BuildingType.getTypes().get("factory1"), playerCity));
		city = new City(this, new AIController(), new Vector2(50, 50));
		//addObject(new Building(new Vector2(3, 3), BuildingType.getTypes().get("factory1"), playerCity));
		//addObject(new Building(new Vector2(8, 3), BuildingType.getTypes().get("factory1"), playerCity));
		//City city = new City(this);
		map.generateCity(city.getPosition(), 4, 0, this, city);
	}
	
	public Map getMap() {
		return map;
	}
	
	public void update(float dt) {
		super.update(dt);
	}
	
	public void draw(SpriteBatch batch) {
		map.draw(batch);
		super.draw(batch);
	}
	
	public void drawUi(SpriteBatch uiBatch) {
		
		super.drawUi(uiBatch);
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
