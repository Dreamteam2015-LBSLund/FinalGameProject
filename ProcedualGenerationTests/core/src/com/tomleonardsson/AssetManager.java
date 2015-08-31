package com.tomleonardsson;

import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
	public static Texture tilesheet;
	
	public static void load(){
		tilesheet = new Texture("tiles.png");
	}
}
