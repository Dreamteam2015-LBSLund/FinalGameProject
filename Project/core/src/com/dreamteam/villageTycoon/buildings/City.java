package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.CityController;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.map.Prop;
import com.dreamteam.villageTycoon.map.PropType;
import com.dreamteam.villageTycoon.map.Resource;

/*
 * Keeps track of a city's resources and provides methods to find stuff
 * 
 */
public class City extends GameObject {
	private TestScene scene;
	private ArrayList<Building> buildings;
	private CityController controller;
	
	public City(Scene scene, CityController controller, Vector2 position) {
		super(position, new Animation(AssetManager.getTexture("error")));
		buildings = new ArrayList<Building>();
		this.scene = (TestScene) scene;
		controller.setCity(this);
		this.controller = controller;
	}
	
	// register a building that is built and belongs to this city
	public void addBuilding(Building b, boolean addToGameObjects) {
		if (!buildings.contains(b)) {
			buildings.add(b);
			if (addToGameObjects) scene.addObject(b);
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
	public GameObject findResource(Resource r, Vector2 closeTo) {
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
		
		return closest;
	}
	
	public void update(float dt) {
		controller.update();
	}
	
	public void drawUi(SpriteBatch batch) {
		controller.drawUi(batch);
	}
	
	public void draw(SpriteBatch batch) {}
	
	public ArrayList<Building> getBuildings() {
		return buildings;
	}

	public TestScene getScene() {
		return scene;
	}
}
