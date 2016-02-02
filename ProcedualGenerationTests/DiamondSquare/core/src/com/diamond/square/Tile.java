package com.diamond.square;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile {
	protected int x;
	protected int y;
	
	private Color color;
	private Sprite sprite;
	
	public Tile(int x, int y, Color color) {
		sprite = new Sprite(new Texture("badlogic.jpg"));
		sprite.setPosition(x*32, y*32);
		
		this.color = color;
		sprite.setColor(color);
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
}
