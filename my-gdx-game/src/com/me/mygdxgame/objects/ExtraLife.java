package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;

public class ExtraLife extends AbstractGameObject {
	private TextureRegion regCarrot;
	public boolean collected;
	
	public ExtraLife() {
		init();
	}
	
	private void init() {
		dimension.set(0.5f, 0.5f);
		regCarrot = Assets.instance.ExtraLife.life;
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}

	@Override
	public void render(SpriteBatch batch) {
		if (collected)
			return;
		TextureRegion reg = null;
		reg = regCarrot;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
	}
	
	public int getScore() {
		return 20;
	}

}
