package com.dreamteam.villageTycoon.workers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.AIController2;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Character;
import com.dreamteam.villageTycoon.characters.Inventory;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.game.GameScene;
import com.dreamteam.villageTycoon.map.Prop;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.utils.Debug;

public class Worker extends Character {
	final boolean PRINT = false;

	private Building workplace;
	private Inventory<Resource> inventory;
	private Task task;
	
	public Worker(Vector2 position, City city) {
		super(position, new Animation(AssetManager.getTexture("worker")), new Animation(AssetManager.getTexture("worker")), city);
		inventory = new Inventory<Resource>();
		
		Debug.print(this, "worker constructed");
		
		city.addWorker(this);
		
		if(this.getCity().getController() instanceof PlayerController) {
			this.setSprite(new Animation(AssetManager.getTexture("worker")));
			this.setDeathAnimation(new Animation(AssetManager.getTexture("playerCorpse")));
		} else {
			this.setSprite(new Animation(AssetManager.getTexture("enemyWorker")));
			this.setDeathAnimation(new Animation(AssetManager.getTexture("enemyCorpse")));
		}
		
		getSprite().setAnimation(1, 4, false);
	}
	
	public void update(float deltaTime) {
		//if (getScene() == null) Debug.print(this, "SCENE IS NULL");
		//else Debug.print(this, "worker updated");
		//Debug.print(this, "tile: " + getTile().getPosition());
		if (getTile().getBuilding() != null && task == null && false) {
			workplace = getTile().getBuilding();
			onStartWork();
		} else if (task == null && false ){
			if (workplace != null) onEndWork();
			workplace = null;
		}
		
		
		//Debug.print(this, "hunger = " + getAmountFull() / getMaxFull());
		if (getAmountFull() / getMaxFull() < .3f && getCity().hasResource(Resource.get("food"))) {
			if (findResource(Resource.get("food"), inventory)) {
				inventory.remove(Resource.get("food"), 1);
				setAmountFull(getMaxFull());
				Debug.print(this, "ATE - hunger = " + getAmountFull() / getMaxFull() + "llllllllllllasdssssssssssssssssssssssssssssss");
			}
		} else if (task != null) {
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
		if (workplace != null) workplace.removeWorker(this);
		workplace = null;
		Tile t = ((GameScene)getScene()).getMap().tileAt(destination);
		if (t != null) {
			Building b = t.getBuilding();
			if (b != null) workAt(b);		
			workplace = b;
		}
	}

	
	
	Vector2 target;
	
	public void setTask(Task task) {
		Debug.print(this, "got task " + task.getClass().getSimpleName());
		if (task instanceof GatherTask) Debug.print(this, "resource: " + ((GatherTask)task).getResource().getName());
		if (task != null) task.onCancel();
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
		if (((GameScene)getScene()).getMap().tileAt(getPosition()).getBuilding() == destination) { // is at destination
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
		if (getShowInventroy() || AIController2.drawDebug) {
			inventory.drawList(getUiScreenCoords(), batch);
			if (task != null) {
				AssetManager.font.draw(batch, "Task: " + task.getString(), getUiScreenCoords().x, getUiScreenCoords().y);
			}
			if (workplace != null) {
				AssetManager.font.draw(batch, "\nWorkplace: " + workplace.getType().getName(), getUiScreenCoords().x, getUiScreenCoords().y);
			}
			AssetManager.font.draw(batch, "\n\nHunger: " + (getAmountFull() / getMaxFull()), getUiScreenCoords().x, getUiScreenCoords().y);
		}
		
		//AssetManager.font.draw(batch, getPosition() + "", getUiScreenCoords().x, getUiScreenCoords().y);
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}

	public void workAt(Building building) {
		if (building == workplace) return;
		if (building.addWorker(this)) {
			if (task != null) task.onCancel();
			task = null;
			setPath(building.getPosition(), building);
			Debug.print(this, "working at building, pos = " + building.getPosition());
			Debug.print(this, "path " + getPath());
			Debug.print(this, getPath() == null ? "path is null" : "path target: " + getPath().get(getPath().size() - 1));
			if (workplace != null) workplace.removeWorker(this);
			workplace = building;
			//onStartWork();
		}
	}
	
	public Building getWorkplace() {
		return workplace;
	}

	public Inventory<Resource> getInventory() {
		return inventory;
	}
}
