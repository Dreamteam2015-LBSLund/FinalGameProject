package com.dreamteam.villageTycoon.utils;

import java.util.ArrayList;

import com.dreamteam.villageTycoon.ai.AIController;
import com.dreamteam.villageTycoon.ai.AIController2;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingButton;
import com.dreamteam.villageTycoon.buildings.BuildingPlacer;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.workers.GatherTask;
import com.dreamteam.villageTycoon.workers.Worker;
import com.sun.corba.se.spi.ior.MakeImmutable;

public abstract class Debug {
	static ArrayList<Class> whitelist = new ArrayList<Class>();
	static boolean isInit;
	
	public static void init() {
		//whitelist.add(Worker.class);
		//whitelist.add(Tile.class);
		//whitelist.add(GatherTask.class);
		//whitelist.add(Character.class);
		//whitelist.add(Building.class);
		//whitelist.add(PathFinder.class);		
		//whitelist.add(BuildingPlacer.class);
		//whitelist.add(BuildingButton.class);
		whitelist.add(Soldier.class);
		whitelist.add(City.class);
		whitelist.add(AIController2.class);
		whitelist.add(AIController2.MakeFactoryState.class);
		whitelist.add(AIController2.MakeResourceState.class);
		whitelist.add(AIController2.MakeSoldierState.class);
	}
	
	public static void print(Object client, String message) {
		if (!isInit) {
			init();
			isInit = true;
		}
		if (whitelist.contains(client.getClass())) {
			String cl = client.getClass().getSimpleName();
			System.out.println(cl + ": " + message);
		} else {
			//System.out.println(client.getClass().getSimpleName());
		}
	}
}
