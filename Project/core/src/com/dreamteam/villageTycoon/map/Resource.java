package com.dreamteam.villageTycoon.map;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class Resource {
	private static ArrayList<String> resources;
	
	private String name;
	
	public Resource(String name) {
		if (!resources.contains(name)) {
			System.out.println("ERROR: Resource " + name + " not recognized"); 
			this.name = "error";
		} else {
			this.name = name;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public static ArrayList<String> getAll() {
		if (resources == null) {
			resources = new ArrayList<String>();
			for (String s : new ResourceReader(Gdx.files.internal("resources.gd")).getList("resources")) {
				resources.add(s);
			}
		}
		return resources;
	}
}
