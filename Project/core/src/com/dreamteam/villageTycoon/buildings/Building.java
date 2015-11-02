package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.characters.Inventory;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.workers.GatherTask;
import com.dreamteam.villageTycoon.workers.Worker;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.characters.Character;

public class Building extends GameObject {

	private enum BuildState { InProgress, Done };
    private BuildingType type;
    private BuildState buildState;
    private Inventory<Resource> inputInventory, outputInventory;
    private ArrayList<Worker> workers;
    private City city;
    private boolean selected;

    ArrayList<Resource> constructionResources;
    
    //  position is tile at lower left corner
    public Building(Vector2 position, BuildingType type, City owner) {
    	super(position.add(new Vector2(.5f, .5f)), new Vector2(4, 3), new Animation(type.getBuildSprite())); // add .5 to position to align properly with tiles
    	owner.addBuilding(this);
    	this.city = owner;
    	inputInventory = new Inventory<Resource>();
    	outputInventory = new Inventory<Resource>();
		this.type = type;
		buildState = BuildState.InProgress;
		setDepth(1);
		workers = new ArrayList<Worker>();
		//setTiles();
		
		constructionResources = new ArrayList<Resource>();
		for (int i = 0; i < type.getBuildResources().length; i++) {
			for (int j = 0; j < type.getBuildAmount()[i]; j++) {
				constructionResources.add(type.getBuildResources()[i]);
			}
		}
    }
    
    public void onAdd(Scene scene) {
    	super.onAdd(scene);
    	setTiles(); // this cant run in the constructor.
    }
    
    private void setTiles() {
    	Tile[][] tiles = ((TestScene)getScene()).getMap().getTiles();
    	for (int x = 0; x < (int)(getSize().x / Tile.WIDTH); x++) {
    		for (int y = 0; y < (int)(getSize().y / Tile.HEIGHT); y++) {
    			tiles[x + (int)(getPosition().x / Tile.WIDTH)][y + (int)(getPosition().y / Tile.HEIGHT)].build(this);
    		}
    	}
    }
    
    public boolean isBuilt() {
    	return buildState == BuildState.Done;
    }
    
    public void update(float deltaTime) {
    	// instant build on Y press. remove before realease :^)
    	if (Gdx.input.isKeyJustPressed(Keys.Y)) {
    		for (int i = 0; i < type.getBuildResources().length; i++) {
    			inputInventory.add(type.getBuildResources()[i], type.getBuildAmount()[i]);
    		}
    	}
    	
    	if (buildState == BuildState.InProgress) {
    		// check if inventory contains all materials. if so, building is done (plus some work?)
    		if (isBuildingDone()) {
    			buildState = BuildState.Done;
    			for (int i = 0; i < type.getBuildResources().length; i++) {
    				inputInventory.remove(type.getBuildResources()[i], type.getBuildAmount()[i]);
    	    	}
    			setSprite(type.getSprite());
    		} else {
    			Debug.print(this, workers.size() + " workers");
    			for (Worker w : workers) {
    				if (!w.hasTask()) {
    					if (constructionResources.size() > 0) w.setTask(new GatherTask(this, getNextConstructionResource()));
    					Debug.print(this, "giving gathertask to worker");
    				}
    			}
    		}
    	} else {
    		// regular production
    	}
    }
    
    // returns the next resource workers should get for construction
    private Resource getNextConstructionResource() {
    	if (constructionResources.size() > 0) return constructionResources.remove(0);
    	else return null;
    }
    
    public void addWorker(Worker w) {
    	if (!workers.contains(w)) workers.add(w);
    }
    
    public void removeWorker(Worker w) {
    	workers.remove(w);
    }
    
    public void destroy() {
    	city.removeBuilding(this);
    }
    
    private boolean isBuildingDone() {
    	boolean hasAllResources = true;
    	for (int i = 0; i < type.getBuildResources().length; i++) {
    		if (inputInventory.count(type.getBuildResources()[i]) < type.getBuildAmount()[i]) {
    			hasAllResources = false;
    			break;
    		}
    	}
    	return buildState == BuildState.InProgress && hasAllResources;
    }
    
    public BuildingType getType() {
    	return type;
    }
    
    public void drawUi(SpriteBatch batch) {
    	inputInventory.drawList(getPosition(), batch);
    }
    
    public void draw(SpriteBatch batch) {
    	super.draw(batch);
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }
    
    public boolean getSelected() {
    	return this.selected;
    }

	public Inventory<Resource> getOutputInventory() {
		return outputInventory;
	}
	
	public Inventory<Resource> getInputInventory() {
		return inputInventory;
	}
	
	public City getCity() {
		return city;
	}
	
	public boolean obstructs(Character character) {
		if (character != null) return character.getCity() != city;
		else return true;
	}
}
