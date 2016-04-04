package com.dreamteam.villageTycoon.buildings;

import com.badlogic.gdx.math.Vector2;

public class BuildingPlacementProvider {
	private float a;
	private Vector2 lastPos;
	public  Vector2 getNextBuildingPosition(City c) {
		if (lastPos == null) lastPos = c.getPosition();
		a = (float)(a + Math.PI / Math.pow(c.getBuildings().size(), 1/2f));
		Vector2 n = new Vector2(5, 0).setAngleRad(a).add(lastPos);
		lastPos = n;
		return n;
	}
}
