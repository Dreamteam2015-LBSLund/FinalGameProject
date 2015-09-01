package frameworkTest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import framework.Animation;
import framework.GameObject;
import framework.Scene;

public class TestObject extends GameObject {

	public TestObject() {
		super(new Vector2(0, 0), new Vector2(3, 3), new Animation(new Texture("badlogic.jpg")));
		setDepth(10);
	}
	
	public void update(float deltaTime) {
		setPosition(getPosition().add(.1f, 0));
	}
	
	public void drawUi(SpriteBatch batch) {
		batch.draw(getSprite().getTexture(), 5, 0, 1, 1);
	}
}
