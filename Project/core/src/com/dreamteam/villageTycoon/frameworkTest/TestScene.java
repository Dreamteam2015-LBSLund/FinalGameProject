package com.dreamteam.villageTycoon.frameworkTest;

import com.badlogic.gdx.Gdx;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.*;

public class TestScene extends Scene {
	public TestScene() {
		super();
		addObject(new TestObject());
		TileType.loadAll();
	}
}
