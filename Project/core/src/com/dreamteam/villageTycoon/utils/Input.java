package com.dreamteam.villageTycoon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.dreamteam.villageTycoon.Game;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class Input {
	public static boolean intersectingWith(Rectangle area) {
		return area.collision(Game.getScene().getUiMouse());
	}
	
	public static boolean areaIsClicked(Rectangle area) {
		return intersectingWith(area) && Gdx.input.isButtonPressed(Buttons.LEFT);
	}
}
