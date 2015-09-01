package com.dreamteam.villageTycoon.map;

public class Map {
	private Tile[][] tiles;
	
	private TileType[] tileTypes;
	
	public Map() {
		//might as well load this from files, would be simple
		tileTypes = new TileType[] {
			new TileType("Grass", true, true)
		};
		
		//TODO: tom's map generation code goes here
	}
}
