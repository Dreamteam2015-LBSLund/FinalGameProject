package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dreamteam.villageTycoon.framework.Point;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.game.GameScene;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.utils.Debug;

public class BuildingPlacer {
	
	private static ArrayList<BuildingButton> buttons;
	
	private enum State { CHOOSING, PLACING };
	private State state;
	private GameScene scene;
	private BuildingType building;
	private boolean canPlace, clicked;
	private Point placePosition;
	private Sprite buildingSprite;
	
	public boolean done;
	
	public BuildingPlacer(Scene scene) {
		state = State.CHOOSING;
		this.scene = (GameScene)scene;
		
		if (buttons == null) {
			buttons = new ArrayList<BuildingButton>();
			int y = -400;
			for (String t : BuildingType.getTypes().keySet()) {
				buttons.add(new BuildingButton(BuildingType.getTypes().get(t), new Rectangle(0, y, 300, 50)));
				y += 50;
			}
		}
	}
	
	public void update(City owner) {
		if (state == State.CHOOSING) {
			for (BuildingButton b : buttons) {
				b.update();
				if (b.isPressed()) {
					building = b.getType();
					buildingSprite = new Sprite(new TextureRegion(building.getSprite()));
					buildingSprite.setSize(400, 300);
					state = State.PLACING;
				}
			}
		} else {
			placePosition = new Point((int)(scene.getWorldMouse().x / Tile.WIDTH), (int)(scene.getWorldMouse().y / Tile.HEIGHT));
			if (canPlace(placePosition)) {
				canPlace = true;
				buildingSprite.setColor(Color.GREEN);
				if (Gdx.input.isButtonPressed(Buttons.LEFT) && !clicked) {
					scene.addObject(new Building(placePosition.toVector(Tile.WIDTH, Tile.HEIGHT), building, owner));
					done = true;
				}
			} else {
				buildingSprite.setColor(Color.RED);
			}
			buildingSprite.setPosition(scene.getUiMouse().x, scene.getUiMouse().y);
		}
		clicked = Gdx.input.isButtonPressed(Buttons.LEFT);
	}
	
	private boolean canPlace(Point corner) {
		Point p = new Point(corner.x, corner.y);
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				p.x = corner.x + i;
				p.y = corner.y + j;
				if (!tilePlaceable(p)) return false;
			}
		}
		
		return true;
	}
	
	private boolean tilePlaceable(Point p) {
		Tile t = p.getElement(scene.getMap().getTiles());
		return (t != null && t.isBuildable());
	}
	
	public void draw(SpriteBatch uiBatch) {
		if (state == State.CHOOSING) {
			for (BuildingButton b : buttons) {
				b.draw(uiBatch);
			}
		} else {
			buildingSprite.draw(uiBatch);
		}
	}
}
