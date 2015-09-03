package com.dreamteam.villageTycoon.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class Controller extends GameObject {
	private Rectangle selectionRectangle = new Rectangle(0, 0, 0, 0);

	private Vector2 selectionPoint;
	
	private boolean active;
	private boolean selectionReleased;

	public Controller() {
		super(new Vector2(0, 0), new Animation(new Texture("badlogic.jpg")));
		setColor(new Color(0, 1, 0, 0.3f));
		setDepth(1000);
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		setSize(new Vector2(selectionRectangle.getWidth(), selectionRectangle.getHeight()));
		if(selectionPoint != null) setPosition(selectionPoint);
		
		//selectionRectangle.printValues();
		
		if(Gdx.input.isTouched() && selectionRectangle != new Rectangle(0, 0, 0, 0)) {
			if (!active) {
				selectionPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			}
			active = true;
		} else if(!Gdx.input.isTouched()) {
			if(active) {
				for(GameObject c : getScene().getObjects()) if(c instanceof Character){
					if(((Character) c).getHitbox().collision(selectionRectangle)) {
						((Character) c).setSelected(true);
					}
				}
			}
			active = false;
			selectionReleased = false;
			selectionRectangle.set(0, 0, 0, 0);
			selectionPoint = getScene().getMouse();
		}
	}
	
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}
	
	public boolean getActive() {
		return active;
	}
	
	public Rectangle getSelectionRectangle() {
		return selectionRectangle;
	}
}
