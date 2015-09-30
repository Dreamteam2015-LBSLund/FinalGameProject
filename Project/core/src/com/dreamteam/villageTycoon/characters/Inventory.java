package com.dreamteam.villageTycoon.characters;

import java.util.HashMap;

import com.dreamteam.villageTycoon.map.Resource;

public class Inventory <T> {
	private HashMap<T, Integer> things;
	
	public Inventory() {
		things = new HashMap<T, Integer>();
	}
	
	public void add(T thing, int amount) {
		if (things.containsKey(thing)) {
			things.put(thing, things.get(thing).intValue() + amount);
		} else {
			things.put(thing, amount);
		}
		
		if (things.get(thing).intValue() <= 0) {
			things.remove(thing);
			System.out.println("WARNING: Negative or 0 amount in inventory");
		}
	}
	
	public void remove(T thing, int amount) {
		add(thing, -amount);
	}
	
	public int count(T thing) {
		if (!things.containsKey(thing)) return 0;
		return things.get(thing).intValue();
	}
	
	public T[] getList() {
		return (T[]) things.keySet().toArray(); // i hope this works :^))
	}
}
