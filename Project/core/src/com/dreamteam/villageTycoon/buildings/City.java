package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.map.Resource;

/*
 * Keeps track of a city's resources and provides methods to find stuff
 * 
 */
public class City {
	private ArrayList<Building> buildings;
	
	public City() {
		
	}
	
	// register a building that is built and belongs to this city
	public void addBuilding(Building b) {
		if (!buildings.contains(b)) buildings.add(b);
	}
	
	// unregister a building that has been destroyed
	public void removeBuilding(Building b) {
		buildings.remove(b);
	}
	
	public boolean hasResource(Resource r) {
		for (Building b : buildings) if (b.getInventory().count(r) > 0) return true;
		return false;
	}
	
	//find the location of a resource, optionally prioritizing ones close to some place. Returns null if it cant be found. Looks in buildings and on the ground
	public Vector2 findResource(Resource r, Vector2 closeTo) {
		if (closeTo != null) {
			for (Building b : buildings) {
				
			}
		}
		return null;
	}
}
