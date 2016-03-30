package com.dreamteam.villageTycoon.userInterface;

import com.dreamteam.villageTycoon.ai.CityController;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.characters.Controller;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.workers.Worker;

public class SelectWorkersButton extends TextButton {

	public SelectWorkersButton(Rectangle area, String text) {
		super(area, text);
		// TODO Auto-generated constructor stub
	}

	/*private Building building;
	
	public SelectWorkersButton(Rectangle area, Building building) {
		super(area, "Select workers");
		this.building = building;
	}

	public void onClick() {
		CityController cc = building.getCity().getController();
		if (cc instanceof PlayerController) {
			Controller c = ((PlayerController)cc).getController();
			c.deselectAll();
			for (Worker w : building.getWorkers()) {
				c.select(w);
			}
			c.ignoreSelect();
		}
		super.onClick();
	}
	
	public void update() {
		super.update();
		if (isHover()) {
			CityController cc = building.getCity().getController();
			if (cc instanceof PlayerController) {
				Controller c = ((PlayerController)cc).getController();
				c.ignoreSelect();
			}
		}
	}*/
}
