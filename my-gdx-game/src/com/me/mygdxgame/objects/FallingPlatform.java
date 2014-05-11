package com.me.mygdxgame.objects;

public class FallingPlatform extends Platform {
	private int time;
	public boolean active;

	public FallingPlatform() {
		super.init();
		time = 50;
		active = false;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (active) {
			if (time == 0) {
				velocity.y = -5;
			} else {
				time--;
			}
		}
	}
}
