package com.dreamteam.villageTycoon.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tile {
	public final static float WIDTH = 1, HEIGHT = 1;
	
	private TileType type;
	private Vector2 position;
	private boolean hasBuilding; //should be set to true when a building is built on this tile. observer?
	
	public Tile(Vector2 position, TileType type) {
		this.type = type;
		this.position = position;
	}
	
	public TileType getType() {
		return type;
	}
	
	public boolean isWalkable() {
		return type.isWalkable() && !hasBuilding;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(type.getSprite(), position.x, position.y, WIDTH, HEIGHT);
	}
}
