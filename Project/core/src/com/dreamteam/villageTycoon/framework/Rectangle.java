package com.dreamteam.villageTycoon.framework;

import com.badlogic.gdx.math.Vector2;

public class Rectangle {
	private float x;
	private float y; 
	private float width; 
	private float height;
	
	public Rectangle(float x, float y, float width, float height) {
		set(x, y, width, height);
	}
	
	public Rectangle(Vector2 position, Vector2 size) {
		set(position.x, position.y, size.x, size.y);
	}
	
	public boolean collision(Rectangle object) {
        if (y >= object.getY() + object.getHeight())
            return false;
        if (x >= object.getX() + object.getWidth())
            return false;
        if (y + height <= object.getY())
            return false;
        if (x + width <= object.getX())
        	return false;
        
		return true;
	}
	
	public void set(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void printValues() {
		System.out.print("x: " + x + "\ny: " + y + "\nw: " + width + "\nh: " + height +  "\n");
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}
