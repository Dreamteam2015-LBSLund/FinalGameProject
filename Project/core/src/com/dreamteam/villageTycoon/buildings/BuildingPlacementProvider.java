package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class BuildingPlacementProvider {
	
	private City city;
	private Random r;
	private ArrayList<Vector2> positions;
	
	public BuildingPlacementProvider(City city) {
		this.city = city;
		r = new Random();
		
		positions = new ArrayList<Vector2>();
		float r = 4;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < i * Math.PI * 2 * r; j += 5 / r * i) {
				positions.add(city.getPosition().cpy().add(new Vector2((float)Math.cos(j) * r * i, (float) Math.sin(j) * r * i))); 
			}
		}
	}
	
	public  Vector2 getNextBuildingPosition() {
		for (Vector2 p : positions) {
			if (city.getScene().canBuild(p)) return p;
		}
		return city.getPosition();
	}
}
