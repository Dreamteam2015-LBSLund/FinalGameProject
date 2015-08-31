/**
 * 
 */
package framework;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Johannes
 *
 */
public abstract class GameObject {
	private Vector2 position, size;
	private Sprite sprite;
	private Scene scene;
	
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
	
	public Sprite getSprite() { return sprite; }

	public Scene getScene() { return scene; }
	
	public GameObject(Vector2 position, Vector2 size, Sprite sprite) {
		this.sprite = sprite;
		setPosition(position);
		setSize(size);
	}
	
	public GameObject(Vector2 position, Sprite sprite) {
		this(position, new Vector2((sprite != null ? sprite.getWidth() : 0), (sprite != null ? sprite.getHeight() : 0)), sprite);
	}
	
	public void onAdd(Scene scene) {
		this.scene = scene;
	}
	
	public void onRemove() { };
	
	public void update() { }
	
	public void draw(SpriteBatch batch) {
		if (sprite != null) sprite.draw(batch);
	}
	
	public void drawUi(SpriteBatch batch) { } 
}
