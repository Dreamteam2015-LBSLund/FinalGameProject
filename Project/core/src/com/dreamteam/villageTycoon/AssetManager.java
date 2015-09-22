package com.dreamteam.villageTycoon;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public abstract class AssetManager {
	
	private static HashMap<String, TextureRegion> regions;
	
	public static BitmapFont font;
	
	public static void load() {
		font = new BitmapFont();
		
		regions = new HashMap<String, TextureRegion>();
		Texture spriteSheet = new Texture("textures/spritesheet.png");
		
		ResourceReader r = new ResourceReader(Gdx.files.internal("textures/data.gd"));
		for (String s : r.getAllKeys()) {
			String[] c = r.getList(s, true);
			regions.put(s, new TextureRegion(spriteSheet, Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3])));
		}
	}
	
	public static TextureRegion getTexture(String name) {
		if (!regions.containsKey(name)) {
			System.out.println("WARNING: Sprite " + name + " couldnt be found");
			return regions.get("error");
		}
		else return regions.get(name);
	}
}
