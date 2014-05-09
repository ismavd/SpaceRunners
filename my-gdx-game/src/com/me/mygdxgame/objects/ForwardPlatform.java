package com.me.mygdxgame.objects;

public class ForwardPlatform extends Platform{
	private float maxDistance;
	private float minDistance;
	private boolean goingRight;
	
	public void initMove(float minDistance) {
		this.maxDistance = minDistance + 2;
		this.minDistance = minDistance;
		goingRight = true;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (goingRight) {
			velocity.x = terminalVelocity.x;
			if (position.x >= maxDistance)
				goingRight = false;
		} else {
			velocity.x = -terminalVelocity.x;
			if (position.x <= minDistance)
				goingRight = true;
		}
	}

}
