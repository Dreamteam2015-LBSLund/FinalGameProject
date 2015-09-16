package com.dreamteam.villageTycoon.map;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class PropType {
	private String resource;
	private TextureRegion texture;
	
	public PropType(ResourceReader r) {
		resource = r.getString("resource");
		texture = AssetManager.getTexture(r.getString("texture"));
	}
	
	public String getResource() {
		return resource;
	}
	
	public TextureRegion getTexture() {
		return texture;
	}
	
	
	public static HashMap<String, PropType> loadAll() {
		HashMap<String, PropType> out = new HashMap<String, PropType>();
		for (ResourceReader r : ResourceReader.readObjectList(Gdx.files.internal("props.gd"))) {
			out.put(r.getObjectName(), new PropType(r));
		}
		return out;
	}
}
