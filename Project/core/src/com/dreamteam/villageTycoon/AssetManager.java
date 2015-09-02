package com.dreamteam.villageTycoon;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public abstract class AssetManager {
	
	private static HashMap<String, TextureRegion> regions;
	
	public static void load() {
		regions = new HashMap<String, TextureRegion>();
		Texture spriteSheet = new Texture("textures/spritesheet.png");
		
		ResourceReader r = new ResourceReader(ResourceReader.getAssetPath() + "/textures/data.t");
		for (String s : r.getAllKeys()) {
			String[] c = r.getList(s);
			regions.put(s, new TextureRegion(spriteSheet, Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3])));
		}
	}
	
	public static TextureRegion getTexture(String name) {
		return regions.get(name);
	}
}