package com.dreamteam.villageTycoon.framework;

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
}
