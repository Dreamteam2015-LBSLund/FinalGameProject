package com.dreamteam.villageTycoon.map;

import com.badlogic.gdx.math.Vector2;

public class Tile {
	public final static float WIDTH = 1, HEIGHT = 1;
	
	private TileType type;
	
	public TileType getType() {
		return type;
	}

	private boolean hasBuilding; //should be set to true when a building is built on this tile. observer?
	
	public Tile(Vector2 position, TileType type) {
		this.type = type;
	}
	
	public boolean isWalkable() {
		return type.isWalkable() && hasBuilding;
	}
}
