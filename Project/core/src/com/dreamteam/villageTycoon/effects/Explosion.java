package com.dreamteam.villageTycoon.effects;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Explosion extends GameObject {
	public enum Type { NAPALM, EXPLOSION };
	
	private float radius;
	
	
	public Explosion(Vector2 position, Animation sprite) {
		super(position, sprite);
	}
	
}
