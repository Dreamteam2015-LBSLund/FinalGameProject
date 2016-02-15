package com.dreamteam.villageTycoon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.AIController;
import com.dreamteam.villageTycoon.ai.AIController2;
import com.dreamteam.villageTycoon.ai.AIController3;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Controller;
import com.dreamteam.villageTycoon.characters.Character;
import com.dreamteam.villageTycoon.characters.SabotageKit;
import com.dreamteam.villageTycoon.characters.SabotageKitType;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.characters.SoldierType;
import com.dreamteam.villageTycoon.characters.WeaponType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.ActivationType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.EffectType;
import com.dreamteam.villageTycoon.characters.WeaponType.Type;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.Map;
import com.dreamteam.villageTycoon.projectiles.ProjectileType;
import com.dreamteam.villageTycoon.userInterface.ArrowButton;
import com.dreamteam.villageTycoon.userInterface.ArrowButton.Direction;

public class GameScene extends Scene {
	public enum MatchState { ON_GOING, WIN, LOSE };
	
	private MatchState matchState;
	
	private City cities[];
	private Map map;
	
	private float currentGameSpeed;
	private float nextGameSpeed;
	
	private Vector2 timeControllerPosition;
	
	private ArrowButton[] timeControll = new ArrowButton[2];
	
	private Animation topBar;
	
	public GameScene() {
		super();
		AssetManager.load();
		map = new Map(this);
		
		City playerCity = new City(this, new PlayerController(), new Vector2(3, 3));
		
		cities = new City[] {
				playerCity,
				new City(this, new AIController3(playerCity), new Vector2(25, 3))
		};
		
		for(int i = 0; i < cities.length; i++) {
			addObject(cities[i]);
		}
		
		map.setupGame(cities, this);
		
		this.currentGameSpeed = 2;
		this.nextGameSpeed = 2;
		
		timeControllerPosition = new Vector2(-150, 300);
		
		timeControll[0] = new ArrowButton(new Rectangle(timeControllerPosition.x-70, timeControllerPosition.y, 64, 64), ArrowButton.Direction.UP);
		timeControll[1] = new ArrowButton(new Rectangle(timeControllerPosition.x-70, timeControllerPosition.y-70, 64, 64), ArrowButton.Direction.DOWN);
		
		this.getCamera().position.set(new Vector3(playerCity.getPosition().x, playerCity.getPosition().y, 0));
		
		this.matchState = MatchState.ON_GOING;
		
		this.topBar = new Animation(AssetManager.getTexture("topBar"));
		topBar.setPosition(-Gdx.graphics.getWidth(), Gdx.graphics.getWidth()/4);
		topBar.setSize(Gdx.graphics.getWidth()*2, 256);
		
		addObject(new Controller());
	}
	
	public void initialize() {
		
	}
	
	public void update(float dt) {
		super.update(dt*currentGameSpeed);
		
		currentGameSpeed = MathUtils.lerp(currentGameSpeed, nextGameSpeed, 0.1f);
		
		for(int i = 0; i < timeControll.length; i++) {
			timeControll[i].update();
			
			nextGameSpeed += timeControll[i].getValue();
		}
		
		nextGameSpeed = MathUtils.clamp(nextGameSpeed, 1, 5);
		
		matchUpdate();
	}
	
	public void draw(SpriteBatch batch) {
		map.draw(batch);
		super.draw(batch);
	}
	
	public void drawUi(SpriteBatch batch) {		
		//topBar.draw(batch);
		super.drawUi(batch);
		
		if(matchState == MatchState.ON_GOING) {
			AssetManager.font.draw(batch, "GAME SPEED x " + (int)currentGameSpeed, this.timeControllerPosition.x, this.timeControllerPosition.y + 16);

			for(int i = 0; i < timeControll.length; i++) {
				timeControll[i].draw(batch);
			}
		} else if(matchState == MatchState.LOSE) {
			AssetManager.font.draw(batch, "DEFEATED", 0, 0);
		} else {
			AssetManager.font.draw(batch, "VICTORY", 0, 0);
		}
	}
	
	public void matchUpdate() {
		for(int i = 0; i < cities.length; i++) {	
			if(!citiesCharactersLeft(cities[i]) && !citiesHousesLeft(cities[i])) {
				if(cities[i].getController() instanceof PlayerController) {
					matchState = MatchState.LOSE;
				} else {
					matchState = MatchState.WIN;
				}
				for(GameObject g : getObjects()) {
					if(g instanceof Controller) removeObject(g);
				}
			}
		}
	}
	
	private boolean citiesCharactersLeft(City city) {
		boolean noLeft = true;
		
		for(GameObject g : getObjects()) {
			if(g instanceof Character) {
				if(((Character) g).getCity() == city) {
					noLeft = false;
				}
			}
		}
		return !noLeft;
	}
	
	private boolean citiesHousesLeft(City city) {
		boolean noLeft = true;
		
		for(GameObject g : getObjects()) {
			if(g instanceof Building) {
				if(((Building) g).getCity() == city && ((Building)g).getType().getType() == BuildingType.Type.Home && ((Building)g).isBuilt()) {
					noLeft = false;
				}
			}
		}
		return !noLeft;
	}
	
	public MatchState getMatchState() {
		return this.matchState;
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
