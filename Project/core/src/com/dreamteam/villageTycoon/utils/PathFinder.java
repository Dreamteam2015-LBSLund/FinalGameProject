package com.dreamteam.villageTycoon.utils;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Point;
import com.dreamteam.villageTycoon.map.Tile;

public class PathFinder {
	
	private final static boolean PRINT = false;
	
	private Point startTile, endTile;
	private Tile[][] map;
	private Vector2[] path;
	private Node[][] nodes;
	
	public PathFinder(Vector2 start, Vector2 end, Tile[][] map) { // put the search in a new thread and add a callback for when it's done
		startTile  = new Point((int)(start.x / Tile.WIDTH), (int)(start.y / Tile.HEIGHT));
		endTile  = new Point((int)(end.x / Tile.WIDTH), (int)(end.y / Tile.HEIGHT));
		this.map = map;
		nodes = new Node[map.length][map[0].length];
	}
	
	private Node getNode(int x, int y) {
		if (x < 0 || x >= map.length || y < 0 || y > map[x].length) return null;
		if (nodes[x][y] == null) nodes[x][y] = new Node(new Point(x, y));
		return nodes[x][y];
	}
	
	private Node getNode(Point tile) {
		return getNode(tile.x, tile.y);
	}
	
	private Node getNode(Point tile, Node parent) {
		getNode(tile).parent = parent;
		return getNode(tile);
	}
	
	public ArrayList<Vector2> getPath() {
		long time = System.nanoTime();
		Node start = getNode(startTile);
		Node  end  = getNode(endTile);
		
		if (start == end || start == null || end == null) return null;
		
		if (start.getTile(map) == null || !start.getTile(map).isWalkable() || !end.getTile(map).isWalkable()) {
			print("tried to find unwalkable path");
			return null;
		}
		
		ArrayList<Node>  openList  = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();
		
		closedList.add(start);
		start.addNeighbors(openList, closedList);
		print("open list length: " + openList.size());
		
		while (!openList.contains(end)) {
			// get cheapest node from open list, add it to closed, and add its neighbors to openList
			if (openList.size() > 0) {
				print("adding neighbours from node with fcost " + openList.get(0).getF());
				openList.get(0).addNeighbors(openList, closedList);
			}
			else {
				print("couldn't find a path");
				return null; // startTile had no neighbors
			}
		}
		//System.out.println("time to find path: " + (System.nanoTime() - time) / 1E6f + " ms before reconstruction");
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
			v.add(0, new Vector2((nodes.get(i).index.x + .5f) * Tile.WIDTH, (nodes.get(i).index.y + .5f) * Tile.HEIGHT));
		}
		v.remove(start);
		return v;
	}
	
	private void print(String s) {
		if (PRINT) System.out.println(s);
	}
	
	private class Node
	{
		private float h; // distance to target
		private float g; // move cost
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
		public void addNeighbors(ArrayList<Node> addTo, ArrayList<Node> ignore) {
			addTo.remove(this);
			ignore.add(this);
			tryTile(index.x - 1, index.y - 1, addTo, ignore, this, new Node[] { getNode(index.x, index.y - 1), getNode(index.x - 1, index.y) });
			tryTile(index.x,     index.y - 1, addTo, ignore, this);
			tryTile(index.x + 1, index.y - 1, addTo, ignore, this, new Node[] { getNode(index.x, index.y - 1), getNode(index.x + 1, index.y) });
			
			tryTile(index.x - 1, index.y,     addTo, ignore, this);
			tryTile(index.x + 1, index.y,     addTo, ignore, this);
			
			tryTile(index.x - 1, index.y + 1, addTo, ignore, this, new Node[] { getNode(index.x - 1, index.y), getNode(index.x, index.y + 1) });
			tryTile(index.x,     index.y + 1, addTo, ignore, this);
			tryTile(index.x + 1, index.y + 1, addTo, ignore, this, new Node[] { getNode(index.x + 1, index.y), getNode(index.x, index.y + 1) });
		}
		
		private void tryTile(int x, int y, ArrayList<Node> openList, ArrayList<Node> closedList, Node parent) {
			tryTile(x, y, openList, closedList, parent, new Node[0]);
		}
		
		private void tryTile(int x, int y, ArrayList<Node> openList, ArrayList<Node> closedList, Node parent, Node[] mustBeEmpty) {
			for (Node n : mustBeEmpty) {
				if (n == null || n.getTile(map) == null 
						|| !n.getTile(map).isWalkable()) return;
			}
			if (x >= 0 && x < map.length && y >= 0 && y < map[x].length) {
				if (map[x][y].isWalkable() && !openList.contains(getNode(x, y)) && !closedList.contains(getNode(x, y))) {
					addSorted(getNode(x, y), openList);
					getNode(x, y).parent = parent;
				}
			}
		}
		
		private void addSorted(Node n, ArrayList<Node> list) {
			if (list.size() == 0) {
				list.add(n);
				return;
			} else { 
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getF() >= n.getF()) {
						list.add(i, n);
						return;
					}
				}
			}
			list.add(n);
		}
	}
}

