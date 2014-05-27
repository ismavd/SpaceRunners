package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;

public class Giant extends AbstractGameObject {

	private TextureRegion regGiant;
	private boolean moving;
	public int hp;
	
	public Giant() {
		init();
	}
	
	private void init() {
		dimension.set(3f,3f);
		regGiant = Assets.instance.giant.giant;
		bounds.set(0, 0, dimension.x - 1, dimension.y);
		terminalVelocity.set(2.3f, 3.0f);
		moving = true;
		hp = 30;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = regGiant;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), true, false);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (moving) {
			velocity.x = terminalVelocity.x;
		}
		else {
			velocity.x = 0;
		}
	}
	
	public void StopMoving() {
		moving = false;
	}
	
	public int getScore() {
		return 20000;
	}

}
