package com.me.mygdxgame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.me.mygdxgame.game.Assets;
import com.me.mygdxgame.objects.Astronaut.JUMP_STATE;
import com.me.mygdxgame.utils.AudioManager;

public class Box extends AbstractGameObject {

	private TextureRegion regBox;
	
	public boolean falling;

	public Box() {
		init();
	}

	private void init() {
		dimension.set(1, 1);
		regBox = Assets.instance.box.box;
		// Update bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		falling = false;
	}

	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = regBox;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x + dimension.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
				false, false);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (falling) {
			position.y = position.y - 0.05f;
		} else {
			position.y = position.y;
		}
	}
}
