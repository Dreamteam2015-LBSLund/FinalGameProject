package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.CityController;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.game.GameScene;
import com.dreamteam.villageTycoon.game.GameScene.MatchState;
import com.dreamteam.villageTycoon.map.Prop;
import com.dreamteam.villageTycoon.map.PropType;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.utils.InventoryItem;
import com.dreamteam.villageTycoon.workers.Worker;

/*
 * Keeps track of a city's resources and provides methods to find stuff
 * 
 */
public class City extends GameObject {
	// well then
	private GameScene scene;
	private ArrayList<Building> buildings;
	private ArrayList<Worker> workers;
	private ArrayList<Soldier> soldiers;

	private CityController controller;
	
	public City(Scene scene, CityController controller, Vector2 position) {
		super(position, new Animation(AssetManager.getTexture("error")));
		buildings = new ArrayList<Building>();
		this.scene = (GameScene) scene;
		controller.setCity(this);
		this.controller = controller;
		
		workers  = new ArrayList<Worker>();
		soldiers = new ArrayList<Soldier>();
		
		Debug.print(this, "city at " + getPosition() + ", controller: " + controller.getClass().getSimpleName());
	}
	
	public void init() {
		controller.init();
	}
	
	// register a building that is built and belongs to this city
	public void addBuilding(Building b, boolean addToGameObjects) {
		if (!buildings.contains(b)) {
			buildings.add(b);
		} else {
			Debug.print(this, "building already exists");
			if (addToGameObjects && !scene.getObjects().contains(b)) scene.addObject(b);
		}
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
		for (Building b : buildings) if (b.getOutputInventory().count(r) > 0) return true;
		return false;
	}
	
	//find the location of a resource, optionally prioritizing ones close to some place. Returns null if it can't be found. Looks in buildings and on props on the map
	public Object findResource(Resource r, Vector2 closeTo) {
		Debug.print(this, "finding resource " + r.getName());
		GameObject closest = null;
		for (Building b : buildings) {
			if (b.getOutputInventory().count(r) > 0){
				if (closeTo != null) { if (closest == null || closest.distanceTo(closeTo) > b.distanceTo(closeTo)) closest = b; }
				else return b;
			}
		}
		
		// check if there exist a proptype with the resource
		boolean propExists = false;
		for (String pt : PropType.getTypes().keySet()) {
			if (PropType.getType(pt).getResource() == r) {
				propExists = true;
				break;
			}
		}
		//if so, look through all props
		if (propExists) {
			Prop p = null;
			for (GameObject g : scene.getObjects()) {
				if (g instanceof Prop) {
					if (((Prop)g).getType().getResource() == r) {
						p = (Prop)g;
						if (closeTo != null) { if (closest == null || closest.distanceTo(closeTo) > p.distanceTo(closeTo)) closest = p; }
						else return p;
					}
				}
			}
		}
		
		if (r.getName().equals("water")) {
			Debug.print(this, "resource is water");
			Tile closestT = null;
			float cd = 0;
			for (Tile[] ta : ((GameScene)getScene()).getMap().getTiles()) {
				for (Tile t : ta) {
					if (t.getType().getName().equals("Water")) {
						for (Tile t2 : t.getNeighbors((GameScene)getScene())) {
							if (t2 != null && t2.isWalkable(null)) {
								Vector2 delta = closeTo.cpy().sub(t2.getPosition());
								float d = delta.len2();
								if (closestT == null || d < cd) {
									closestT = t2;
									cd = d;
								}
							}
						}
					}
				}
			}
			if (closestT != null) {
				Debug.print(this, "returning watertile");
				return closestT;
			}
		} else Debug.print(this, "not water: " + r.getName());
		//System.out.println(closest == null); // TODO: remove
		return closest;
	}
	
	public boolean hasBuildingType(BuildingType type) {
		for (Building b : buildings) {
			if (b.getType() == type) return true;
		}
		return false;
	}
	
	public Building getBuildingByType(BuildingType type) {
		return getBuildingByType(type, false);
	}
	
	public Building getBuildingByType(BuildingType type, boolean onlyFinished) {
		Building best = null;
		for (Building b : buildings) {
			if (b.getType() == type) {
				//return b;
				if (best == null || (!best.isBuilt() && b.isBuilt() && onlyFinished)) best = b;
			}
		}
		return best;
	}
	
	public void update(float dt) {
		controller.update(dt);
	}
	
	public void drawUi(SpriteBatch batch) {
		if(((GameScene)getScene()).getMatchState() == MatchState.ON_GOING) controller.drawUi(batch);
	}
	
	public void draw(SpriteBatch batch) {}
	
	public CityController getController() {
		return controller;
	}
	
	public ArrayList<Building> getBuildings() {
		return buildings;
	}

	public GameScene getScene() {
		return scene;
	}

	public ArrayList<Worker> getWorkers() {
		return workers;
	}

	public ArrayList<Soldier> getSoldiers() {
		return soldiers;
	}
	
	public void addWorker(Worker w) {
		workers.add(w);
		if (!getScene().getObjects().contains(w)) getScene().addObject(w);
	}
	
	public void removeWorker(Worker w) {
		workers.remove(w);
	}
	
	public void addSoldier(Soldier s) {
		soldiers.add(s);
		if (!getScene().getObjects().contains(s)) getScene().addObject(s);
	}
	
	public void removeSoldier(Soldier s) {
		soldiers.remove(s);
	}

	public ArrayList<Resource> getMaterials() {
		ArrayList<Resource> ret = new ArrayList<Resource>();
		for (Building b : buildings) {
			for (InventoryItem r : b.getOutputInventory().getList()) {
				ret.add((Resource)r);
			}
		}
		for (GameObject g : getScene().getObjects()) {
			if (g instanceof Prop) {
				ret.add(((Prop)g).getType().getResource());
			}
		}
		return ret;
	}
	
	public int getNoMaterials(Resource r) {
		int i = 0;
		for (Building b : buildings) {
			for (InventoryItem r2 : b.getOutputInventory().getList()) {
				if (r == r2) i++;
			}
		}
		for (GameObject g : getScene().getObjects()) {
			if (g instanceof Prop) {
				if (((Prop)g).getType().getResource() == r) i++;
			}
		}
		return i;
	}

	public boolean hasResources(ArrayList<Resource> arg) {
		ArrayList<Resource> resources = (ArrayList<Resource>) getMaterials().clone();
		for (Resource r : arg) {
			if (!resources.remove(r)) return false;
		}
		return true;
	}

	public boolean canProduceResources(Resource[] buildResources) {
		for (Resource r : buildResources) {
			boolean good = false;
			for (Building b : getBuildings()) {
				Resource[] res = b.getType().getProductionResources();
				for (Resource r2 : res) 
					if (r == r2) good = true;
			}
			if (good == false) return false;
		}
		return true;
	}

	public ArrayList<Resource> missingResources(ArrayList<Resource> resources) {
		ArrayList<Resource> have = (ArrayList<Resource>) getMaterials().clone();
		for (int i = 0; i < 50; i++) have.add(Resource.get("water"));
		for (Resource r : have) {
			resources.remove(r);
		}
		return resources;
	}
}
