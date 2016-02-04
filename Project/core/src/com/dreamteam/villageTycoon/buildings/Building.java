package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.characters.Inventory;
import com.dreamteam.villageTycoon.characters.SabotageKit;
import com.dreamteam.villageTycoon.characters.SabotageKitType;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.characters.SoldierType;
import com.dreamteam.villageTycoon.characters.WeaponType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.ActivationType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.EffectType;
import com.dreamteam.villageTycoon.characters.WeaponType.Type;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.Prop;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.projectiles.Projectile;
import com.dreamteam.villageTycoon.projectiles.ProjectileType;
import com.dreamteam.villageTycoon.userInterface.CreateCharacterButton;
import com.dreamteam.villageTycoon.userInterface.DestroyBuildingButton;
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
    
    private Character characterToSpawn;
    private CreateCharacterButton createCharacterButton; 
    
    private DestroyBuildingButton destroyBuildingButton;
    
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
		
		health = type.getHealth();
		
		toGather = getConstructionResources();
		//setTiles();
		selectedSign = new Animation(AssetManager.getTexture("test"), new Vector2(0.3f, 0.3f), new Color(0, 0, 1, 0.5f));
		selectedSign.setSize(this.getSize().x, this.getSize().y);
		
		toClear = new ArrayList<Prop>();
		
		this.createCharacterButton = new CreateCharacterButton(new Vector2(0, 0), this);
		this.destroyBuildingButton = new DestroyBuildingButton(new Vector2(300, 300));
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
    	
    	if(this.type.getType() == BuildingType.Type.Home) {
    		//if(this.selected) System.out.println(type.getName());			
			this.createCharacterButton = new CreateCharacterButton(new Vector2(0, 0), this);
		}
    	
    	selectedSign.setPosition(this.getPosition().x-0.5f, this.getPosition().y-0.5f);

    	if(health <= 0) getScene().removeObject(this);
    	
    	if(type.getType() == BuildingType.Type.Home && selected && this.isBuilt()) {
    		this.createCharacterButton.update(getScene());
    	}
    	
    	if(selected) {
    		destroyBuildingButton.update(this);
    	}

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
    			if (type.isFactory()) startProduction();
    			inputInventory.remove(type.getBuildResourcesArray());
    			setSprite(type.getSprite());
    		} else {
    			assignGatherTask(toGather);
    		}
    	} else if (type.isFactory()) {
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
    	if (!type.isFactory())  return true;
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
    
    public void spawn() {
    	getScene().addObject(getCharacterToSpawn());
    }
    
    public Character getCharacterToSpawn() {
    	if(this.type.getType() == BuildingType.Type.Home) {
			if (!type.getName().equals("house")) {
				return new Soldier(city, new Vector2(this.getPosition().x, this.getPosition().y), new WeaponType("pistol", 1, 1, 1, 1, 1, new ProjectileType(ProjectileType.Type.SHOT, 5, 5, 5, null, "projectile"), new Sprite(AssetManager.getTexture("gun")), Type.HANDGUN), 
						new SoldierType(1, 1, 1, 1, new Animation(AssetManager.getTexture("soldier"))), 
						new SabotageKit[]{ 
								new SabotageKit(new SabotageKitType("motolv coctalil", 1, 1, "firekit", ActivationType.INSTANT, EffectType.FIRE)), 
								new SabotageKit(new SabotageKitType("motolv coctalil", 1, 1, "firekit", ActivationType.INSTANT, EffectType.FIRE)) 
				});
			} else {
				return new Worker(new Vector2(this.getPosition().x, this.getPosition().y), city);
			}

		}
    	else return null;
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
    		
    		destroyBuildingButton.draw(batch);
    		
    		if(type.getType() == BuildingType.Type.Home && this.isBuilt() && selected) {
        		createCharacterButton.draw(batch);
        	}
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
    
    public CreateCharacterButton getCreateCharacterButton() {
    	return this.createCharacterButton;
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
