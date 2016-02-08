package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;
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
	
	private int health;
	
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
	
	public int getHealth() {
		return health;
	}
	
	public BuildingType(ResourceReader r) {
		name = r.getObjectName();
		
		buildResources = Resource.fromStringArray(r.getList("materials"));
		buildAmount = r.getIntList("material-amount");
		
		sprite      = AssetManager.getTexture(r.getString("sprite-finished"));
		buildSprite = AssetManager.getTexture(r.getString("sprite-building"));
		
		health = r.getInt("health");
		
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
	
	private Resource[] constructResourceArray(Resource[] resources, int[] amounts) {
		ArrayList<Resource> ret = new ArrayList<Resource>();
		for (int i = 0; i < resources.length; i++) {
    		for (int j = 0; j < amounts[i]; j++) ret.add(resources[i]);
    	}
		return ret.toArray(new Resource[ret.size()]);
	}
	
	// returns an array of all input resources, with one element per resource instance. 
	// (for example 3 different refs if three of something is needed)
	public Resource[] getInputResourcesArray() {
		return constructResourceArray(getProductionResources(), getInputResourceAmount());
	}
	
	public Resource[] getBuildResourcesArray() {
		return constructResourceArray(getBuildResources(), getBuildAmount());
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

	public Resource[] getOutputResourceArray() {
		return constructResourceArray(getProducts(), getOutputAmountPerRun());
	}

	public boolean isFactory() {
		return type == Type.Factory;
	}

	public ArrayList<Resource> constructBuildResourceList() {
		return Resource.constructList(getBuildResources(), getBuildAmount());
	}

	// get the first buildingType that produces the given resource
	public static BuildingType factoryProduces(Resource r) {
		for (Object k : BuildingType.getTypes().keySet().toArray()) {
			BuildingType t = BuildingType.getTypes().get(k);
			if (t.isFactory()) {
				for (Resource r2 : t.products) {
					if (r == r2) return t;					
				}
			}
		}
		return null;
	}

	public ArrayList<Resource> constructProductionResourcesList() {
		return Resource.constructList(getProductionResources(), getInputResourceAmount());
	}
}
