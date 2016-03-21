package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.workers.GatherTask;

public class BuildingTaskProvider {
	private ArrayList<Resource> all, notDone, inProgress, done;
	private Building building;
	
	public BuildingTaskProvider(ArrayList<Resource> resources, Building b) {
		reset(resources);
		this.building = b;
	}
	
	public boolean onAdd(Resource resource) {
		if (inProgress.remove(resource) || notDone.remove(resource)) {
			done.add(resource);
			return true;
		} else {
			return false;
		}
	}
	
	public GatherTask nextTask() {
		if (notDone.size() > 0) {
			Resource ret = notDone.remove(0);
			inProgress.add(ret);
			System.out.println("next resource = " + ret.getName()); //TODO: remov
			return new GatherTask(building, ret);
		}
		return null;
	}
	
	public void cancelTask(GatherTask t) {
		System.out.println("task canceled"); //TODO: remove
		if (inProgress.remove(t.getResource())) notDone.add(t.getResource());
	}
	
	public void cancelTask(Resource r) {
		if (inProgress.remove(r)) notDone.add(r);
	}
	
	public boolean isDone() {
		return notDone.size() == 0 && inProgress.size() == 0;
	}
	
	public void reset(ArrayList<Resource> newRes) {
		all = newRes;
		notDone = (ArrayList<Resource>) newRes.clone();
		if (inProgress == null) inProgress = new ArrayList<Resource>();
		else inProgress.clear();
		if (done == null) done = new ArrayList<Resource>();
		else done.clear();
	}
	
	public String getProgress() {
		return all.size() + " total, " + inProgress.size() + " in progress, " + done.size() + " done";
	}

	public void draw(Vector2 pos, SpriteBatch batch) {
		int size = 50;
		ArrayList<Resource> resources = getTypes(all);
		int[] have = new int[resources.size()];
		int[] allCount = new int[resources.size()];
		for (int i = 0; i < resources.size(); i++) {
			have[i] = count(done, resources.get(i));
			allCount[i] = count(all, resources.get(i));
		}
		
		for (int i = 0; i < resources.size(); i++) {
			batch.draw(resources.get(i).getIcon(), pos.x, pos.y + i * size, size, size);
			AssetManager.font.draw(batch, resources.get(i).getName() + " " + have[i] + "/" + allCount[i], pos.x + size, pos.y + (i + 1) * size);
		}
	}
	
	private ArrayList<Resource> getTypes(ArrayList<Resource> res) {
		ArrayList<Resource> ret = new ArrayList<Resource>();
		for (Resource r : res) {
			if (!ret.contains(r)) ret.add(r);
		}
		return ret;
	}
	
	private int count(ArrayList<Resource> list, Resource type) {
		int ret = 0;
		for (Resource r : list) if (r == type) ret++;
		return ret;
	}
}
