package characters;

import com.badlogic.gdx.math.Vector2;

import framework.Animation;
import framework.GameObject;

public abstract class Soldier extends GameObject {
	
	
	public Soldier(Vector2 position, Animation sprite) {
		super(position, sprite);
	}
}
