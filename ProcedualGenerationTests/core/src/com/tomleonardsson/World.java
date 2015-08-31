package com.tomleonardsson;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class World {
	int[][] map;
	
	int mapWidth;
	int mapHeight;
	
	int sparseNess = 5000;
	int amountOfLakes = 2;
	
	int villageX;
	int villageY;
	
	Random random = new Random();
	
	public World(int mapWidth, int mapHeight){
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		map = new int[mapWidth][mapHeight];
	}
	
	public void create(){
		amountOfLakes = 2;
		Game.walkers.clear();
		
		for(int y = 0; y < map[0].length; y++){
			for(int x = 0; x < map.length; x++){
				map[x][y] = 1;
			}
		}
		
		for(int i = 0; i < sparseNess; i++){
			int x = random.nextInt(map.length);
			int y = random.nextInt(map[0].length);
			
			map[x][y] = 0;
		}
		
		int amountOfStones = random.nextInt(5)+1;
		
		for(int i = 0; i < amountOfStones; i++){
			int x = random.nextInt(map.length);
			int y = random.nextInt(map[0].length);
			
			Globals.circle(x, y, random.nextInt(5)+1, 3);
		}
		
		villageX = random.nextInt(42)+10;
		villageY = random.nextInt(40)+10;
		Globals.addVillege(villageX, villageY, 8);
		
		for(int i = 0; i < amountOfLakes; i++){
			int lakeY = random.nextInt(random.nextInt(42)+10);
			int lakeX = random.nextInt(random.nextInt(42)+10);
			
			while(Globals.getDistance(lakeX, lakeY, villageX, villageY) >= 10){
				lakeY = random.nextInt(random.nextInt(42)+10);
				lakeX = random.nextInt(random.nextInt(42)+10);
				
				if(Globals.getDistance(lakeX, lakeY, villageX, villageY) >= 10 && amountOfLakes > 0){
					amountOfLakes -= 1;
					Globals.addSurface(lakeX, lakeY, 4, 4);
					Game.walkers.add(new Walker(lakeY, lakeX, 0, 5, 4, 150));
				}
			}
		}
	}
	
	public void draw(SpriteBatch batch){
		for(int y = 0; y < map[0].length; y++){
			for(int x = 0; x < map.length; x++){
				batch.draw(AssetManager.tilesheet, x*8, -y*8+480-8, map[x][y]*8, 0, 8, 8);
			}
		}
	}
}
