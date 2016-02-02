package com.dreamteam.villageTycoon.workers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Character;
import com.dreamteam.villageTycoon.characters.Inventory;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.map.Prop;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.utils.Debug;

public class Worker extends Character {
	final boolean PRINT = false;

	private Building workplace;
	private Inventory<Resource> inventory;
	private Task task;
	
	public Worker(Vector2 position, Animation sprite, Animation deathAnimation, City city) {
		super(position, sprite, deathAnimation, city);
		inventory = new Inventory<Resource>();
		
		Debug.print(this, "worker constructed");
		
		city.addWorker(this);
		
	}
	
	public void update(float deltaTime) {
		//if (getScene() == null) Debug.print(this, "SCENE IS NULL");
		//else Debug.print(this, "worker updated");
		//Debug.print(this, "tile: " + getTile().getPosition());
		if (getTile().getBuilding() != null && task == null) {
			workplace = getTile().getBuilding();
			onStartWork();
		} else if (task == null){
			if (workplace != null) onEndWork();
			workplace = null;
		}
		
		if (task != null) {
			if (task.work(this)) task = null;
		}

		super.update(deltaTime);
	}
	
	public void onPlayerInput(Vector2 destination) {
		super.onPlayerInput(destination);
		//Debug.print(this, "recieved input");
		if (task != null) {
			task.onCancel();
		}
		task = null;
	}

	// finds the resource and puts it in the inventory. returns true if done
	public boolean findResource(Resource r) {
		//print("finding resource " + r.getName());
		if (inventory.count(r) > 0) {
			//print("is already in inventory");
			return true;
		}
		else {
			GameObject t = getCity().findResource(r, getPosition()); // this doesn't need to happen every frame
			if (t != null) {
				Vector2 target = t.getPosition();
				print("found resource at " + target + ", setting path");
			
				setPath(target);
				
				if (isAtPathEnd()) {
					print("got to end of path");
					if (t instanceof Building) {
						print("thing is building, taking resource");
						Building b = (Building)t;
						if (b.getOutputInventory().count(r) > 0) {
							b.getOutputInventory().remove(r, 1);
							inventory.add(r, 1);
							//Debug.print(this, "has resource, putting");
							return true;
						}
					} else if (t instanceof Prop) {
						print("thing is prop, destroying it");
						if (((Prop)t).getType().getResource() == r) {
							getScene().removeObject(t);
							inventory.add(r, 1);
							return true;
						} else {
							print("Wrong prop :^( resource = " + ((Prop)t).getType().getResource().getName());
							return false;
						}
					}
				}
			} else {
				print("couldn't find resource");
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
	
	public boolean getProp(Prop p) {
		if (distanceTo(p.getPosition()) < .1f) {
			inventory.add(p.getType().getResource(), 1);
			getScene().removeObject(p);
			print("done with get prop");
			return true;
		} else {
			setPath(p.getPosition());
			return false;
		}
	}
	
	// if has resource, goes to building and puts it there
	public boolean putResource(Building destination, Resource r) {
		print("putting resource " + r + " in building at " + destination.getPosition());
		if (inventory.count(r) < 1) {
			print("resource not in inventory");
			return false;
		}
		if (((TestScene)getScene()).getMap().tileAt(getPosition()).getBuilding() == destination) { // is at destination
			print("have arrived, putting");
			destination.getInputInventory().add(r, 1);
			inventory.remove(r, 1);
			return true;
		} else {
			print("setting a path, target = " + destination + ", postition = " + destination.getPosition());
			setPath(destination.getPosition());
			if (isAtPathEnd()) {
				print("im there, putting");
				destination.getInputInventory().add(r, 1);
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
	
	public void setPath(Vector2 target) {
		setPath(target, workplace);
	}
	
	private void print(String s) {
		if (PRINT) Debug.print(this, s);
	}
	
	public void onAdd(Scene s) {
		//Debug.print(this, "prev scene: " + getScene());
		super.onAdd(s);
		//Debug.print(this, "added to scene");
	}
	
	public void drawUi(SpriteBatch batch) {
		if (getShowInventroy()) {
			inventory.drawList(getUiScreenCoords(), batch);
			if (task != null) {
				AssetManager.font.draw(batch, "Task: " + task.getString(), getUiScreenCoords().x, getUiScreenCoords().y);
			}
		}
		//AssetManager.font.draw(batch, getPosition() + "", getUiScreenCoords().x, getUiScreenCoords().y);
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}

	public void workAt(Building building) {
		task = null;
		setPath(building.getPosition(), building);
		Debug.print(this, "working at building, pos = " + building.getPosition());
		Debug.print(this, "path target: " + getPath().get(getPath().size() - 1));
		//workplace = building;
		//onStartWork();
	}

	

	public Building getWorkplace() {
		return workplace;
	}
}
