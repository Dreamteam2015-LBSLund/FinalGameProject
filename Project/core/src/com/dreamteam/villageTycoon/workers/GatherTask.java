package com.dreamteam.villageTycoon.workers;

import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.map.Resource;

public class GatherTask {
	
	private enum State { Finding, Returning }
	
	private State state;
	
	private Building destination;
	private Resource resource;
	
	public GatherTask(Building building, Resource resource) {
		this.destination = building;
		this.resource = resource;
	}
	
	public Resource getResource() {
		return resource;
	}
	
	public Building destination() {
		return destination;
	}
	
	// returns true if the work is done
	public boolean work(Worker w) {
		if (state == State.Finding) {
			if (w.findResource(resource)) state = State.Returning;
		} else {
			w.putResource(destination, resource);
		}
		
		return false;
	}
}
