package com.me.mygdxgame.objects;

import java.util.Random;

public class MovingPlatform extends Platform {
	
	private float maxHeight;
	private float minHeight;
	private boolean goingUp;
	
	public void initMove(float minHeight) {
		this.maxHeight = minHeight + 2;
		this.minHeight = minHeight;
		goingUp = true;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (goingUp) {
			float randomY = new Random().nextFloat();
			velocity.y = randomY;
			if (position.y >= maxHeight)
				goingUp = false;
		} else {
			velocity.y = -terminalVelocity.y;
			if (position.y <= minHeight)
				goingUp = true;
		}
	}

}
