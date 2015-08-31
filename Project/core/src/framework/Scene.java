package framework;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Scene {
	
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
	
	public void update() {
		for (GameObject g : objects) g.update();
		
		for (GameObject g : toAdd) {
			objects.add(g);
			g.onAdd(this);
		}
		for (GameObject g : toRemove) {
			objects.remove(g);
			g.onRemove();
		}
		toAdd.clear();
		toRemove.clear();
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
}
