package com.dreamteam.villageTycoon.framework;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class Scene {
	private boolean depthChanged;
	
	private ArrayList<GameObject> objects, toAdd, toRemove;
	private OrthographicCamera camera, uiCamera;
	
	public OrthographicCamera getCamera() { return camera; }
	public OrthographicCamera getUiCamera() { return uiCamera; }

	public ArrayList<GameObject> getObjects() { return objects; }
	
	public Scene() {
		objects = new ArrayList<GameObject>();
		toAdd = new ArrayList<GameObject>();
		toRemove = new ArrayList<GameObject>();
		uiCamera = new OrthographicCamera(16, 9);
		camera = new OrthographicCamera(16, 9);
	}
	
	public void update(float deltaTime) {
		if (depthChanged) {
			//could be more effective with knowledge of which object changed and a bunch of custom sorting
			Collections.sort(objects, new Comparator<GameObject>() {
				@Override
				public int compare(GameObject o1, GameObject o2) {
					return Float.compare(o1.getDepth(), o2.getDepth());
				}
			});
			depthChanged = false;
		}
		
		for (GameObject g : objects) g.update(deltaTime);
		
		for (GameObject g : toAdd) {
			objects.add(g);
			g.onAdd(this);
			onDepthChange();
		}
		for (GameObject g : toRemove) {
			objects.remove(g);
			g.onRemove();
		}
		toAdd.clear();
		toRemove.clear();
	}
	
	public void onDepthChange() {
		depthChanged = true;
	}
	
	public void onPause() { }
	public void onResume() { }
	
	public void addObject(GameObject g) { toAdd.add(g); }
	public void removeObject(GameObject g) { toRemove.add(g); }
	
	public void drawUi(SpriteBatch batch) {
		for (GameObject g : objects) g.drawUi(batch);
	}
	
	public void draw(SpriteBatch batch) {
		for (GameObject g : objects) g.draw(batch);
	}
	

	public Vector2 getMouse() {
		Vector3 mouse = new Vector3(Gdx.input.getY(), Gdx.input.getY(), 0);
		camera.unproject(mouse);
		return new Vector2(mouse.x, mouse.y);
	}
}
