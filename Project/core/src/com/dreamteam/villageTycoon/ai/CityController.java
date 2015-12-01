package com.dreamteam.villageTycoon.ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamteam.villageTycoon.buildings.City;

public abstract class CityController {
	
	private City city;
	
	public CityController() {
		
	}
	
	
	public City getCity() {
		return city;
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	
	public abstract void update();
	public void drawUi(SpriteBatch batch) {}
}
