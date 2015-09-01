/**
 * 
 */
package framework;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

/**
 * @author Johannes
 *
 */
public abstract class GameObject {
	private Vector2 position, size;
	private Animation sprite;
	private Scene scene;
	private float depth;
	
	public void setPosition(Vector2 p) { 
		position = p;
		if (sprite != null) sprite.setPosition(p.x, p.y); 
	}
	public Vector2 getPosition() { return position; }
	
	public void setSize(Vector2 s) { 
		size = s;
		if (sprite != null) sprite.setSize(s.x, s.y); 
	}
	public Vector2 getSize() { return size; }
	
	public Animation getSprite() { return sprite; }

	public Scene getScene() { return scene; }
	
	public float getDepth() {
		return depth;
	}
	public void setDepth(float depth) {
		this.depth = depth;
		if (scene != null) scene.onDepthChange();
	}
	public GameObject(Vector2 position, Vector2 size, Animation sprite) {
		this.sprite = sprite;
		setPosition(position);
		setSize(size);
	}
	
	public GameObject(Vector2 position, Animation sprite) {
		this(position, new Vector2((sprite != null ? sprite.getWidth() : 0), (sprite != null ? sprite.getHeight() : 0)), sprite);
	}
	
	public void onAdd(Scene scene) {
		this.scene = scene;
	}
	
	public void onRemove() { };
	
	public void update(float deltaTime) { }
	
	public void draw(SpriteBatch batch) {
		if (sprite != null) sprite.draw(batch);
	}
	
	public void drawUi(SpriteBatch batch) { } 
}
