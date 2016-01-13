package com.dreamteam.villageTycoon.ai;

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
	
	class AttackState extends State {
		
		public AttackState(State previous) {
			super(previous);
		}

		public State update() {
			return null;
		}
	}
}

