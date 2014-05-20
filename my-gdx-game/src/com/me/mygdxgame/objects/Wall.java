package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;

public class Wall extends AbstractGameObject {

	private TextureRegion regWall;
	private int length;

	public Wall() {
		init();
	}

	private void init() {
		dimension.set(1, 1.5f);
		regWall = Assets.instance.wall.wall;
		System.out.println(regWall);
		// Start length of this rock
		setLength(1);
	}

	public void setLength(int length) {
		this.length = length;
		// Update bounding box for collision detection
		bounds.set(0, 0, dimension.x , dimension.y * length);
	}

	public void increaseLength(int amount) {
		setLength(length + amount);
	}

	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		float relX = 0;
		float relY = 0;
		reg = regWall;
		for (int i = 0; i < length; i++) {
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x, origin.y, dimension.x, dimension.y, scale.x,
					scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relY += dimension.y;
		}
	}

}
