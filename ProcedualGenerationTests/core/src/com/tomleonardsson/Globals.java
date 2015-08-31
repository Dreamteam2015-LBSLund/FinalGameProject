package com.tomleonardsson;

import java.util.Random;

public class Globals {
	public static void line(int x, int y, int vx, int vy, int tile){
		
	}
	
	public static float getDistance(float x1, float y1, float x2, float y2){
		float x = (x1 - x2);
		float y = (y1 - y2);
		
		return (float)(Math.sqrt((x*x)+(y*y)));
	}
	
	public static void addVillege(int x, int y, int size){
		addSurface(x, y, size, 2);
		int offset = size-1;
		circle(x+offset, y+offset, 5, 2);
		Game.testWorld.map[y+offset][x+offset] = 5;
		Game.testWorld.map[y+1+offset][x+offset] = 6;
		Game.testWorld.map[y+offset][x+1+offset] = 7;
		Game.testWorld.map[y+1+offset][x+1+offset] = 8;
	}
	
	public static void addSurface(int x, int y, int size, int tile){
		Random random = new Random();
		
		for(int i = 0; i < size+2; i++){
			int circleSize = random.nextInt(10)+1;
			
			circle(x+random.nextInt(circleSize)-2, y+random.nextInt(circleSize)-2, circleSize, tile);
		}
	}
	
	public static void circle(int x, int y, int radius, int tile){
		radius /= 2;
		
		for(int y2 = 0; y2 < radius*2; y2++){
			for(int x2 = 0; x2 < radius*2; x2++){
				if(y + x2 >= 0 && y + x2 <= Game.testWorld.map.length && x + y2 >= 0 &&  x + y2 <= Game.testWorld.map[0].length-1)  if(getDistance(x+radius, y+radius, x + y2, y + x2) <= radius) Game.testWorld.map[y + x2][x + y2] = tile;
			}
		}
	}
	
	public static void floodFill(int x, int y, int toReplaceWith, int targetTile){
		Game.testWorld.map[x][y] = toReplaceWith;
		
		if(y != 0){
			if(Game.testWorld.map[x][y-1] == targetTile){
				floodFill(x, y-1, toReplaceWith, targetTile);
			}
		}
		if(y != Game.testWorld.map[0].length-1){
			if(Game.testWorld.map[x][y+1] == targetTile){
				floodFill(x, y+1, toReplaceWith, targetTile);
			}
		}
		if(x != 0){
			if(Game.testWorld.map[x-1][y] == targetTile){
				floodFill(x-1, y, toReplaceWith, targetTile);
			}
		}
		if(x != Game.testWorld.map.length-1){
			if(Game.testWorld.map[x+1][y] == targetTile){
				floodFill(x+1, y, toReplaceWith, targetTile);
			}
		}
	}
}
