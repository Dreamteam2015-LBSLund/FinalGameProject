package com.dreamteam.villageTycoon.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.framework.Point;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class Map {
	public final static int WIDTH = 100, HEIGHT = 100;
	private Tile[][] tiles;
	private HashMap<String, PropType> propTypes;
	private HashMap<String, TileType> tileTypes;
	private HashMap<String, BuildingType> buildingTypes;

	
	public Map(Scene scene) {
		Resource.getTypes();
		tileTypes = TileType.loadAll();
		propTypes = PropType.getTypes();
		buildingTypes = BuildingType.getTypes();
		

		tiles = new Tile[WIDTH][HEIGHT];
		String[][] map = generateMap();
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				tiles[x][y] = new Tile(new Vector2(x * Tile.WIDTH, y * Tile.HEIGHT), tileTypes.get(map[x][y]), scene); // tileType is null since keys are strings, not ints
			}
		}
	}
	
	public String[][] generateMap() {
		// I'll start by doing something somewhat simple so that the project progesses towards the minimal viable product and then I can make it more advanced later
		Random random = new Random();	

		String[][] map = new String[WIDTH][HEIGHT];

		Point[] lakes = new Point[3];
		Point[] villages = new Point[4];
		
		int treeSparseness = 2110000;
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				map[x][y] = "treeTrunk"; //TODO: the order might change. The list of tileTypes should probably be a HashMap instead. S� kallat strings eller
			}
		}
		
		for (int i = 0; i < treeSparseness; i++) {
			int plotX = random.nextInt(WIDTH);
			int plotY = random.nextInt(HEIGHT);
			
			map[plotX][plotY] = "Grass";
		}
		
		for (int i = 0; i < lakes.length; i++) {
			lakes[i] = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));
			map = field(lakes[i].x, lakes[i].y, random.nextInt(5)+3, random.nextInt(5)+3, "Water", map);
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
			
			map = field(villages[i].x, villages[i].y, random.nextInt(5)+4, random.nextInt(1), "Dirt", map);
		}
		
		return map;
	}
	
	public String[][] field(int x, int y, int size, int amountOfPlots, String tile, String[][] mapToEdit) {
		String[][] map = mapToEdit;
		
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
	
	public String[][] circle(int x, int y, int diameter, String tile, String[][] mapToEdit) {
		String[][] map = mapToEdit;
		
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
	
	public City generateCity(Vector2 position, int size, int techLevel, Scene scene) {
		City city = new City(scene);
		
		Random random = new Random();
		
		// Houses for the workers should be the core of cities
		for(int x = 0; x < size/2; x++) {
			for(int y = 0; y < size/2; y++) {
				Vector2 buildingSize = BuildingType.getTypes().get("house").getSprite().getSize();
				Vector2 center = new Vector2(position.x-size/4, position.y-size/4);
				city.addBuilding(new Building(center.add(new Vector2(x*buildingSize.x, y*buildingSize.y)), BuildingType.getTypes().get("house"), city));
			}
		}
		
		//Next up is agriculture
		
		
		return city;
	}
	
	public boolean canAddBuilding(ArrayList<Building> buildings, Building building) {
		boolean canPlace = false;
		
		for(Building b : buildings) {
			canPlace = (getDistance(b.getPosition().x, b.getPosition().y, building.getPosition().x, building.getPosition().y) > building.getSize().x);
		}
		
		return canPlace;
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

	public Tile tileAt(Vector2 target) {
		Point p = new Point((int)(target.x / Tile.WIDTH), (int)(target.y / Tile.HEIGHT));
		if (p.isOnArray(tiles)) {
			return tiles[p.x][p.y];
		}
		return null;
	}
}
