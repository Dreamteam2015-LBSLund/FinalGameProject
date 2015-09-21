package com.dreamteam.villageTycoon.buildings;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Building extends GameObject {

	public Building(Vector2 position, BuildingType type) {
		super(position, type.getSprite());
	}
	
}
