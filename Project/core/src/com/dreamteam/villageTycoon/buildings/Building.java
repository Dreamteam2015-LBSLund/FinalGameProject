package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.characters.Inventory;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.Prop;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.projectiles.Projectile;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.workers.GatherTask;
import com.dreamteam.villageTycoon.workers.GetPropTask;
import com.dreamteam.villageTycoon.workers.Worker;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.characters.Character;

public class Building extends GameObject {

	private enum BuildState { Clearing, InProgress, Done };
    private BuildingType type;
    private BuildState buildState;
    private Inventory<Resource> inputInventory, outputInventory;
    private ArrayList<Worker> workers;
    private City city;
    private boolean selected;
    private Animation selectedSign;
    private ArrayList<Resource> toGather;
    private ArrayList<Prop> toClear;
    
    private int health;
    
    //  position is tile at lower left corner
    public Building(Vector2 position, BuildingType type, City owner) {
    	super(position.add(new Vector2(.5f, .5f)), new Vector2(4, 3), new Animation(type.getBuildSprite())); // add .5 to position to align properly with tiles
    	owner.addBuilding(this);
    	this.city = owner;
    	inputInventory = new Inventory<Resource>();
    	outputInventory = new Inventory<Resource>();
		this.type = type;
		buildState = BuildState.Clearing;
		setDepth(1);
		workers = new ArrayList<Worker>();
		
		health = 10;
		
		toGather = getConstructionResources();
		//setTiles();
		selectedSign = new Animation(AssetManager.getTexture("test"), new Vector2(0.3f, 0.3f), new Color(0, 0, 1, 0.5f));
		selectedSign.setSize(this.getSize().x, this.getSize().y);
		
		toClear = new ArrayList<Prop>();
    }
    
    public void onAdd(Scene scene) {
    	super.onAdd(scene);
    	setTiles(); // this cant run in the constructor.
    }
    
    private void setTiles() {
    	Tile[][] tiles = ((TestScene)getScene()).getMap().getTiles();
    	
    	ArrayList<Prop> props = new ArrayList<Prop>();
    	for (GameObject g : getScene().getObjects()) {
			if (g instanceof Prop) {
				props.add((Prop)g);
			}
		}    	
    	
    	for (int x = 0; x < (int)(getSize().x / Tile.WIDTH); x++) {
    		for (int y = 0; y < (int)(getSize().y / Tile.HEIGHT); y++) {
    			Tile t = tiles[x + (int)(getPosition().x / Tile.WIDTH)][y + (int)(getPosition().y / Tile.HEIGHT)];
    			t.build(this);

				for (Prop p : props) if (p.getTile() == t) {
					if (!toClear.contains(p)) toClear.add(p);
					Debug.print(this, "prop to clear added at tile " + p.getTile().getPosition());
				}
    		}
    	}
    }
    
    public boolean isBuilt() {
    	return buildState == BuildState.Done;
    }
    
    public void update(float deltaTime) {
    	// TODO: instant build on Y press. remove before realease :^)
    	
    	selectedSign.setPosition(this.getPosition().x-0.5f, this.getPosition().y-0.5f);
    	
    	if(health <= 0) getScene().removeObject(this);
    	
    	for(GameObject g : getScene().getObjects()) {
			if(g instanceof Projectile) {
				if(this.city != ((Projectile)g).getOwner().getCity()) {
					if(g.getHitbox().collision(getHitbox())) {
						onHit(((Projectile)g));
						((Projectile)g).onHit();
					}
				}
			}
    	}
    	
    	if (Gdx.input.isKeyJustPressed(Keys.Y)) {
    		inputInventory.add(type.getBuildResourcesArray());
    	}

    	if (buildState == BuildState.Clearing) {
    		if (toClear.size() > 0) assignGetPropTask(toClear);
    		else buildState = BuildState.InProgress;
    	} else if (buildState == BuildState.InProgress) {
    		// check if inventory contains all materials. if so, building is done (plus some work?)
    		if (isBuildingDone()) {
    			buildState = BuildState.Done;
    			startProduction();
    			inputInventory.remove(type.getBuildResourcesArray());
    			setSprite(type.getSprite());
    		} else {
    			assignGatherTask(toGather);
    		}
    	} else {
    		// regular production
    		if (isProductionGatheringDone() || toGather.size() == 0) {
    			inputInventory.remove(type.getInputResourcesArray());
    			outputInventory.add(type.getOutputResourceArray());
    			startProduction();
    		} else {
    			assignGatherTask(toGather);
    		}
    	}
    }
    
    public void onHit(Projectile projectile) {
    	health -= projectile.getDamege();
    }
    
    public void cancelGather(Resource r) {
    	toGather.add(r);
    }
    
    private ArrayList<Resource> getConstructionResources() {
    	return getInputInventoryDifference(type.getBuildResources(), type.getBuildAmount());
    }
    
    private ArrayList<Resource> getProductionResources() {
    	for (int i = 0; i < type.getProductionResources().length; i++) {
    		Debug.print(this, type.getProductionResources()[i].getName() + ": " + type.getInputResourceAmount()[i]);
    	}
    	return getInputInventoryDifference(type.getProductionResources(), type.getInputResourceAmount());
    }
    
    private ArrayList<Resource> getInputInventoryDifference(Resource[] resources, int[] amounts) {
    	ArrayList<Resource> all = new ArrayList<Resource>();
    	for (int i = 0; i < resources.length; i++) {
    		for (int j = 0; j < amounts[i] - inputInventory.count(resources[i]); j++) {
    			all.add(resources[i]);
    		}
    	}
    	return all;
    }
    
    private void assignGetPropTask(ArrayList<Prop> props) {
    	for (Worker w : workers) {
			if (!w.hasTask()) {
				if (props.size() > 0) {
					w.setTask(new GetPropTask(this, props.remove(0)));
					Debug.print(this, "setting new get prop task, left = " + props.size());
				}
			}
		}
    }
    
    private void assignGatherTask(ArrayList<Resource> toGet) {
    	for (Worker w : workers) {
			if (!w.hasTask()) {
				Debug.print(this, "resources to get: ");
				for (Resource r : toGather) Debug.print(this, r.getName());
				if (toGet.size() > 0) w.setTask(new GatherTask(this, toGet.remove(0)));
			}
		}
    }
    
    
    // reset the list of things to gather for production, so workers can get new tasks
    private void startProduction() {
    	toGather = getProductionResources();
    }
    
    private boolean isProductionGatheringDone() {
    	boolean hasAllResources = true;
    	for (int i = 0; i < type.getProductionResources().length; i++) {
    		if (inputInventory.count(type.getProductionResources()[i]) < type.getInputResourceAmount()[i]) {
    			hasAllResources = false;
    			break;
    		}
    	}
    	return hasAllResources;
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
    	if (selected) {
    		inputInventory.drawList(getUiScreenCoords(), batch);
    		outputInventory.drawList(getUiScreenCoords().cpy().add(new Vector2(100, 0)), batch);
    	}
    }
    
    public void draw(SpriteBatch batch) {
    	if (selected) {
    		selectedSign.draw(batch);
    	}
    	    	
    	super.draw(batch);
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }
    
    public boolean getSelected() {
    	return this.selected;
    }
    
    public int getHealth() { 
    	return health;
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
