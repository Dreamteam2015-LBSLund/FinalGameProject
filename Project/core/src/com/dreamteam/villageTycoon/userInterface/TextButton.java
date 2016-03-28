package com.dreamteam.villageTycoon.userInterface;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.framework.Rectangle;

public class TextButton extends UiElement {
	private final float PADDING = 20;
	private String text;
	private GlyphLayout g;
	private float height;
	private BitmapFont font;
	
	public TextButton(Rectangle area, String text) {
		super(area);
		this.text = text;
		font = AssetManager.font;

		fixSize();
		// TODO Auto-generated constructor stub
	}
	
	public void setFont(BitmapFont font) {
		if (font != null) this.font = font;
		fixSize();
	}
	
	private void fixSize() {
		g = new GlyphLayout(font, text);
		if (getArea().getHeight() < g.height + PADDING * 1.5f) getArea().set(getArea().getX(), getArea().getY(), getArea().getWidth(), g.height + PADDING * 1.5f);
		if (getArea().getWidth() < g.width + PADDING * 1.5f) {
			float newW = g.width + PADDING * 1.5f;
			getArea().set(getArea().getX() - (newW - getArea().getWidth()) / 2, getArea().getY(), newW, getArea().getHeight());
		}
		height = g.height + PADDING * 1.5f;
	}

	private String getTexture() {
		if (isPressed()) return "button-pressed";
		else if (isHover()) return "button-hover";
		else return "button-regular";
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(AssetManager.getTexture(this.getTexture()), getArea().getX(), getArea().getY(), getArea().getWidth(), getArea().getHeight());
		if (isPressed()) {
			font.draw(batch, text, getArea().getX() - g.width / 2 + getArea().getWidth() / 2, getArea().getY() + height / 2  - PADDING / 2 + getArea().getHeight() / 2);
		} else {
			font.draw(batch, text, getArea().getX() - g.width / 2 + getArea().getWidth() / 2, getArea().getY() + height / 2 - PADDING / 2 + getArea().getHeight() / 2);
		}
	}
}
