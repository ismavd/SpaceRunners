package com.me.mygdxgame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;
import com.me.mygdxgame.game.WorldController;

public class Enemy extends AbstractGameObject{

	private TextureRegion regEnemy;
	private float maxHeight;
	private float minHeight;
	private boolean goingUp;
	
	public boolean alive;
	
	public Enemy() {
		init();
	}

	private void init() {
		dimension.set(0.5f,0.5f);
		regEnemy = Assets.instance.enemy.enemy;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		terminalVelocity.set(2.0f, 3.0f);
		alive = true;
	}
	
	public void initMove(float minHeight) {
		this.maxHeight = minHeight + 2;
		this.minHeight = minHeight;
		goingUp = true;
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
		if (goingUp) {
			velocity.y = terminalVelocity.y;
			if (position.y >= maxHeight)
				goingUp = false;
		} else {
			velocity.y = -terminalVelocity.y;
			if (position.y <= minHeight)
				goingUp = true;
		}
	}
	
	public int getScore() {
		return 200;
	}
}
