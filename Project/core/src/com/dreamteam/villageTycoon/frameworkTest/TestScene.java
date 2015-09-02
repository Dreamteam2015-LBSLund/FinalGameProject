package com.dreamteam.villageTycoon.frameworkTest;

import com.dreamteam.villageTycoon.characters.Controller;
import com.dreamteam.villageTycoon.framework.Scene;

public class TestScene extends Scene {
	public TestScene() {
		super();
		addObject(new TestObject());
		addObject(new Controller());
	}
}
