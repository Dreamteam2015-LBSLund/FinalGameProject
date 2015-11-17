package com.dreamteam.villageTycoon.map;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;

public class Prop extends GameObject {

	private PropType type;
	
	private Tile tile;
	
	public Prop(Vector2 position, PropType type, Tile tile) {
		super(position, new Animation(type.getTexture()));
		this.type = type;
		setSize(new Vector2(type.getTexture().getRegionWidth() / 32f, type.getTexture().getRegionHeight() / 32f));
		setOriginCenter();
		setPosition(getPosition());
		this.tile = tile;
	}
	
	public Tile getTile() {
		return tile;
	}

	public PropType getType() {
		return type;
	}
}
