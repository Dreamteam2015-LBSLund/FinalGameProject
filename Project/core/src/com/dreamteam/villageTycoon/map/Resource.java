package com.dreamteam.villageTycoon.map;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class Resource {
	private static ArrayList<String> resources;
	
	private String name;
	
	public Resource(String name) {
		name = name.toLowerCase();
		if (resources == null || !resources.contains(name)) {
			System.out.println("ERROR: Resource " + name + " not recognized (or the list is null)"); 
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
	
	public static Resource[] fromStringArray(String[] s) {
		Resource[] r = new Resource[s.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = new Resource(s[i]);
		}
		return r;
	}
}
