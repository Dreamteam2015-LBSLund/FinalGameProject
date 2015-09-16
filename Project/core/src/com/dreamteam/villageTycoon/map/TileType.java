package com.dreamteam.villageTycoon.map;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class TileType {
	//resources are added dynamically. 
	//TileType, when constructed, will find all resources that exist in the ground for mining
	//also objects that spawn on top, Trees in the forest for example
	private boolean isWalkable, isBuildable;
	private String name;
	private String[] resources;
	private TextureRegion sprite; // transition tiles?
	private float gCost; // for pathfinding
	
	//ArrayList<Resource> 
	
	//should only be called on startup!
	public TileType(ResourceReader r) {
		isWalkable = r.getBool("isWalkable");
		isBuildable = r.getBool("isBuildable");
		name = r.getString("name");
		resources = r.getList("resources"); // should be a list of resources, which means they should be loaded before
		if (resources == null) resources = new String[0];
		if (isWalkable) gCost = r.getFloat("gCost");
		sprite = AssetManager.getTexture(r.getString("sprite")); // assets maste alltsa laddas innan tiles (forst antagligen)
	}

	public static ArrayList<TileType> loadAll() {
		ArrayList<TileType> out = new ArrayList<TileType>();
		for (ResourceReader r : ResourceReader.readObjectList(Gdx.files.internal("tiletypes.gd"))) {
			out.add(new TileType(r));
		}
		return out;
	}
	
	public float getG() {
		return gCost;
	}

	//Tile.isWalkable should probably be used since this does not take buildings into account
	public boolean isWalkable() {
		return isWalkable;
	}

	public boolean isBuildable() {
		return isBuildable;
	}
	
	public TextureRegion getSprite() {
		return sprite;
	}
	
	public String getName() { return name; }
}