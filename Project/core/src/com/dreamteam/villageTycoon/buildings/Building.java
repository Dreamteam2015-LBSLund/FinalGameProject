package com.dreamteam.villageTycoon.buildings;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.characters.Inventory;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.map.Resource;

public class Building extends GameObject {

	private enum BuildState { InProgress, Done };
	
    private BuildingType type;
    private BuildState buildState;
    private Inventory<Resource> inventory;
    
    public Building(Vector2 position, BuildingType type) {
    	super(position, type.getSprite());
    	inventory = new Inventory<Resource>();
		this.type = type;
    }
    
    public void update(float deltaTime) {
    	if (buildState == BuildState.InProgress) {
    		// check if inventory contains all materials. if so, building is done (plus some work?)
    	} else {
    		// regular production
    	}
    }
    
    public BuildingType getType() {
    	return type;
    }
}
