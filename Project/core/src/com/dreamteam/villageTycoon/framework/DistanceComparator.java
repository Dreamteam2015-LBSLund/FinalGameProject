package com.dreamteam.villageTycoon.framework;

import java.util.Comparator;

public class DistanceComparator implements Comparator<GameObject> {

	@Override
	public int compare(GameObject object1, GameObject object2) {
		return object1.distanceTo(object2.getPosition()) < object2.distanceTo(object1.getPosition()) ? -1 
				: object1.distanceTo(object2.getPosition()) == object2.distanceTo(object1.getPosition()) ? 0 : 1;
	}

}
