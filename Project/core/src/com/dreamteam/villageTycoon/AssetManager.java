package com.dreamteam.villageTycoon;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public abstract class AssetManager {
	
	private static HashMap<String, TextureRegion> regions;
	private static HashMap<String, Sound> sounds;
	
	public static BitmapFont font, smallFont;
	
	public static void load() {
		FreeTypeFontGenerator g = new FreeTypeFontGenerator(Gdx.files.internal("fonts/CALIST.TTF"));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 50;
		font = g.generateFont(param);
		param.size = 35;
		smallFont = g.generateFont(param);
		
		regions = new HashMap<String, TextureRegion>();
		Texture spriteSheet = new Texture("textures/spritesheet.png");
		
		ResourceReader r = new ResourceReader(Gdx.files.internal("textures/data.gd"));
		for (String s : r.getAllKeys()) {
			String[] c = r.getList(s, true);
			regions.put(s, new TextureRegion(spriteSheet, Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3])));
		}
		
		sounds = new HashMap<String, Sound>();
		
		r = new ResourceReader(Gdx.files.internal("soundEffects/data.gd"));
		for (String s : r.getAllKeys()) {
			String c = r.getString(s);
			sounds.put(s, Gdx.audio.newSound(Gdx.files.internal("soundEffects/" + c + ".wav")));
		}
	}
	
	public static TextureRegion getTexture(String name) {
		if (!regions.containsKey(name)) {
			System.out.println("WARNING: Sprite " + name + " couldnt be found");
			return regions.get("error");
		}
		else return regions.get(name);
	}
	
	public static Sound getSound(String name) {
		if (sounds == null) load();
		
		if (!sounds.containsKey(name)) {
			System.out.println("WARNING: Sound " + name + " couldnt be found");
			return null;
		}
		else return sounds.get(name);
	}
}
