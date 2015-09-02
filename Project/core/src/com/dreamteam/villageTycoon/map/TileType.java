package com.dreamteam.villageTycoon.map;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class TileType {
	//resources are added dynamically. 
	//TileType, when constructed, will find all resources that exist in the ground for mining
	//also objects that spawn on top, Trees in the forest for example
	private boolean isWalkable, isBuildable;
	private String name;
	private String[] resources;
	private TextureRegion sprite; // transition tiles?
	
	//ArrayList<Resource> 
	
	//should only be called on startup!
	public TileType(File data) {
		ResourceReader r = new ResourceReader(data.getAbsolutePath());
		isWalkable = r.getBool("isWalkable");
		isBuildable = r.getBool("isBuildable");
		name = r.getString("name");
		resources = r.getList("resources"); // should be a list of resources, which means they should be loaded before
		//sprite = AssetManager.get(r.getString("sprite")); // assets maste alltsa laddas innan tiles (forst antagligen)
	}

	public static ArrayList<TileType> loadAll() {
		ArrayList<TileType> out = new ArrayList<TileType>();
		File[] files = new FileHandle(ResourceReader.getAssetPath() + "/tileTypes").file().listFiles();
		for (File f : files) {
			out.add(new TileType(f));
			System.out.println("Loaded tile type " + out.get(out.size() - 1).getName());
		}
		return null;
	}

	//Tile.isWalkable should probably be used since this does not take building into account
	public boolean isWalkable() {
		return isWalkable;
	}

	public boolean isBuildable() {
		return isBuildable;
	}
	
	public String getName() { return name; }
}