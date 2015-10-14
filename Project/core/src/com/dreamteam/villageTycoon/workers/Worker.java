package com.dreamteam.villageTycoon.workers;

import javax.crypto.spec.IvParameterSpec;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Character;
import com.dreamteam.villageTycoon.characters.Inventory;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.map.Tile;

public class Worker extends Character {

	private Building workplace;
	private Inventory<Resource> inventory;
	private Task task;
	
	public Worker(Vector2 position, Animation sprite, Animation deathAnimation, City city) {
		super(position, sprite, deathAnimation, city);
		inventory = new Inventory<Resource>();
	}
	
	public void update(float deltaTime) {
		if (getTile().getBuilding() != null) {
			workplace = getTile().getBuilding();
		} else {
			workplace = null;
		}
		
		if (task != null) {
			if (task.work(this)) task = null;
		}
	}

	// finds the resource and puts it in the inventory. returns true if done
	public boolean findResource(Resource r) {
		if (inventory.count(r) > 0) return true;
		else {
			
		}
		return false;
	}
	
	// if has resource, goes to building and puts it there
	public boolean putResource(Building destination, Resource r) {
		if (inventory.count(r) < 1) return false;
		if (((TestScene)getScene()).getMap().tileAt(getPosition()).getBuilding() == destination) { // is at destination
			destination.getInventory().add(r, 1);
			inventory.remove(r, 1);
		} else {
			setPath(destination.getPosition());
			if (isAtPathEnd()) {
				destination.getInventory().add(r, 1);
				inventory.remove(r, 1);
				return true;
			}
		}
		return false;
	}
	
	private void onStartWork() {
		workplace.addWorker(this);
	}
	
	private void onEndWork() {
		workplace.removeWorker(this);
	}
}
