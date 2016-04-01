package com.dreamteam.villageTycoon.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.AIController;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.Point;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.utils.ResourceReader;
import com.dreamteam.villageTycoon.workers.Worker;

public class Map {
	public final static int WIDTH = 100, HEIGHT = 100;
	
	final int LANGTON_LIMIT = 2000;
	
	private Tile[][] tiles;
	private HashMap<String, PropType> propTypes;
	private HashMap<String, TileType> tileTypes;
	private HashMap<String, BuildingType> buildingTypes;
	
	private int amountOfCities;
	private Point[] cityPositions;
	
	private int timesDone;
	
	City city;
	
	//City[] cities;
	
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
		
		//ities = new City[amountOfCities];
		
		for(int i = 0; i < amountOfCities; i++) {
			//cities[i] =  new City(scene, new AIController(), new Vector2()); //TODO: cities need to get a position
		}
	}

	public void setupGame(City[] cities, Scene scene) {
		Vector2 midPoint = new Vector2(WIDTH/2, HEIGHT/2);
		
		float angleInterval = ((float)Math.PI*2)/cities.length;
		
		float distance = (WIDTH/2)-15;
		
		for(int i = 0; i < cities.length; i++) {
			cities[i].setPosition(new Vector2(midPoint.x + (float)Math.cos(angleInterval*i) * distance, midPoint.y + (float)Math.sin(angleInterval*i) * distance));
		}
		
		for(int i = 0; i < cities.length; i++) {
			if(cities[i].getController() instanceof PlayerController) {
				Building startBuilding = new Building(cities[i].getPosition().cpy(), BuildingType.getTypes().get("house"), cities[i]);
				scene.addObject(startBuilding);
				cities[i].addBuilding(startBuilding);
			} else {
				generateCity(cities[i].getPosition().cpy(), 1, 2, scene, cities[i]);
			}
			
			for(int j = 0; j < 5; j++) {
				Vector2 buildingPos = cities[i].getPosition().cpy(); //new Vector2(midPoint.x + (float)Math.cos(angleInterval*i) * distance, midPoint.y + (float)Math.sin(angleInterval*i) * distance);
				Worker w = new Worker(buildingPos.add(new Vector2(randomInt(-5, 5), randomInt(-5, 5))), cities[i]);
				scene.addObject(w);
			}
			
			cities[i].init();
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
		
		for(int i = 0; i < 500; i++) {
			map[randomInt(0, WIDTH-1)][randomInt(0, HEIGHT-1)] = "treeTrunk";
		}
		
		for(int i = 0; i < 5; i++) {
			antIt(randomInt(0, WIDTH-1), randomInt(0, HEIGHT-1), 0, map, new String[]{"Grass", "treeTrunk"});
			timesDone = 0;
		}
		
		for (int i = 0; i < lakes.length; i++) {		
			lakes[i] = new Point(randomInt(40, WIDTH-40), randomInt(40, HEIGHT-40));		
			//map = field(lakes[i].x, lakes[i].y, random.nextInt(5)+3, random.nextInt(5)+3, "Water", map);
			createField(lakes[i].x, lakes[i].y, 20+random.nextInt(5), 20+random.nextInt(5), 1, 800, new String[]{"Grass", "Water"}, map);
		}
		cityPositions = new Point[villages.length];
		
		for (int i = 0; i < villages.length; i++) {
			villages[i] = new Point(random.nextInt(WIDTH-10)+10, random.nextInt(HEIGHT-10)+10);
			cityPositions[i] = villages[i];
			//map = field(villages[i].x, villages[i].y, random.nextInt(5)+4, random.nextInt(1), "Dirt", map);
			createField(villages[i].x, villages[i].y, 20, 20, 10, 800, new String[]{"Grass", "Dirt"}, map);
		}
		
		amountOfCities = villages.length;
		
		createField(10, 70, 20, 20, 10, 4, new String[]{"Grass", "Dirt"}, map);
		
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

		String farmType = (techLevel >= 2) ? "advancedFarm" : "farm";

		addCityPart(size, farmType, position, 1, 3, city, random, scene);
		
		String industryType = (techLevel >= 1) ? "mine" : "woodshop";
		addCityPart(size, "mine", position, 4, 6, city, random, scene);
		
		addCityPart(size, "bakery", position, 5, 8, city, random, scene);
		addCityPart(size, "flourMill", position, 10, 12, city, random, scene);
		addCityPart(size, "wheatFarm", position, 12, 16, city, random, scene);
		
		//addCityPart(size/2, "armyBarack", position, -size/2, size/2, city, random, scene);
	}
	
	public void addCityPart(int amount, String type, Vector2 position, int min, int max, City city, Random random, Scene scene) {
		Building buildingToAdd = null;
		
		ArrayList<Building> buildingsAdded = new ArrayList<Building>();
		
		for(int i = 0; i < amount; i++) {
			int offset = randomInt(min, max);
			int angle = randomInt(0, 360);
			Vector2 newPosition = new Vector2(position.x + (float)Math.cos(angle * ((float)Math.PI/180))*offset, position.y + (float)Math.sin(angle * ((float)Math.PI/180))*offset);
			buildingToAdd = new Building(newPosition, BuildingType.getTypes().get(type), city);

			while(!canAddBuilding(city.getBuildings(), buildingToAdd) || !canAddBuilding(buildingsAdded , buildingToAdd)) {
				offset = randomInt(min, max);
				angle = randomInt(0, 360);

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
				canPlace = (b.distanceTo(building.getPosition().cpy()) < 5);
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
	
	// Major key in post-spaghetti is using the same word for different things
	public void createField(int x, int y, int width, int height, int plotAmount, int thickness, String[] tiles, String[][] map) {
		String tmp[][] = fillAntIt(width, height, plotAmount, thickness, tiles);

		replaceSection(map, tmp, x, y, tiles[0]);
	}
	
	public void replaceSection(String[][] plane, String[][] section, int offsetX, int offsetY, String tileToIngnore) {
		for(int x = 0; x < section.length; x++) {
			for(int y = 0; y < section[1].length; y++) {
				if(section[x][y] != tileToIngnore && x+offsetX >= 0 && x+offsetX <= plane.length-1 && y+offsetY >= 0 && y+offsetY <= plane[1].length-1) plane[x+offsetX][y+offsetY] = section[x][y];
			}
		}
	}
	
	public String[][] fillAntIt(int width, int height, int plotAmount, int thickness, String[] tiles) {
		String tmp[][] = new String[width][height];
		
		for(int x = 0; x < tmp.length; x++) {
			for(int y = 0; y < tmp[1].length; y++) {
				tmp[x][y] = tiles[0];
			}
		}
		
		Random random = new Random();
		
		for(int i = 0; i < plotAmount; i++) {
			plot(random.nextInt(width), random.nextInt(height), tiles[1], tmp);
		}
		
		timesDone = 0;
		antIt(width/2, height/2, random.nextInt(4), tmp, tiles);
		
		boolean[] sidesFilled = new boolean[4];
		
		for(int j = 0; j < thickness; j++) {
			for(int x = 1; x < tmp.length-1; x++) {
				for(int y = 1; y < tmp[1].length-1; y++) {
					if(tmp[x+1][y] == tiles[1]) sidesFilled[0] = true;
					else sidesFilled[0] = false;
					if(tmp[x][y-1] == tiles[1]) sidesFilled[1] = true;
					else sidesFilled[1] = false;
					if(tmp[x][y+1] == tiles[1]) sidesFilled[2] = true;
					else sidesFilled[2] = false;
					if(tmp[x][y-1] == tiles[1]) sidesFilled[3] = true;
					else sidesFilled[3] = false;
				
					int count = 0;
					for(int i = 0; i < sidesFilled.length; i++) {
						if(sidesFilled[i]) count += 1;
					}
				
					if(count >= 3) {
						tmp[x][y] = tiles[1];
					}
				}
			}	
		}
		
		return tmp;
	}
	
	public void antIt(int x, int y, int direction, String[][] map, String[] tiles) {
		boolean canMove = true;
		
		timesDone += 1;
		
		if(x >= 0 && x < map.length && y >= 0 && y < map[1].length) {
			if(map[x][y] == tiles[0]) {
				plot(x, y, tiles[1], map);
				direction -= 1;
				if(direction <= -1) direction = 3;
			} else {
				plot(x, y, tiles[0], map);
				direction += 1;
				if(direction >= 4) direction = 0;
			}
		}
		
		if(direction == 0 && x == WIDTH - 1) {
			canMove = false;
		}
		
		if(direction == 2 && x == 0) {
			canMove = false;
		}
		
		if(direction == 1 && y == 0) {
			canMove = false;
		}
		
		if(direction == 3 && y == HEIGHT-1) {
			canMove = false;
		}
		
		if(canMove && timesDone <= LANGTON_LIMIT) {
			if(direction == 0) {
				antIt(x+1, y, direction, map, tiles);
			}
			if(direction == 1) {
				antIt(x, y-1, direction, map, tiles);
			}
			if(direction == 2) {
				antIt(x-1, y, direction, map, tiles);
			}
			if(direction == 3) {
				antIt(x, y+1, direction, map, tiles);
			}
		}
	}
	
	public void plot(int x, int y, String n, String[][] map) {
		map[x][y] = n;
	}
	
	// VIsste inte vart jag skulle lägga denna
	public int randomInt(int min, int max) {
	    Random random = new Random();

	    int randomNumber = random.nextInt((max - min) + 1) + min;

	    return randomNumber;
	}
}
