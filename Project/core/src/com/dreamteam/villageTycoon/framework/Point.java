package com.dreamteam.villageTycoon.framework;

public class Point {
	public int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isOnArray(Object[][] o) {
		return y >= 0 && y < o.length && x >= 0 && x <= o[y].length;
	}
		
	public String toString() {
		return x + ", " + y;
	}
}
