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
	
	private boolean overlap(float p1, float p2, float s1, float s2) {
		return (p1 > p2 && p1 <= p2 + s2) || (p2 > p1 && p2 <= p1 + s1);
	}
	
	public boolean collision(Rectangle r) {
        boolean res = overlap(x, r.getX(), width, r.getWidth()) && overlap(y, r.getY(), height, r.getHeight());
        //printValues();
        //System.out.println((res ? "does" : "does not") + " intersect with ");
        //r.printValues();
        return res;
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
