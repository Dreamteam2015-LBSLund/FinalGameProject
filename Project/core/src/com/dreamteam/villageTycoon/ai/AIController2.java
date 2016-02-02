package com.dreamteam.villageTycoon.ai;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.workers.Worker;

public class AIController2 extends CityController {

	private State state;
	private int i;
	
	public AIController2(City targetCity) {
		state = new AttackState(null, targetCity);
	}

	public void update(float dt) {
		//if (i++ < 60) return;
		State s = state.update();
		if (s != null) {
			state = s;
			Debug.print(this, "switching to state " + s.getClass().getSimpleName());
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.O)) {
			getCity().getScene().getCamera().position.set(new Vector3(getCity().getPosition(), 0));
		}
	}
	
	public void init() {
		Debug.print(this, getCity().getWorkers().size() + " workers");
	}
	

	abstract class State {
		
		protected State prevState;
		
		public State(State previous) {
			this.prevState = previous;
		}

		// returns next state, null if not done
		public abstract State update();
	}
	
	// se https://github.com/Dreamteam2015-LBSLund/Village-Tycoon-RTS/blob/master/Documents/aiStates.md
	
	// send soldiers to attack the player
	public class AttackState extends State {
		
		private City target;
		
		public AttackState(State previous, City target) {
			super(previous);
			this.target = target;
		}

		public State update() {
			if (getCity().getSoldiers().size() == 0) {
				return new MakeSoldierState(this);
			} else {
				for (Soldier s : getCity().getSoldiers()) {
					if (!s.hasPath()) s.setPath(target.getPosition());
					Debug.print(this, "sending soldier");
				}
				return null;
			}
		}
	}
	
	// use soldier factories to make soldiers
	public class MakeSoldierState extends State {
		
		private final int numSoldiers = 5;
		
		public MakeSoldierState(State previous) {
			super(previous);
		}

		public State update() {
			if (getCity().hasBuildingType(BuildingType.getTypes().get("armyBarack"))) {
				// make some soldiers
				if (getCity().getSoldiers().size() >= numSoldiers) {
					return prevState; // done
				} else {
					/*for (Worker w : getCity().getWorkers()) {
						w.workAt(getCity().getBuildingByType(BuildingType.getTypes().get("armyBarack")));
					}*/
					Building b = getCity().getBuildingByType(BuildingType.getTypes().get("armyBarack"));
					if (b != null && b.isBuilt()) {
						Debug.print(this, "spawning soldiers");
						b.spawn();
					} else {
						getCity().addBuilding(new Building(getNextBuildingPosition(), BuildingType.getTypes().get("armyBarack"), getCity()), true);
					}
					return null;	
				}
			}
			else return new MakeFactoryState(this, BuildingType.getTypes().get("armyBarack"));
		}
	}
	
	private Vector2 getNextBuildingPosition() {
		return 
				getCity().getBuildings().size() > 0 
		? getCity().getBuildings().get(getCity().getBuildings().size() - 1).getPosition().cpy().add(5, 0) 
		: getCity().getPosition();
	}
	
	public class MakeFactoryState extends State {
		
		private BuildingType type;
		
		public MakeFactoryState(State previous, BuildingType type) {
			super(previous);
			this.type = type;
		}

		public State update() {
			Building b = getCity().getBuildingByType(type);
			if (b != null) Debug.print(this, "building at " + b.getPosition());
			// if the building is not done
			if (b == null || !b.isBuilt()) {
				ArrayList<Resource> missing = getCity().missingResources(type.constructBuildResourceList());
				/*Debug.print(this, "resources for " + type.getName() + ":");
				for (Object r : singularize(type.constructBuildResourceList())) {
					Debug.print(this, ((Resource)r).getName());
				}
				Debug.print(this, "Missing resources for " + type.getName() + ":");
				for (Object r : singularize(missing)) {
					Debug.print(this, ((Resource)r).getName());
				}*/
				// if all resources are available
				if (missing.size() == 0) {
					// if the building is not placed, place it
					if (b == null) {
						Debug.print(this, "adding building " + type.getName());
						b = new Building(getNextBuildingPosition(), type, getCity());
						Debug.print(this, "new building at " + b.getPosition());
						getCity().addBuilding(b, true);
					}
					
					// assign workers to it
					for (Worker w : getCity().getWorkers()) {
						if (w.getWorkplace() != b) w.workAt(b);
						//Debug.print(this, "assigning worker to " + b.getType().getName());
					}
					
					return null;
				} else {
					// resources aren't available; make factories for them
					return new MakeResourceState(this, missing);
				}
			} else {
				// done
				return prevState;
			}
		}
	}
	
	public class GatherResourceState extends State {
		
		private Resource resource;
		
		public GatherResourceState(State previous, Resource r) {
			super(previous);
			this.resource = r;
		}

		public State update() {
			return null;
		}
	}
	
	public class MakeResourceState extends State {
		
		private ArrayList<Resource> resources;
		
		public MakeResourceState(State previous, ArrayList<Resource> missing) {
			super(previous);
			this.resources = singularize(missing);
			Debug.print(this, "neeed resources: ");
			for (Resource r : resources) Debug.print(this, r.getName());
		}

		public State update() {
			return null;
		}
		
	}
	
	public ArrayList singularize(ArrayList l) {
		ArrayList ret = new ArrayList();
		for (Object o : l) {
			if (!ret.contains(o)) {
				ret.add(o);
			}
		}
		return ret;
	}
}

