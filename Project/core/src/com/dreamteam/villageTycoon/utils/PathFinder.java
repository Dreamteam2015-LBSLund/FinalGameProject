package com.dreamteam.villageTycoon.utils;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Point;
import com.dreamteam.villageTycoon.map.Tile;

public class PathFinder {
	
	private Point startTile, endTile;
	
	private Vector2[] path;
	
	public PathFinder(Vector2 start, Vector2 end, Tile[][] map) { // put the search in a new thread and add a callback for when it's done
		startTile  = new Point((int)(start.x / Tile.WIDTH), (int)(start.y / Tile.HEIGHT));
		endTile  = new Point((int)(end.x / Tile.WIDTH), (int)(end.y / Tile.HEIGHT));
	}
	
	public Vector2[] getPath() {
		return null;
	}
}
