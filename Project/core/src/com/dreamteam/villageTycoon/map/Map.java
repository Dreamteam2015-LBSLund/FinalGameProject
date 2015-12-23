package com.dreamteam.villageTycoon.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.ai.AIController;
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
	
	private int amountOfCities;
	private Point[] cityPositions;
	
	City city;
	
	City[] cities;
	
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
		
		cities = new City[amountOfCities];
		
		for(int i = 0; i < amountOfCities; i++) {
			cities[i] =  new City(scene, new AIController(), new Vector2()); //TODO: cities need to get a position
		}
	}

	public void setupGame(City[] cities, Scene scene) {
		Vector2 midPoint = new Vector2(WIDTH/2, HEIGHT/2);
		
		float angleInterval = ((float)Math.PI*2)/cities.length;
		
		for(int i = 0; i < cities.length; i++) {
			float distance = this.randomInt((int)midPoint.x/2, (int)midPoint.x);
			
			if(i == 0) {
				scene.addObject(new Building(new Vector2(midPoint.x + (float)Math.cos(angleInterval*i) * distance, midPoint.y + (float)Math.sin(angleInterval*i) * distance), BuildingType.getTypes().get("house1"), cities[i]));
				cities[i].addBuilding(new Building(new Vector2(midPoint.x + (float)Math.cos(angleInterval*i) * distance, midPoint.y + (float)Math.sin(angleInterval*i) * distance), BuildingType.getTypes().get("house1"), cities[i]));
			} else {
				generateCity(new Vector2(midPoint.x + (float)Math.cos(angleInterval*i) * distance, midPoint.y + (float)Math.sin(angleInterval*i) * distance), 5, 2, scene, cities[i]);
			}
		}
	}
	
	public String[][] generateMap() {
		// I'll start by doing something somewhat simple so that the project progesses towards the minimal viable product and then I can make it more advanced later
		Random random = new Random();	

		String[][] map = new String[WIDTH][HEIGHT];

		Point[] lakes = new Point[3];
		Point[] villages = new Point[4];
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				map[x][y] = "Grass"; 
			}
		}
		for (int i = 0; i < lakes.length; i++) {		
			lakes[i] = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));		
			map = field(lakes[i].x, lakes[i].y, random.nextInt(5)+3, random.nextInt(5)+3, "Water", map);		
		}
		cityPositions = new Point[villages.length];
		
		for (int i = 0; i < villages.length; i++) {
			villages[i] = new Point(random.nextInt(WIDTH-10)+10, random.nextInt(HEIGHT-10)+10);
			cityPositions[i] = villages[i];
			map = field(villages[i].x, villages[i].y, random.nextInt(5)+4, random.nextInt(1), "Dirt", map);
		}
		
		amountOfCities = villages.length;
		
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
	
	public void generateCity(Vector2 position, int size, int techLevel, Scene scene, City city) {
		Random random = new Random();
		scene.addObject(city);
		
		// Houses for the workers should be the core of cities
		for(int x = 0; x < size/2; x++) {
			for(int y = 0; y < size/2; y++) {
				Vector2 buildingSize = new Vector2(4, 4);
				Vector2 center = new Vector2(position.x, position.y);
				//scene.addObject(new Building(center.add(new Vector2(x*buildingSize.x, y*buildingSize.y)), BuildingType.getTypes().get("house1"), city));
				//city.addBuilding(new Building(center.add(new Vector2(x*buildingSize.x, y*buildingSize.y)), BuildingType.getTypes().get("house1"), city));
			}
		}

		//Next up is agriculture
		String farmType = (techLevel >= 2) ? "advancedFarm" : "basicFarm";

		addCityPart(size/2, farmType, position, 0, 5, city, random, scene);
		
		String industryType = (techLevel >= 1) ? "factory1" : "woodshop";
		addCityPart(size/2, "factory1", position, 6, 10, city, random, scene);
		
		addCityPart(size/2, "bakery", position, 11, 20, city, random, scene);
		addCityPart(size/2, "flourMill", position, 21, 25, city, random, scene);
		addCityPart(size/2, "wheatFarm", position, 26, 30, city, random, scene);
		
		//addCityPart(size/2, "armyBarack", position, -size/2, size/2, city, random, scene);
	}
	
	public void addCityPart(int amount, String type, Vector2 position, int min, int max, City city, Random random, Scene scene) {
		Building buildingToAdd = null;
		
		ArrayList<Building> buildingsAdded = new ArrayList<Building>();
		
		for(int i = 0; i < amount; i++) {
			int offset = random.nextInt(max)+min;
			int angle = random.nextInt(360);
			Vector2 newPosition = new Vector2(position.x + (float)Math.cos(angle * ((float)Math.PI/180))*offset, position.y + (float)Math.sin(angle * ((float)Math.PI/180))*offset);
			buildingToAdd = new Building(newPosition, BuildingType.getTypes().get(type), city);

			while(!canAddBuilding(city.getBuildings(), buildingToAdd) || !canAddBuilding(buildingsAdded , buildingToAdd)) {
				offset = random.nextInt(max)+min;
				angle = random.nextInt(360);

				newPosition = new Vector2(position.x + (float)Math.cos(angle * ((float)Math.PI/180))*offset, position.y + (float)Math.sin(angle * ((float)Math.PI/180))*offset);
				buildingToAdd = new Building(newPosition, BuildingType.getTypes().get(type), city);
				buildingsAdded.add(buildingToAdd);
				
				if(canAddBuilding(city.getBuildings(), buildingToAdd) )
					break;
			}
			
			buildingsAdded.add(buildingToAdd);
			city.addBuilding(buildingToAdd);
			scene.addObject(buildingToAdd);
		}
	}
	
	public boolean canAddBuilding(ArrayList<Building> buildings, Building building) {
		boolean canPlace = false;
		
		for(Building b : buildings) {
			if(b != building)
				canPlace = (b.getHitbox().collision(building.getHitbox()));
		}
		
		return canPlace;
	}
	
	// Visste inte vart jag skulle lï¿½gga den
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
	
	// VIsste inte vart jag skulle lägga denna
	public int randomInt(int min, int max) {
	    Random random = new Random();

	    int randomNumber = random.nextInt((max - min) + 1) + min;

	    return randomNumber;
	}
}
