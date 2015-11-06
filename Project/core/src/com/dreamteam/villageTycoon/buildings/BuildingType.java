package com.dreamteam.villageTycoon.buildings;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.utils.ResourceReader;
import com.dreamteam.villageTycoon.map.Resource;

public class BuildingType {
	private static HashMap<String, BuildingType> types;
	
	public enum Type {
		Home, 
		Factory
	}
	
	private Resource[] buildResources;
	private int[] buildAmount;
	
	private String name;
	private TextureRegion sprite; //when the building is done
	private TextureRegion buildSprite; // when it's being built

	private Type type;
	
	private int inhabitants;
	
	private int maxWorkers;
	private Resource[] products;
	private int[] outputProductPerRun;
	private Resource[] productionResources;
	private int[] inputResourcePerRun;
	
	public TextureRegion getSprite() {
		return sprite;
	}
	
	public TextureRegion getBuildSprite() {
		return buildSprite;
	}
	
	public String getName() {
		return name;
	}
	
	public BuildingType(ResourceReader r) {
		name = r.getObjectName();
		
		buildResources = Resource.fromStringArray(r.getList("materials"));
		buildAmount = r.getIntList("material-amount");
		
		sprite      = AssetManager.getTexture(r.getString("sprite-finished"));
		buildSprite = AssetManager.getTexture(r.getString("sprite-building"));
		
		if (r.getString("type").equals("home")) {
			type = Type.Home;
			inhabitants = r.getInt("inhabitants");
		} else if (r.getString("type").equals("factory")) {
			type = Type.Factory;
			products = Resource.fromStringArray(r.getList("product"));
			outputProductPerRun = r.getIntList("product-per-run");
			productionResources = Resource.fromStringArray(r.getList("production-resources"));
			inputResourcePerRun = r.getIntList("resource-per-run");
			maxWorkers = r.getInt("max-workers");
		} else {
			System.out.println("ERROR: type of building " + r.getObjectName() + " wasn't recognized (" + r.getString("type") + ")");
		}
	}
	
	public Resource[] getBuildResources() {
		return buildResources;
	}

	public int[] getBuildAmount() {
		return buildAmount;
	}

	public Type getType() {
		return type;
	}

	public int getInhabitants() {
		return inhabitants;
	}

	public int getMaxWorkers() {
		return maxWorkers;
	}

	public Resource[] getProducts() {
		return products;
	}

	public int[] getOutputAmountPerRun() {
		return outputProductPerRun;
	}

	public Resource[] getProductionResources() {
		return productionResources;
	}

	public int[] getInputResourceAmount() {
		return inputResourcePerRun;
	}

	public static HashMap<String, BuildingType> getTypes() {
		if (types == null) {
			types = new HashMap<String, BuildingType>();
			for (ResourceReader r : ResourceReader.readObjectList(Gdx.files.internal("buildings.gd"))) {
				types.put(r.getObjectName(), new BuildingType(r));
			}
		}
		return types;
	}
}
