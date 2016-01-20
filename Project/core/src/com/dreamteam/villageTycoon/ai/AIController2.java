package com.dreamteam.villageTycoon.ai;

import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.map.Resource;

public class AIController2 extends CityController {

	State state;
	
	public AIController2() {
		state = new AttackState(null);
	}

	public void update(float dt) {
		State s = state.update();
		if (s != null) state = s;
	}
	

	abstract class State {
		
		private State prevState;
		
		public State(State previous) {
			this.prevState = previous;
		}

		// returns next state, null if not done
		public abstract State update();
	}
	
	
	
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
		
		public MakeSoldierState(State previous) {
			super(previous);
		}

		public State update() {
			if (getCity().hasBuildingType(BuildingType.getTypes().get("armyBarack"))) return null;
			else return new MakeSoldierFactoryState(this);
		}
	}
	
	// make a soldier factory
	class MakeSoldierFactoryState extends State {
		
		public MakeSoldierFactoryState(State previous) {
			super(previous);
		}

		public State update() {
			if (getCity().hasResources(BuildingType.getTypes().get("armyBarack").constructBuildResourceList())) {
				// collect the resources
				return null;
			} else if (getCity().canProduceResources(BuildingType.getTypes().get("armyBarack").getBuildResources())) {
				// produce the resources
			}
			return null;
		}
	}
	
	// use existing factories to construct resources to build another factory
	class GetFactoryResourceState extends State {
		
		public GetFactoryResourceState(State previous) {
			super(previous);
		}

		public State update() {
			return null;
		}
	}
	
	// make a factory to make resources for another factory
	class  MakeFactoryResourceFactoryState extends State {
		
		public MakeFactoryResourceFactoryState(State previous) {
			super(previous);
		}

		public State update() {
			return null;
		}
	}
}

