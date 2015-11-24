package com.dreamteam.villageTycoon.map;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class PropType {
	private static HashMap<String, PropType> types;
	
	private Resource resource;
	private TextureRegion texture;
	private String name;
	
	public PropType(ResourceReader r) {
		resource = Resource.get(r.getString("resource"));
		texture = AssetManager.getTexture(r.getString("texture"));
		name = r.getObjectName();
	}
	
	public Resource getResource() {
		return resource;
	}
	
	public TextureRegion getTexture() {
		return texture;
	}
	
	//loads all types if that hasn't been done yet, and returns them
	public static HashMap<String, PropType> getTypes() {
		if (types == null) {
			types = new HashMap<String, PropType>();
			for (ResourceReader r : ResourceReader.readObjectList(Gdx.files.internal("props.gd"))) {
				types.put(r.getObjectName(), new PropType(r));
			}	
		}
		return types;
	}
	
	// returns a loaded type with that name
	public static PropType getType(String name) {
		PropType p = getTypes().get(name);
		if (p == null) {
			System.out.println("WARNING: Prop " + name + " couln't be found");
		}
		return p;
	}
	
	public String getName() {
		return name;
	}
}
