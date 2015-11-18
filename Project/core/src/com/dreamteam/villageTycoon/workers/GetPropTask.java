package com.dreamteam.villageTycoon.workers;

import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.map.Prop;

public class GetPropTask implements Task {

	private Building building;
	private Prop prop;
	
	public GetPropTask(Building destination, Prop prop) {
		this.prop = prop;
		this.building = destination;
	}
	
	@Override
	public boolean work(Worker w) {
		return w.getProp(prop);
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

}
