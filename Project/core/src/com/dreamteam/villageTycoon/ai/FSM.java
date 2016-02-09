package com.dreamteam.villageTycoon.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.dreamteam.villageTycoon.ai.AIController2.State;
import com.dreamteam.villageTycoon.utils.Debug;

public class FSM {
	private State state;
	
	public FSM(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}

	public void update() {
		State s = state.update();
		if (s != null) {
			state = s;
			Debug.print(this, "switching to state " + s.getClass().getSimpleName());
		}
	}
	
	public boolean isTop() {
		return state.prevState == null;
	}
	
	public boolean isDone() {
		return state instanceof AIController2.DoneState;
	}

}

