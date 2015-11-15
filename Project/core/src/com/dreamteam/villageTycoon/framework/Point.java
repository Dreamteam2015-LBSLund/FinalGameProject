package com.dreamteam.villageTycoon.framework;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.map.Map;
import com.dreamteam.villageTycoon.map.Tile;

public class Point {
	public int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isOnArray(Object[][] o) {
		return x >= 0 && x < o.length && y >= 0 && y < o[x].length;
	}
		
	public String toString() {
		return x + ", " + y;
	}

	public <T> T getElement(T[][] array) {
		if (isOnArray(array))  {
			return array[x][y];
		} else return null;
	}

	public Vector2 toVector(float width, float height) {
		return new Vector2(x * width, y * height);
	}
}
