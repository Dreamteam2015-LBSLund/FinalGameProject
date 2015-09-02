package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class Controller extends GameObject {
	private Rectangle selectionRectangle = new Rectangle(0, 0, 0, 0);

	private Vector2 selectionPoint;
	
	public Controller() {
		super(new Vector2(0, 0), new Animation(new Texture("badlogic.jpg")));
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		setSize(new Vector2(selectionRectangle.getWidth(), selectionRectangle.getHeight()));
		if(selectionPoint != null) setPosition(selectionPoint);
		
		if(Gdx.input.isTouched() && selectionRectangle != new Rectangle(0, 0, 0, 0)) {
			selectionRectangle = new Rectangle(getScene().getMouse().x-getScene().getMouse().x-selectionPoint.x, getScene().getMouse().y-getScene().getMouse().y-selectionPoint.y, 
					getScene().getMouse().x-selectionPoint.x, getScene().getMouse().y-selectionPoint.y);
		}
		
		if(!Gdx.input.isTouched()) {
			selectionRectangle.set(0, 0, 0, 0);
			selectionPoint = getScene().getMouse();
		}
	}
}
