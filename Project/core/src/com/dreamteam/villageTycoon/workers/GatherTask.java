package com.dreamteam.villageTycoon.workers;

import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.utils.Debug;

public class GatherTask implements Task {
	
	final static boolean PRINT = true;
	
	private enum State { Finding, Returning }
	
	private State state;
	
	private Building destination;
	private Resource resource;
	
	public GatherTask(Building building, Resource resource) {
		this.destination = building;
		this.resource = resource;
		state = State.Finding;
	}
	
	public Resource getResource() {
		return resource;
	}
	
	public Building destination() {
		return destination;
	}
	
	// returns true if the work is done
	public boolean work(Worker w) {
		print("gather task working, state = " + state);
		
		if (state == State.Finding) {
			if (w.findResource(resource)) state = State.Returning;
		} else {
			if (w.putResource(destination, resource)) return true;
		}
		
		return false;
	}
	
	public void onCancel() {
		destination.cancelGather(resource);
	}
	
	private void print(String s) {
		Debug.print(this, s);
	}
}
