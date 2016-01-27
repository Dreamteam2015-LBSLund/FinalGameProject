package com.dreamteam.villageTycoon.ai;

import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.workers.Worker;

public class AIController2 extends CityController {

	State state;
	
	public AIController2() {
		state = new AttackState(null);
		//Debug.print(this, "ai controller, city at " + getCity().getPosition());
	}

	public void update(float dt) {
		State s = state.update();
		if (s != null) {
			state = s;
			Debug.print(this, "switching to state " + s);
		}
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
		
		public AttackState(State previous) {
			super(previous);
		}

		public State update() {
			if (getCity().getSoldiers().size() == 0) {
				return new MakeSoldierState(this);
			} else {
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
					b.spawn();
					return null;	
				}
			}
			else return new MakeFactoryState(this, BuildingType.getTypes().get("armyBarack"));
		}
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

