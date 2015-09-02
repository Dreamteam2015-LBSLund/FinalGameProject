package com.dreamteam.villageTycoon.map;

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
	public TileType(String filename) {
		ResourceReader r = new ResourceReader("tiletypes/grass.tt");
		isWalkable = r.getBool("isWalkable");
		isBuildable = r.getBool("isBuildable");
		name = r.getString("name");
		resources = r.getList("resources");
	}

	public TileType(String name, boolean isWalkable, boolean isBuildable) {
		this.name = name;
		this.isWalkable = isWalkable;
		this.isBuildable = isBuildable;
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