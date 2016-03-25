package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dreamteam.villageTycoon.Game;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.utils.Input;

public abstract class UiElement {
	
	// defines where it listens to mouse events
	private Rectangle area;
	
	// the scene where it resides
	private Scene scene;
	
	private Vector2 oldMouse, newMouse;

	private boolean isPressed, wasPressed, isHover, wasHover; // index of pointer, -1 if nothing
	
	protected boolean keepGrabbed;
	
	public void setPosition(Vector2 position) {
		area.set(position.x, position.y, area.getWidth(), area.getHeight());
	}
	
	protected boolean isHover() {
		return isHover;
	}

	protected boolean wasHover() {
		return wasHover;
	}

	public Rectangle getArea() {
		return area;
	}

	protected Vector2 getOldMouse() {
		return oldMouse;
	}

	protected Vector2 getNewMouse() {
		return newMouse;
	}

	public boolean isPressed() {
		return isPressed;
	}

	protected boolean wasPressed() {
		return wasPressed;
	}

	
	public UiElement(Rectangle area) {
		this.area = area;
		newMouse = new Vector2(0, 0);
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	protected Vector2 getRelMouse() {
		return getRelMouse(newMouse);
	}
	
	protected Vector2 getRelMouse(Vector2 sceneMouse) {
		return newMouse.cpy().sub(new Vector2(area.getX(), area.getY()));
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public void onMouseEnter() {
		
	}
	
	public void onMouseExit() {
		
	}
	
	public void onClick() {
		
	}
	
	public void onDrag() {
		
	}
	
	public void onRelease() {
		
	}
	
	public boolean getIsHover() {
		return isHover;
	}
	
	// called each frame
	public void update() {
		
		isPressed = Input.areaIsClicked(area);
		isHover = Input.intersectingWith(area);
		newMouse = Game.getScene().getUiMouse();
		
		if (!wasHover() && isHover()) {
			onMouseEnter();
			//System.out.println("enter");
		}
		
		if (wasHover() && !isHover()) {
			onMouseExit();
			//System.out.println("exit");
		}
		
		if (!wasPressed() && isPressed())  {
			onClick();
			//System.out.println("press");
		}
		
		if (wasPressed() && !isPressed()) {
			onRelease();
			//System.out.println("release");
		}
		
		if (isPressed() && wasPressed()) {
			onDrag();
			//System.out.println("dragged");
		}
		
		wasHover = isHover;
		wasPressed = isPressed;
		oldMouse = newMouse.cpy();
	}
	
	public abstract void draw(SpriteBatch batch);
}