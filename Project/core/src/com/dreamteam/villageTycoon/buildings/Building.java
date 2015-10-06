package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dreamteam.villageTycoon.characters.Inventory;
import com.dreamteam.villageTycoon.characters.Worker;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.map.Resource;

public class Building extends GameObject {

	private enum BuildState { InProgress, Done };
	private Texture inProgressTexture, finishedTexture;
    private BuildingType type;
    private BuildState buildState;
    private Inventory<Resource> inventory;
    private ArrayList<Worker> workers;
    
    public Building(Vector2 position, BuildingType type) {
    	super(position, new Vector2(4, 3), type.getBuildSprite());
    	inventory = new Inventory<Resource>();
		this.type = type;
		buildState = BuildState.InProgress;
		setDepth(1);
    }
    
    public void update(float deltaTime) {
    	if (Gdx.input.isKeyJustPressed(Keys.Y)) {
    		for (int i = 0; i < type.getBuildResources().length; i++) {
    			inventory.add(type.getBuildResources()[i], type.getBuildAmount()[i]);
    		}
    	}
    	if (buildState == BuildState.InProgress) {
    		// check if inventory contains all materials. if so, building is done (plus some work?)
    		if (isBuildingDone()) {
    			buildState = BuildState.Done;
    			for (int i = 0; i < type.getBuildResources().length; i++) {
    	    		inventory.remove(type.getBuildResources()[i], type.getBuildAmount()[i]);
    	    	}
    			setSprite(type.getSprite());
    		}
    	} else {
    		// regular production
    	}
    }
    
    public void addWorker(Worker w) {
    	if (!workers.contains(w)) workers.add(w);
    }
    
    public void removeWorker(Worker w) {
    	workers.remove(w);
    }
    
    private boolean isBuildingDone() {
    	boolean hasAllResources = true;
    	for (int i = 0; i < type.getBuildResources().length; i++) {
    		if (inventory.count(type.getBuildResources()[i]) < type.getBuildAmount()[i]) {
    			hasAllResources = false;
    			break;
    		}
    	}
    	return buildState == BuildState.InProgress && hasAllResources;
    }
    
    public BuildingType getType() {
    	return type;
    }
    
    public void draw(SpriteBatch batch) {
    	super.draw(batch);
    }
}
