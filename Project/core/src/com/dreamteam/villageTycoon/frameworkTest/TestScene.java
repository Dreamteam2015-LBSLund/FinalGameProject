package com.dreamteam.villageTycoon.frameworkTest;

import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Scene;

public class TestScene extends Scene {
	public TestScene() {
		super();
		AssetManager.load();
		addObject(new TestObject(AssetManager.getTexture("test")));
	}
}
