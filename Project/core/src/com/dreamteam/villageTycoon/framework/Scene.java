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
	public final int UI_W = 1600, UI_H = 900, G_W = 16, G_H = 9; // camera size in game and ui
	
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
		uiCamera = new OrthographicCamera(UI_W, UI_H);
		camera = new OrthographicCamera(G_W, G_H);
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
		
		camera.update();
		
		for (GameObject g : objects) g.update(deltaTime);
		
		updateObjectLists();
	}
	
	public void onDepthChange() {
		depthChanged = true;
	}
	
	public void onPause() { }
	public void onResume() { }
	
	public void addObject(GameObject g) { g.onAdd(this); toAdd.add(g); }
	public void removeObject(GameObject g) { toRemove.add(g); }
	
	public void drawUi(SpriteBatch batch) {
		for (GameObject g : objects) g.drawUi(batch);
	}
	
	public void draw(SpriteBatch batch) {
		for (GameObject g : objects) g.draw(batch);
	}
	
	public Vector2 getUiMouse() {
		Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		uiCamera.unproject(mouse);
		return new Vector2(mouse.x, mouse.y);
	}
	
	public Vector2 getScreenMouse() {
		return new Vector2(Gdx.input.getX(), Gdx.input.getY());
	}
	
	public Vector2 getWorldMouse() {
		Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mouse);
		return new Vector2(mouse.x, mouse.y);
	}
	
	public Vector2 worldToUiCoords(Vector2 position) {
		//return getScreenCoords().scl(2).sub(new Vector2(scene.UI_W / 2, scene.UI_H / 2));
		Vector3 v = getUiCamera().unproject(getCamera().project(new Vector3(position, 0)));
		v.y *= -1;
		return new Vector2(v.x, v.y);
	}
	public void updateObjectLists() {
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
}
