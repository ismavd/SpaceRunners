package com.me.mygdxgame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;
import com.me.mygdxgame.game.WorldController;

public class EnemyForward extends AbstractGameObject {
	private TextureRegion regEnemy;
	public boolean moving;
	public boolean alive;
	public boolean dying;
	
	public EnemyForward() {
		init();
	}
	
	private void init() {
		dimension.set(1f,1f);
		regEnemy = Assets.instance.enemyFwd.enemy;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		terminalVelocity.set(2.0f, 3.0f);
		alive = true;
		moving = false;
		dying = true;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = regEnemy;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (moving)
			velocity.x = -terminalVelocity.x;
		else
			velocity.x = 0;
	}
	
	public void StopMoving() {
		moving = false;
	}
	
	public int getScore() {
		return 200;
	}
}
