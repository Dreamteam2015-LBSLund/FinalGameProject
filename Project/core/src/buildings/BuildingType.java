package buildings;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.utils.ResourceReader;

public class BuildingType {
	private static HashMap<String, BuildingType> types;
	
	private String name;
	private Animation sprite;
	
	public Animation getSprite() {
		return sprite;
	}
	
	public String getName() {
		return name;
	}
	
	public BuildingType(ResourceReader r) {
		sprite = new Animation(AssetManager.getTexture(r.getString("sprite")));
		name = r.getObjectName();
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
