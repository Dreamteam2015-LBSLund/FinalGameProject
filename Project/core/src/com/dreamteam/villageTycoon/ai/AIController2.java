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
		
		public MakeSoldierState(State previous) {
			super(previous);
		}

		public State update() {
			if (getCity().hasBuildingType(BuildingType.getTypes().get("armyBarack"))) return null;
			else return new MakeFactoryState(this, BuildingType.getTypes().get("armyBarack"));
		}
	}
	
	class MakeFactoryState extends State {
		
		public MakeFactoryState(State previous, BuildingType type) {
			super(previous);
		}

		public State update() {
			return null;
		}
	}
	
	class GatherResourceState extends State {
		
		public GatherResourceState(State previous, Resource r) {
			super(previous);
		}

		public State update() {
			return null;
		}
	}
	
	class MakeResourceState extends State {
		
		public MakeResourceState(State previous, Resource r) {
			super(previous);
		}

		public State update() {
			return null;
		}
	}
}

