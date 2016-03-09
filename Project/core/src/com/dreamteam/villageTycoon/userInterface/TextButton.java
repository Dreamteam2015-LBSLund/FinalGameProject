package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class TextButton extends UiElement {
	private final float PADDING = 20;
	private String text;
	private GlyphLayout g;
	private float height;
	
	public TextButton(Rectangle area, String text) {
		super(area);
		this.text = text;

		g = new GlyphLayout(AssetManager.font, text);
		if (getArea().getHeight() < g.height + PADDING * 1.5f) getArea().set(getArea().getX(), getArea().getY(), getArea().getWidth(), g.height + PADDING * 1.5f);
		height = g.height + PADDING * 1.5f;
		// TODO Auto-generated constructor stub
	}

	private String getTexture() {
		if (isPressed()) return "button-pressed";
		else if (isHover()) return "button-hover";
		else return "button-regular";
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(AssetManager.getTexture(this.getTexture()), getArea().getX(), getArea().getY(), getArea().getWidth(), getArea().getHeight());
		if (isPressed()) {
			AssetManager.font.draw(batch, text, getArea().getX() - g.width / 2 + getArea().getWidth() / 2, getArea().getY() + height / 2  - PADDING / 2 + getArea().getHeight() / 2);
		} else {
			AssetManager.font.draw(batch, text, getArea().getX() - g.width / 2 + getArea().getWidth() / 2, getArea().getY() + height / 2 - PADDING / 2 + getArea().getHeight() / 2);
		}
	}
}
