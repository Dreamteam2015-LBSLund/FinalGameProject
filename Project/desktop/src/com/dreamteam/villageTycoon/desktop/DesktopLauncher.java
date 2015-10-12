package com.dreamteam.villagetycoon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dreamteam.villageTycoon.Game;

public class DesktopLauncher {
	final static boolean FULLSCREEN = false;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		if (FULLSCREEN) {
			config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
			config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
			config.fullscreen = true;
		} else {
			config.width = 800;
			config.height = 450;
		}
		config.foregroundFPS = 0;
		new LwjglApplication(new Game(), config);
	}
}
