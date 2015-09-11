package com.dreamteam.villageTycoon.utils;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Point;
import com.dreamteam.villageTycoon.map.Tile;

public class PathFinder {
	
	private Point startTile, endTile;
	private Tile[][] map;
	private Vector2[] path;
	
	public PathFinder(Vector2 start, Vector2 end, Tile[][] map) { // put the search in a new thread and add a callback for when it's done
		startTile  = new Point((int)(start.x / Tile.WIDTH), (int)(start.y / Tile.HEIGHT));
		endTile  = new Point((int)(end.x / Tile.WIDTH), (int)(end.y / Tile.HEIGHT));
		this.map = map;
		nodes = new Node[map.length][map[0].length];
	}
	
	// keeps ONE set of nodes, since it's all references.
	Node[][] nodes;
	
	private Node getNode(Point tile) {
		if (nodes[tile.x][tile.y] == null) nodes[tile.x][tile.y] = new Node(tile);
		return nodes[tile.x][tile.y];
	}
	
	private Node getNode(Point tile, Node parent) {
		getNode(tile).parent = parent;
		return getNode(tile);
	}
	
	public ArrayList<Vector2> getPath() {
		Node start = getNode(startTile);
		Node  end  = getNode(endTile);
		ArrayList<Node>  openList  = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();
		
		closedList.add(start);
		start.addNeighbors(openList, closedList);
		
		while (!openList.contains(end)) {
			// get cheapest node from open list, add it to closed, and add its neighbors to openList
			openList.get(0).addNeighbors(openList, closedList);
		}
		return reconstruct(start, end);
	}
	
	private ArrayList<Vector2> reconstruct(Node start, Node end) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(end);
		while (!nodes.contains(start)) {
			nodes.add(nodes.get(nodes.size() - 1).parent);
		}
		ArrayList<Vector2> v = new ArrayList<Vector2>();
		for (int i = 0; i < nodes.size(); i++) {
			v.add(new Vector2(nodes.get(i).index.x * Tile.WIDTH, nodes.get(i).index.y * Tile.HEIGHT));
		}
		return v;
	}
	
	private class Node
	{
		private float h; // distance to target
		private float g; // move cost
		private float f; // = g + h
		private Point index;
		
		public Node parent;
		
		public Node(Point tile) {
			this(tile, null);
		}
		
		public Node(Point tile, Node parent) {
			this.parent = parent;
			index = tile;
			h = Math.abs(endTile.x - index.x) + Math.abs(endTile.y - index.y);
			g = getTile(map).getType().getG();
		}
		
		public float getF() {
			return g + h;
		}
		
		public Tile getTile(Tile[][] map) {
			if (index.isOnArray(map)) return map[index.x][index.y];
			else return null;
		}
		
		// adds all walkable neighbors of the tile to the first list, if it's not already in there or in the second list, in order.
		// returns true if nothing was added
		public boolean addNeighbors(ArrayList<Node> addTo, ArrayList<Node> ignore) {
			Point p = new Point(0, 0);
			int added = 0;
			// step through all neighbors
			for (p.x = index.x - 1; p.x < index.x + 1; p.x++) {
				for (p.y = index.y - 1; p.y < index.y + 1; p.y++) {
					//check its not outside map or the center
					if ((p.x == index.x && p.y == index.y) || !p.isOnArray(map)) continue;
					//check that it's walkable and not in a list already
					if (getNode(p).getTile(map).isWalkable() && !addTo.contains(getNode(p)) && !ignore.contains(getNode(p))) {
						added++;
						// find its position in the list and add it there
						for (int i = 0; i < addTo.size(); i++) {
							if (addTo.get(i).getF() >= getNode(p).getF()) {
								addTo.add(i, getNode(p));
								break;
							} else if (i == addTo.size() -1) addTo.add(getNode(p));
						}
					}
				}
			}
			return added == 0;
		}
	}
}

