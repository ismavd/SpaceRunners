package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;

public class Goal extends AbstractGameObject {
	private TextureRegion regGoal;
	private boolean finish;

	public Goal() {
		init();
	}

	private void init() {
		dimension.set(3.0f, 3.0f);
		regGoal = Assets.instance.goal.goal;
		// Set bounding box for collision detection
		bounds.set(1, Float.MIN_VALUE, 10, Float.MAX_VALUE);
		origin.set(dimension.x / 2.0f, -1.0f);
		finish = false;
	}

	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		reg = regGoal;
		batch.draw(reg.getTexture(), position.x - origin.x, position.y
				- origin.y, origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (finish) {
			velocity.x = -terminalVelocity.x;
		} else {
			velocity.x = 0;
		}
	}
	
}
