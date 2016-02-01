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
			map[random.nextInt(WIDTH)][random.nextInt(HEIGHT)] = 1;
		}
		
		antIt(random.nextInt(WIDTH), random.nextInt(HEIGHT), random.nextInt(4));
		//antIt(WIDTH/2, HEIGHT/2, random.nextInt(4));
	}
	
	public void plot(int x, int y, int value) {
		map[x][y] = value;
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
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				batch.draw(tile, x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE*map[x][y], 0, 32, 32);
			}
		}
	}
}
