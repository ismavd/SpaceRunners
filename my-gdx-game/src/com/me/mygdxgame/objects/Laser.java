package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;

public class Laser extends AbstractGameObject {
	private TextureRegion regLaser;
	public int duracion;
	public int maxDuracion = 25;
	public BunnyHead.VIEW_DIRECTION direction;

	public Laser(BunnyHead.VIEW_DIRECTION direction) {
		init(direction);
	}

	private void init(BunnyHead.VIEW_DIRECTION direction) {
		dimension.set(1f, 0.125f);
		regLaser = Assets.instance.laser.laser;
		terminalVelocity.set(5.0f, 3.0f);
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		duracion = 0;
		this.direction = direction;
	}

	@Override
	public void render(SpriteBatch batch) {
		/*
		 * if (collected) return;
		 */
		TextureRegion reg = null;
		reg = regLaser;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (direction == BunnyHead.VIEW_DIRECTION.RIGHT)
			velocity.x = terminalVelocity.x;
		else
			velocity.x = -terminalVelocity.x;
		duracion++;
	}

}
