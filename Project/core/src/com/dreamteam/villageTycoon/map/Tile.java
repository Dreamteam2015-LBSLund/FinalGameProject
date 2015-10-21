package com.dreamteam.villageTycoon.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.characters.Character;

public class Tile {
	public final static float WIDTH = 1, HEIGHT = 1;
	
	private TileType type;
	private Vector2 position;
	private boolean hasBuilding() { return building != null; }
	private Building building;
	
	public Tile(Vector2 position, TileType type, Scene scene) {
		this.type = type;
		this.position = position;

		String prop = type.getProps();
		if (prop != null) {
			scene.addObject(new Prop(position.cpy().add(new Vector2(WIDTH / 2, HEIGHT / 2).add(new Vector2(MathUtils.random(-.2f, .2f), MathUtils.random(-.2f, .2f)))), PropType.getType(prop)));
		}
	}
	
	public void build(Building b) {
		System.out.println("Building on " + getPosition());
		this.building = b;
	}
	
	public void removeBuilding() {
		building = null;
	}
	
	public TileType getType() {
		return type;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public boolean isWalkable(Character c) {
		return type.isWalkable() && (!hasBuilding() || !building.obstructs(c));
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(type.getSprite(), position.x, position.y, WIDTH, HEIGHT);
		if (!isWalkable(null)) batch.draw(AssetManager.getTexture("error"), position.x, position.y, 1, 1);
	}

	public Building getBuilding() {
		return building;
	}
}
