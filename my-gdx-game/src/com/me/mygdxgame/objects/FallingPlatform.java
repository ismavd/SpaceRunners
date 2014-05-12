package com.me.mygdxgame.objects;

public class FallingPlatform extends Platform {
	private int time;
	public boolean active;

	public FallingPlatform() {
		super.init();
		time = 50;
		active = false;
		setLength(1);
	}

	@Override
	public void increaseLength(int amount) {}
	
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
