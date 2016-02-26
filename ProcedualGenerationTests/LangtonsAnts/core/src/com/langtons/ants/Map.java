package com.langtons.ants;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {
	final int TILE_SIZE = 8;
	final int WIDTH = 640/TILE_SIZE;
	final int HEIGHT = 480 /TILE_SIZE;
	final int LIMIT = 5000/2;
	
	private int map[][];
	
	private Texture tileTex;
	private Texture tile;
	
	private int timesDone;
	
	public Map() {
		map = new int[WIDTH][HEIGHT];
		
		tileTex = new Texture("badlogic.jpg");
		tile = new Texture("badlogic.jpg");
		
		Random random = new Random();
		
		for(int i = 0; i < 200; i++) {	
			//plot(random.nextInt(WIDTH), random.nextInt(HEIGHT), 1, map);
		}
		
		//antIt(random.nextInt(WIDTH), random.nextInt(HEIGHT), random.nextInt(4), map);
		map = fillAntIt(50, 50, 50);
		
		System.out.println(map[1].length + " - " + HEIGHT + " | " + map.length + " - " + WIDTH);
		
		//antIt(WIDTH/2, HEIGHT/2, random.nextInt(4));
	}
	
	public void plot(int x, int y, int value) {
		map[x][y] = value;
	}
	
	public void plot(int x, int y, int value, int[][] plane) {
		plane[x][y] = value;
	}
	
	public int[][] fillAntIt(int width, int height, int plotAmount) {
		int tmp[][] = new int[width][height];
		
		Random random = new Random();
		
		for(int i = 0; i < plotAmount; i++) {
			plot(random.nextInt(width), random.nextInt(height), 1, tmp);
		}
		
		antIt(random.nextInt(width), random.nextInt(height), random.nextInt(4), tmp);
		
		boolean[] sidesFilled = new boolean[4];
		
		for(int j = 0; j < 50; j++) {
			for(int x = 1; x < tmp.length-1; x++) {
				for(int y = 1; y < tmp[1].length-1; y++) {
					if(tmp[x+1][y] == 1) sidesFilled[0] = true;
					else sidesFilled[0] = false;
					if(tmp[x][y-1] == 1) sidesFilled[1] = true;
					else sidesFilled[1] = false;
					if(tmp[x][y+1] == 1) sidesFilled[2] = true;
					else sidesFilled[2] = false;
					if(tmp[x][y-1] == 1) sidesFilled[3] = true;
					else sidesFilled[3] = false;
				
					int count = 0;
					for(int i = 0; i < sidesFilled.length; i++) {
						if(sidesFilled[i]) count += 1;
					}
				
					if(count >= 3) {
						//floodFill(x, y, 1, 0, tmp);
						tmp[x][y] = 1;
					}
				}
			}	
		}
		
		return tmp;
	}
	
	public void antIt(int x, int y, int direction, int[][] plane) {
		boolean canMove = true;
		
		timesDone += 1;
		
		if(plane[x][y] == 0) {
			plot(x, y, 1, plane);
			direction -= 1;
			if(direction <= -1) direction = 3;
		} else {
			plot(x, y, 0, plane);
			direction += 1;
			if(direction >= 4) direction = 0;
		}
		
		if(direction == 0 && x == plane.length - 1) {
			canMove = false;
		}
		
		if(direction == 2 && x == 0) {
			canMove = false;
		}
		
		if(direction == 1 && y == 0) {
			canMove = false;
		}
		
		if(direction == 3 && y == plane[1].length - 1) {
			canMove = false;
		}
		
		if(canMove && timesDone <= LIMIT) {
			if(direction == 0) {
				antIt(x+1, y, direction, plane);
			}
			if(direction == 1) {
				antIt(x, y-1, direction, plane);
			}
			if(direction == 2) {
				antIt(x-1, y, direction, plane);
			}
			if(direction == 3) {
				antIt(x, y+1, direction, plane);
			}
		}
		
		System.out.println("X: " + x + " - Y: " + y + " - DIR: " + direction + " - CAN MOVE: " + canMove);
	}
	
	public void floodFill(int x, int y, int toReplaceWith, int targetTile, int[][] plane){
		plane[x][y] = toReplaceWith;
		
		if(y != 0){
			if(plane[x][y-1] == targetTile){
				floodFill(x, y-1, toReplaceWith, targetTile, plane);
			}
		}
		if(y != plane[0].length-1){
			if(plane[x][y+1] == targetTile){
				floodFill(x, y+1, toReplaceWith, targetTile, plane);
			}
		}
		if(x != 0){
			if(plane[x-1][y] == targetTile){
				floodFill(x-1, y, toReplaceWith, targetTile, plane);
			}
		}
		if(x != plane.length-1){
			if(plane[x+1][y] == targetTile){
				floodFill(x+1, y, toReplaceWith, targetTile, plane);
			}
		}
	}
	
	public void antIt(int x, int y, int direction) {
		boolean canMove = true;
		
		timesDone += 1;
		
		if(map[x][y] == 0) {
			plot(x, y, 1);
			direction -= 1;
			if(direction <= -1) direction = 3;
		} else {
			plot(x, y, 0);
			direction += 1;
			if(direction >= 4) direction = 0;
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
		
		if(canMove && timesDone <= LIMIT) {
			if(direction == 0) {
				antIt(x+1, y, direction);
			}
			if(direction == 1) {
				antIt(x, y-1, direction);
			}
			if(direction == 2) {
				antIt(x-1, y, direction);
			}
			if(direction == 3) {
				antIt(x, y+1, direction);
			}
		}
		
		System.out.println("X: " + x + " - Y: " + y + " - DIR: " + direction + " - CAN MOVE: " + canMove);
	}
	
	public void draw(SpriteBatch batch) {
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[1].length; y++) {
				batch.draw(tile, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE*map[x][y], 0, 32, 32);
			}
		}
	}
}
