package com.dreamteam.villageTycoon.frameworkTest;

import com.dreamteam.villageTycoon.characters.Controller;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.characters.SoldierType;
import com.dreamteam.villageTycoon.characters.WeaponType;
import com.dreamteam.villageTycoon.characters.WeaponType.Type;
import com.dreamteam.villageTycoon.characters.Character;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.Scene;

public class TestScene extends Scene {
	public TestScene() {
		super();

		//addObject(new TestObject(null));
		addObject(new Controller());

		AssetManager.load();
		//addObject(new TestObject(AssetManager.getTexture("test")));
		//addObject(new Character(new Vector2(0, 0), new Animation(AssetManager.getTexture("test"))));
		//addObject(new Character(new Vector2(0, -1.5f), new Animation(AssetManager.getTexture("test"))));
		//addObject(new Character(new Vector2(0, -2.5f), new Animation(AssetManager.getTexture("test"))));
		//addObject(new Character(new Vector2(0, -4.5f), new Animation(AssetManager.getTexture("test"))));
		
		addObject(new Soldier(new Vector2(2, 2), new WeaponType("pistol", 1, 1, 1, 1, 1, new Sprite(AssetManager.getTexture("test")), Type.HANDGUN), 
				new SoldierType(1, 1, 1, 1, new Animation(AssetManager.getTexture("test")))));
	}
}
