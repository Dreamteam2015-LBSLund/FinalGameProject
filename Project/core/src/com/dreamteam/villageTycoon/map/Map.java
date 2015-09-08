package com.dreamteam.villageTycoon.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Map {
	final static int WIDTH = 100, HEIGHT = 100;
	private Tile[][] tiles;
	
	private ArrayList<TileType> tileTypes;
	
	public Map() {
		tileTypes = TileType.loadAll();
		//TODO: Tom's map generation goes here
		tiles = new Tile[WIDTH][HEIGHT];
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				tiles[x][y] = new Tile(new Vector2(x * Tile.WIDTH, y * Tile.HEIGHT), tileTypes.get(MathUtils.random(tileTypes.size() - 1)));
			}
		}
	}
	
	public void draw(SpriteBatch batch) {
		for (Tile[] ta : tiles) for (Tile t : ta) t.draw(batch);
	}
}
