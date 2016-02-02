package com.diamond.square;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Map {
	final int TILE_SIZE = 32;
	
	final int WIDTH = 640/32;
	final int HEIGHT = 480/32;
	
	private Tile tiles[][];
	
	public Map() {
		tiles = new Tile[WIDTH][HEIGHT];
		
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				tiles[x][y] = new Tile(x, y, new Color(0, 0, 0, 1));
			}
		}
	}
	
	public void Draw(SpriteBatch batch) {
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				tiles[x][y].draw(batch);
			}
		}
	}
	
	public static float smoothStep(float edge0, float edge1, float x) {
		x = (float)MathUtils.clamp((x - edge0)/(edge1 - edge0), 0.0f, 1.0f);
		
		return x*x*(3 - 2*x);
	}
}
