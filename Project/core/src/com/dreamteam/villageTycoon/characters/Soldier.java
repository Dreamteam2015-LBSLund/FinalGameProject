package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public abstract class Soldier extends GameObject {
	
	
	public Soldier(Vector2 position, Animation sprite) {
		super(position, sprite);
	}
}