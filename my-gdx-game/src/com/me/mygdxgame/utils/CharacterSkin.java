package com.me.mygdxgame.utils;

import com.badlogic.gdx.graphics.Color;

public enum CharacterSkin {

	WHITE("Blanco", 1.0f, 1.0f, 1.0f), GRAY("Gris", 0.7f, 0.7f, 0.7f), BROWN(
			"Marron", 0.7f, 0.5f, 0.3f);
	private String name;
	private Color color = new Color();

	private CharacterSkin(String name, float r, float g, float b) {
		this.name = name;
		color.set(r, g, b, 1.0f);
	}

	@Override
	public String toString() {
		return name;
	}

	public Color getColor() {
		return color;
	}
}
