package com.dreamteam.villageTycoon.frameworkTest;

<<<<<<< HEAD
import com.dreamteam.villageTycoon.characters.Controller;
=======
import com.dreamteam.villageTycoon.AssetManager;
>>>>>>> origin/master
import com.dreamteam.villageTycoon.framework.Scene;

public class TestScene extends Scene {
	public TestScene() {
		super();
<<<<<<< HEAD
		addObject(new TestObject());
		addObject(new Controller());
=======
		AssetManager.load();
		addObject(new TestObject(AssetManager.getTexture("test")));
>>>>>>> origin/master
	}
}
