package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;

public class Platform extends AbstractGameObject {

	private TextureRegion regPlatform;
	private int length;
	
	public Platform() {
		init();
	}
	
	protected void init() {
		dimension.set(1f, 0.5f);
		regPlatform = Assets.instance.platform.platform;
		setLength(1);
		terminalVelocity.set(2.0f, 3.0f);
	}
	
	public void setLength(int length) {
		this.length = length;
		// Se actualiza la caja de colisiones 
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
		reg = regPlatform;
		for (int i = 0; i < length; i++) {
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x, origin.y, dimension.x, dimension.y, scale.x,
					scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}
	}

}
