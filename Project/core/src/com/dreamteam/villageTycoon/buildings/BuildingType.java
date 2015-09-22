package com.dreamteam.villageTycoon.buildings;

import java.util.HashMap;

import sun.security.action.GetIntegerAction;

import com.badlogic.gdx.Gdx;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class BuildingType {
	private static HashMap<String, BuildingType> types;
	
	public enum Type {
		Home, 
		Factory
	}
	
	private String[] buildResources;
	private int[] buildAmount;
	
	private String name;
	private Animation sprite;

	private Type type;
	
	private int inhabitants;
	
	private int maxWorkers;
	private String[] products;
	private int[] productPerRun;
	private String[] productionResources;
	private int[] resourcePerRun;
	
	public Animation getSprite() {
		return sprite;
	}
	
	public String getName() {
		return name;
	}
	
	public BuildingType(ResourceReader r) {
		sprite = new Animation(AssetManager.getTexture(r.getString("sprite")));
		name = r.getObjectName();
		
		buildResources = r.getList("materials");
		buildAmount = r.getIntList("material-amount");
		
		if (r.getString("type").equals("home")) {
			type = Type.Home;
			inhabitants = r.getInt("inhabitants");
		}
		else if (r.getString("type").equals("factory")) {
			type = Type.Factory;
			products = r.getList("product");
			productPerRun = r.getIntList("product-per-run");
			productionResources = r.getList("production-resources");
			resourcePerRun = r.getIntList("resource-per-run");
			maxWorkers = r.getInt("max-workers");
		} else {
			System.out.println("ERROR: type of building " + r.getObjectName() + " wasn't recognized (" + r.getString("type") + ")");
		}
	}
	
	public String[] getBuildResources() {
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

	public String[] getProducts() {
		return products;
	}

	public int[] getProductPerRun() {
		return productPerRun;
	}

	public String[] getProductionResources() {
		return productionResources;
	}

	public int[] getResourcePerRun() {
		return resourcePerRun;
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
