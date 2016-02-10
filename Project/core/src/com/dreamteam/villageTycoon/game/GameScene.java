package com.dreamteam.villageTycoon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.AIController;
import com.dreamteam.villageTycoon.ai.AIController2;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Controller;
import com.dreamteam.villageTycoon.characters.SabotageKit;
import com.dreamteam.villageTycoon.characters.SabotageKitType;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.characters.SoldierType;
import com.dreamteam.villageTycoon.characters.WeaponType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.ActivationType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.EffectType;
import com.dreamteam.villageTycoon.characters.WeaponType.Type;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.Map;
import com.dreamteam.villageTycoon.projectiles.ProjectileType;
import com.dreamteam.villageTycoon.userInterface.ArrowButton;
import com.dreamteam.villageTycoon.userInterface.ArrowButton.Direction;

public class GameScene extends Scene {
	City cities[];
	Map map;
	
	private float currentGameSpeed;
	private float nextGameSpeed;
	
	private Vector2 timeControllerPosition;
	
	private ArrowButton[] timeControll = new ArrowButton[2];
	
	public GameScene() {
		super();
		AssetManager.load();
		map = new Map(this);
		
		City playerCity = new City(this, new PlayerController(), new Vector2(3, 3));
		
		cities = new City[] {
				playerCity,
				new City(this, new AIController2(playerCity), new Vector2(25, 3))
		};
		
		for(int i = 0; i < cities.length; i++) {
			addObject(cities[i]);
		}
		
		map.setupGame(cities, this);
		
		addObject(new Soldier(playerCity, new Vector2(2, 2), new WeaponType("pistol", 1, 1, 1, 1, 1, new ProjectileType(ProjectileType.Type.SHOT, 15, 5, 5, null, "projectile"), new Sprite(AssetManager.getTexture("gun")), Type.HANDGUN), 
				new SoldierType(1, 1, 1, 1, new Animation(AssetManager.getTexture("soldier"))), 
				new SabotageKit[]{ 
						new SabotageKit(new SabotageKitType("motolv coctalil", 1, 1, "firekit", ActivationType.INSTANT, EffectType.FIRE)), 
						new SabotageKit(new SabotageKitType("motolv coctalil", 1, 1, "firekit", ActivationType.INSTANT, EffectType.FIRE)) 
		}));
		
		this.currentGameSpeed = 2;
		this.nextGameSpeed = 2;
		
		
		timeControllerPosition = new Vector2(-150, 300);
		
		timeControll[0] = new ArrowButton(new Rectangle(timeControllerPosition.x-70, timeControllerPosition.y, 64, 64), ArrowButton.Direction.UP);
		timeControll[1] = new ArrowButton(new Rectangle(timeControllerPosition.x-70, timeControllerPosition.y-70, 64, 64), ArrowButton.Direction.DOWN);
		
		addObject(new Controller());
	}
	
	public void initialize() {
		
	}
	
	public void update(float dt) {
		super.update(dt*currentGameSpeed);
		
		currentGameSpeed = MathUtils.lerp(currentGameSpeed, nextGameSpeed, 0.1f);
		
		System.out.println(currentGameSpeed + "LLLLL");
		
		for(int i = 0; i < timeControll.length; i++) {
			timeControll[i].update();
			
			nextGameSpeed += timeControll[i].getValue();
		}
		
		nextGameSpeed = MathUtils.clamp(nextGameSpeed, 1, 5);
		
		this.getCamera().zoom = 2f;
	}
	
	public void draw(SpriteBatch batch) {
		map.draw(batch);
		super.draw(batch);
	}
	
	public void drawUi(SpriteBatch batch) {
		super.drawUi(batch);
		
		AssetManager.font.draw(batch, "GAME SPEED x " + (int)currentGameSpeed, this.timeControllerPosition.x, this.timeControllerPosition.y + 16);
		
		for(int i = 0; i < timeControll.length; i++) {
			timeControll[i].draw(batch);
		}
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
	
	public float getCurrentGameSpeed() {
		return this.currentGameSpeed;
	}
}
