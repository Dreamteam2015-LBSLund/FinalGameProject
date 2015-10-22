package com.dreamteam.villageTycoon.utils;

import java.util.ArrayList;

import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.workers.GatherTask;
import com.dreamteam.villageTycoon.workers.Worker;

public abstract class Debug {
	static ArrayList<Class> whitelist = new ArrayList<Class>();
	static boolean isInit;
	
	public static void init() {
		whitelist.add(Worker.class);
		//whitelist.add(GatherTask.class);
		//whitelist.add(Character.class);
		//whitelist.add(Building.class);
		whitelist.add(PathFinder.class);
	}
	
	public static void print(Object client, String message) {
		if (!isInit) {
			init();
			isInit = true;
		}
		if (whitelist.contains(client.getClass())) {
			String[] s = client.getClass().getName().split("\\.");
			String cl  = s.length > 0 ? s[s.length - 1] : ""; 
			System.out.println(cl + ": " + message);
		}
	}
}