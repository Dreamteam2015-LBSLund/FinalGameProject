package com.dreamteam.villageTycoon.ai;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.workers.Worker;

public class AIController3 extends CityController {

	private Command[] script = new Command[] {
		new BuildCommand(BuildingType.getTypes().get("house")),
		new MakeWorkerCommand(10),
		new BuildCommand(BuildingType.getTypes().get("farm")),
		new MakeResourceCommand(Resource.get("food"), 10, null),
		new BuildCommand(BuildingType.getTypes().get("armyBarack"))
	};

	public AIController3 (City targetCity) {
		
	}

	public void update(float dt) {
		for (int i = 0; i < script.length; i++) {
			if (script[i].isDone()) continue;
			else {
				script[i].update();
				break;
			}
		}
	
		if (Gdx.input.isKeyJustPressed(Keys.F2)) {
			getCity().getScene().getCamera().position.set(new Vector3(getCity().getPosition(), 0));
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F3)) {
			Debug.print(this, "toggling");
			AIController2.drawDebug = true;
		} else if (Gdx.input.isKeyJustPressed(Keys.F4)) {
			AIController2.drawDebug = false;
		}
	}
	
	public void drawUi(SpriteBatch batch) {
	}

	
	class BuildCommand extends Command {
		private BuildingType t;
		private Building b;
		
		public BuildCommand(BuildingType t) {
			this.t = t;
		}
		
		public void update() {
			if (b == null) {
				b = new Building(AIController2.getNextBuildingPosition(getCity()), t, getCity());
				getCity().addBuilding(b, true);
			} if (b.getWorkers().size() < getCity().getWorkers().size()) {
				for (Worker w : getCity().getWorkers()) {
					w.workAt(b);
				}
			}
		}
		
		public boolean isDone() {
			if (b == null || !b.isBuilt()) {
				Building tmp = getCity().getBuildingByType(t);
				if (tmp != null && b.isBuilt()) {
					b = tmp;
					return true;
				}
			} else {
				return true;
			}
			return false;
		}
	}
	
	public class MakeWorkerCommand extends Command {
		private int n;
		
		public MakeWorkerCommand(int n) {
			this.n = n;
		}
		
		public void update() {
			Building b = getCity().getBuildingByType(BuildingType.getTypes().get("house"));
			if (b != null && b.isBuilt()) {
				b.spawn();
			}
		}
		
		public boolean isDone() {
			Debug.print(this, getCity().getWorkers().size() + "");
			return getCity().getWorkers().size() >= n;
		}
	}
	
	public class MakeResourceCommand extends Command {
		private Resource r;
		private int n;
		private Building b;
		
		public MakeResourceCommand(Resource r, int n, Building b) {
			this.r = r;
			this.n = n;
			this.b = b;
		}
		
		public void update() {
			if (b == null) b = getCity().getBuildingByType(BuildingType.factoryProduces(r)); // TODO: null somewhere?
			if (b.getWorkers().size() < getCity().getWorkers().size()) {
				for (Worker w : getCity().getWorkers()) {
					w.workAt(b);
				}
			}
		}
		
		public boolean isDone() {
			return getCity().getNoMaterials(r) >= n;
		}
	}
	
	abstract class Command {
		// returns true if completed, otherwise false
		public abstract void update();
		public abstract boolean isDone();
	}
}

