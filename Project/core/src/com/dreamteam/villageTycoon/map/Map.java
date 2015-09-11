package com.dreamteam.villageTycoon.map;

import java.util.ArrayList;
import java.util.Random;

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
		int[][] map = generateMap();
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				tiles[x][y] = new Tile(new Vector2(x * Tile.WIDTH, y * Tile.HEIGHT), tileTypes.get(map[x][y]));
			}
		}
	}
	
	public int[][] generateMap() {
		// I'll start by doing something somewhat simple so that the project progesses towards the minimal viable product and then I can make it more advanced later
		Random random = new Random();
		
		int[][] map = new int[WIDTH][HEIGHT];
		
		int treeSparseness = 21100;
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				map[x][y] = 3;
			}
		}
		
		for(int i = 0; i < treeSparseness; i++) {
			int plotX = random.nextInt(WIDTH);
			int plotY = random.nextInt(HEIGHT);
			
			map[plotX][plotY] = 0;
		}
		
		return map;
	}
	
	public int[][] circle(int x, int y, int diameter, int tile, int[][] mapToEdit){
		int[][] map = mapToEdit;
		
		for(int y2 = 0; y2 < diameter*2; y2++){
			for(int x2 = 0; x2 < diameter*2; x2++){
				if(y + x2 >= 0 && y + x2 <= map.length && x + y2 >= 0 &&  x + y2 <= map[0].length-1)  
					if(getDistance(x+diameter, y+diameter, x + y2, y + x2) <= diameter) map[y + x2][x + y2] = tile;
			}
		}
		
		return map;
	}
	
	// Visste inte vart jag skulle lägga den
	public float getDistance(float x1, float y1, float x2, float y2){
		float x = (x1 - x2);
		float y = (y1 - y2);
		
		return (float)(Math.sqrt((x*x)+(y*y)));
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public void draw(SpriteBatch batch) {
		for (Tile[] ta : tiles) for (Tile t : ta) t.draw(batch);
	}
}
