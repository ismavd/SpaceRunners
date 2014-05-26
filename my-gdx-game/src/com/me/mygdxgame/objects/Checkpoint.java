package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;

public class Checkpoint extends AbstractGameObject {

	private TextureRegion regCheckpoint;
	public boolean active;
	
	public Checkpoint() {
		init();
	}
	
	private void init() {
		dimension.set(1f, 1.25f);
		regCheckpoint = Assets.instance.checkpoint.checkpoint;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		active = false;
	}
	
	@Override
	public void render(SpriteBatch batch) {	
		TextureRegion reg = null;
		reg = regCheckpoint;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
		if (active)
			batch.setColor(0, 0, 0, 0);
	}

}
