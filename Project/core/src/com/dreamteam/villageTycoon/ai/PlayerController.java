package com.dreamteam.villageTycoon.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamteam.villageTycoon.buildings.BuildingPlacer;

public class PlayerController extends CityController {


	private BuildingPlacer placer;
	
	public PlayerController() {
		
	}
	
	public void update() {
		if (Gdx.input.isKeyJustPressed(Keys.B)) {
			if (placer == null) placer = new BuildingPlacer(getCity().getScene());
			else placer = null;
		}
		
		if (placer != null) {
			if (placer.done) {
				placer = null;
			} else {
				placer.update(getCity());
			}
		}
	}
	
	public void drawUi(SpriteBatch batch) {
		if (placer != null) placer.draw(batch);
	}
}
