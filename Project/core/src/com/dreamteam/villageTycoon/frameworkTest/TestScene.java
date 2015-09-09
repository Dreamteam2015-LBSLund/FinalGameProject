package com.dreamteam.villageTycoon.frameworkTest;

import com.dreamteam.villageTycoon.characters.Controller;
import com.dreamteam.villageTycoon.characters.SabotageKit;
import com.dreamteam.villageTycoon.characters.SabotageKitType;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.characters.SoldierType;
import com.dreamteam.villageTycoon.characters.WeaponType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.ActivationType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.EffectType;
import com.dreamteam.villageTycoon.characters.WeaponType.Type;
import com.dreamteam.villageTycoon.characters.Character;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.Map;
import com.dreamteam.villageTycoon.map.TileType;

public class TestScene extends Scene {
	private Map map;
	
	public TestScene() {
		super();

		AssetManager.load();
		
		map = new Map();
		
		addObject(new TestObject(AssetManager.getTexture("test")));
		addObject(new Controller());
		//addObject(new TestObject(AssetManager.getTexture("test")));
		//addObject(new Character(new Vector2(0, 0), new Animation(AssetManager.getTexture("test"))));
		//addObject(new Character(new Vector2(0, -1.5f), new Animation(AssetManager.getTexture("test"))));
		//addObject(new Character(new Vector2(0, -2.5f), new Animation(AssetManager.getTexture("test"))));
		//addObject(new Character(new Vector2(0, -4.5f), new Animation(AssetManager.getTexture("test"))));
		
		// x-types will be pre-defined so it won't look this messy later
		
		/*addObject(new Soldier(new Vector2(2, 2), new WeaponType("pistol", 1, 1, 1, 1, 1, new Sprite(AssetManager.getTexture("gun")), Type.HANDGUN), 
				new SoldierType(1, 1, 1, 1, new Animation(AssetManager.getTexture("test"))), 
				new SabotageKit[]{ 
						new SabotageKit(new SabotageKitType("motolv coctalil", 1, 1, new Sprite(AssetManager.getTexture("firekit")), ActivationType.INSTANT, EffectType.FIRE)), 
						new SabotageKit(new SabotageKitType("motolv coctalil", 1, 1, new Sprite(AssetManager.getTexture("firekit")), ActivationType.INSTANT, EffectType.FIRE)) }));
	*/
	}
	
	public Map getMap() {
		return map;
	}
	
	public void draw(SpriteBatch batch) {
		map.draw(batch);
		super.draw(batch);
	}
}
