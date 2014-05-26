package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;

public class Rock extends AbstractGameObject {

	private TextureRegion regMiddle;
	private int length;

	public Rock() {
		init();
	}

	private void init() {
		dimension.set(1f, 1.5f);
		regMiddle = Assets.instance.rock.rock;
		// Longitud inicial de la roca
		setLength(1);
	}

	public void setLength(int length) {
		this.length = length;
		bounds.set(0, 0, dimension.x * length, dimension.y);
	}

	public void increaseLength(int amount) {
		setLength(length + amount);
	}

	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		float relX = 0;
		float relY = 0;
		relX = 0;
		reg = regMiddle;
		for (int i = 0; i < length; i++) {
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x, origin.y, dimension.x + 0.02f, dimension.y, scale.x,
					scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}
	}
}
