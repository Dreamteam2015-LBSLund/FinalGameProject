package com.dreamteam.villageTycoon.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class TileType {
	//resources are added dynamically. 
	//TileType, when constructed, will find all resources that exist in the ground for mining
	//also objects that spawn on top, Trees in the forest for example
	private boolean isWalkable, isBuildable;
	private String name;
	private String[] resources;
	private String[] props;
	private TextureRegion sprite; // transition tiles?
	private float gCost; // for pathfinding
	
	//ArrayList<Resource> 
	
	//should only be called on startup!
	public TileType(ResourceReader r) {
		isWalkable = r.getBool("isWalkable");
		isBuildable = r.getBool("isBuildable");
		name = r.getString("name");
		resources = r.getList("resources"); 
		props = r.getList("props");
		if (isWalkable) gCost = r.getFloat("gCost");
		sprite = AssetManager.getTexture(r.getString("sprite")); // assets maste alltsa laddas innan tiles (forst antagligen)
	}

	public static HashMap<String, TileType> loadAll() {
		HashMap<String, TileType> out = new HashMap<String, TileType>();
		for (ResourceReader r : ResourceReader.readObjectList(Gdx.files.internal("tiletypes.gd"))) {
			out.put(r.getObjectName(), new TileType(r));
			System.out.println("loaded tileType " + r.getObjectName());
		}
		return out;
	}
	
	//does the randomization and returns a set of props that should be spawned
	public String getProps() {
		float chanceSum = 0;
		for (int i = 1; i < props.length; i += 2) {
			try {
				chanceSum += Float.parseFloat(props[i]);
			} 
			catch (Exception e) {
				System.out.println("WARNING: there's an error in props for tileType " + name);
			}
		}
		if (MathUtils.randomBoolean(chanceSum)) {
			for (int t = 0; t < 10; t++) {
				for (int i = 0; i < props.length; i += 2) {
					try {
						if (MathUtils.randomBoolean(Float.parseFloat(props[i + 1]) / (chanceSum))) {
							return props[i];
						}	
					} 
					catch (Exception e) {
						System.out.println("WARNING: there's an error in props for tileType " + name);
					}
				}
			}
			System.out.println("for some reason a prop couln't be selected, tiletype: " + name);
		}
		return null;
	}
	
	public float getG() {
		return gCost;
	}

	//Tile.isWalkable should probably be used since this does not take buildings into account
	public boolean isWalkable() {
		return isWalkable;
	}

	public boolean isBuildable() {
		return isBuildable;
	}
	
	public TextureRegion getSprite() {
		return sprite;
	}
	
	public String getName() { return name; }
}