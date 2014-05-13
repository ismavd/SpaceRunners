package com.me.mygdxgame.objects;

public class Geiser extends Platform {
	public boolean active;
	public int time;
	
	public Geiser() {
		super();
		active = false;
		time = 30;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (active) {
			time--;
			if (time <= 0) {
				active = false;
				time = 30;
			}
		}	
	}
}
