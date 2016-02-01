package com.dreamteam.villageTycoon.ai;

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

	State state;
	
	public AIController2(City targetCity) {
		state = new AttackState(null, targetCity);
	}

	public void update(float dt) {
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
	class AttackState extends State {
		
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
					s.setPath(target.getPosition());
				}
				return null;
			}
		}
	}
	
	// use soldier factories to make soldiers
	class MakeSoldierState extends State {
		
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
		? getCity().getBuildings().get(getCity().getBuildings().size()).getPosition().cpy().add(5, 0) 
		: getCity().getPosition();
	}
	
	class MakeFactoryState extends State {
		
		BuildingType type;
		
		public MakeFactoryState(State previous, BuildingType type) {
			super(previous);
			this.type = type;
		}

		public State update() {
			return null;
		}
	}
	
	class GatherResourceState extends State {
		
		private Resource resource;
		
		public GatherResourceState(State previous, Resource r) {
			super(previous);
			this.resource = r;
		}

		public State update() {
			return null;
		}
	}
	
	class MakeResourceState extends State {
		
		private Resource resource;
		
		public MakeResourceState(State previous, Resource r) {
			super(previous);
			this.resource = r;
		}

		public State update() {
			return null;
		}
	}
}

