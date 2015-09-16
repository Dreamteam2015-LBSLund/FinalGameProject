package com.dreamteam.villageTycoon.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Point;

public class Map {
	final static int WIDTH = 100, HEIGHT = 100;
	private Tile[][] tiles;
	private HashMap<String, PropType> propTypes;
	private HashMap<String, TileType> tileTypes;
	
	public Map() {
		tileTypes = TileType.loadAll();
		propTypes = PropType.loadAll();

		tiles = new Tile[WIDTH][HEIGHT];
		String[][] map = generateMap();
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				tiles[x][y] = new Tile(new Vector2(x * Tile.WIDTH, y * Tile.HEIGHT), tileTypes.get(map[x][y])); // tileType is null since keys are strings, not ints
			}
		}
	}
	
	public String[][] generateMap() {
		// I'll start by doing something somewhat simple so that the project progesses towards the minimal viable product and then I can make it more advanced later
		Random random = new Random();
		
		final int TREE = 3;
		final int WATER = 1;
		final int DIRT = 2;
		final int GRASS = 0;
		
		String[] tileTypes = new String[] { "Grass", "Water", "Dirt", "treeTrunk" };
		
		int[][] map = new int[WIDTH][HEIGHT];
		
		String[][] mapString = new String[WIDTH][HEIGHT];
		
		Point[] lakes = new Point[3];
		Point[] villages = new Point[4];
		
		int treeSparseness = 21100;
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				map[x][y] = 3; //TODO: the order might change. The list of tileTypes should probably be a HashMap instead. S� kallat strings eller
			}
		}
		
		for (int i = 0; i < treeSparseness; i++) {
			int plotX = random.nextInt(WIDTH);
			int plotY = random.nextInt(HEIGHT);
			
			map[plotX][plotY] = 0;
		}
		
		for (int i = 0; i < lakes.length; i++) {
			lakes[i] = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));
			map = field(lakes[i].x, lakes[i].y, random.nextInt(5)+3, random.nextInt(5)+3, 1, map);
		}
		
		for (int i = 0; i < villages.length; i++) {
			boolean canPlace = false;
			
			villages[i] = new Point(random.nextInt(WIDTH-10)+10, random.nextInt(HEIGHT-10)+10);
			
			while (!canPlace) {
				for(int j = 0; j < lakes.length; j++) {
					villages[i] = new Point(random.nextInt(WIDTH-10)+10, random.nextInt(HEIGHT-10)+10);
					canPlace = getDistance((float)villages[i].x, (float)villages[i].y, (float)lakes[j].x, (float)lakes[j].y) >= 10;
					if(i > 0) canPlace = getDistance((float)villages[i].x, (float)villages[i].y, (float)villages[i-1].x, (float)villages[i-1].y) >= 20;
				}
			}
			
			map = field(villages[i].x, villages[i].y, random.nextInt(5)+4, random.nextInt(1), 2, map);
		}
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				mapString[x][y] = tileTypes[map[x][y]];
			}
		}

		return mapString;
	}
	
	public int[][] field(int x, int y, int size, int amountOfPlots, int tile, int[][] mapToEdit) {
		int[][] map = mapToEdit;
		
		Random random = new Random();
		
		map = circle(x, y, size, tile, map);
		
		for(int i = 0; i < amountOfPlots; i++) {
			int offsetX = random.nextInt(size);
			int offsetY = random.nextInt(size);
			
			if(offsetX >= 0 && offsetX <= WIDTH && offsetY >= 0 && offsetY <= HEIGHT)
			map = circle(x+(size/2)+offsetX, y+(size/2)+offsetY, size, tile, map);
		}
		
		return map;
	}
	
	public int[][] circle(int x, int y, int diameter, int tile, int[][] mapToEdit) {
		int[][] map = mapToEdit;
		
		for(int y2 = 0; y2 < diameter*2; y2++){
			for(int x2 = 0; x2 < diameter*2; x2++){
				if(y + x2 >= 0 && y + x2 <= map.length && x + y2 >= 0 &&  x + y2 <= map[0].length-1)  
					if(getDistance(x+diameter, y+diameter, x + y2, y + x2) <= diameter) {
						if(y + x2 >= 0 && y + x2 <= WIDTH-1 && x + y2 >= x + y2 && x + y2 <= HEIGHT) map[y + x2][x + y2] = tile;
					}
			}
		}
		
		return map;
	}
	
	// Visste inte vart jag skulle l�gga den
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
