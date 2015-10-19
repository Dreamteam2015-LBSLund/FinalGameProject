package com.dreamteam.villageTycoon.workers;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Character;
import com.dreamteam.villageTycoon.characters.Inventory;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.map.Prop;
import com.dreamteam.villageTycoon.map.Resource;

public class Worker extends Character {

	private Building workplace;
	private Inventory<Resource> inventory;
	private Task task;
	
	public Worker(Vector2 position, Animation sprite, Animation deathAnimation, City city) {
		super(position, sprite, deathAnimation, city);
		inventory = new Inventory<Resource>();
		
		System.out.println("position " + position);
	}
	
	public void update(float deltaTime) {
		System.out.println("tile: " + getTile().getPosition());
		if (getTile().getBuilding() != null) {
			workplace = getTile().getBuilding();
			onStartWork();
		} else {
			if (workplace != null) onEndWork();
			workplace = null;
		}
		
		if (task != null) {
			if (task.work(this)) task = null;
		}
		
		super.update(deltaTime);
	}

	// finds the resource and puts it in the inventory. returns true if done
	public boolean findResource(Resource r) {
		if (inventory.count(r) > 0) return true;
		else {
			GameObject t = getCity().findResource(r, getPosition());
			Vector2 target = t.getPosition();
			setPath(target);
			
			if (isAtPathEnd()) {
				if (t instanceof Building) {
					Building b = (Building)t;
					if (b.getInventory().count(r) > 0) {
						b.getInventory().remove(r, 1);
						inventory.add(r, 1);
						return true;
					}
				} else if (t instanceof Prop) {
					if (((Prop)t).getType().getResource() == r) {
						getScene().removeObject(t);
						inventory.add(r, 1);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
	
	public boolean hasTask() {
		return task != null;
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
