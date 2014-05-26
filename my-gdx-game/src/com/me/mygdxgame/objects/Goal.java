package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;

public class Goal extends AbstractGameObject {
	private TextureRegion regGoal;
	private int level;
	private boolean finish;

	public Goal(int level) {
		this.level = level;
		init();
	}

	private void init() {
		switch (level) { // Asignamos un diseño de meta distinto según el nivel
		case 1:
			regGoal = Assets.instance.goal.goal1;
			break;
		case 2:
			regGoal = Assets.instance.goal.goal2;
			break;
		case 3:
			regGoal = Assets.instance.goal.goal3;
			break;
		case 4:
			regGoal = Assets.instance.goal.goal4;
			break;
		case 5:
			regGoal = Assets.instance.goal.goal5;
			break;
		default:
			dimension.set(3.0f, 3.0f);
			regGoal = Assets.instance.goal.goal6;
		}
		bounds.set(1, Float.MIN_VALUE, 10, Float.MAX_VALUE);
		origin.set(dimension.x / 2.0f, -1.0f);
		finish = false;
	}
	
	public void setPosition(float xBase, float yBase) {
		switch (level) { // Asignamos un diseño de meta distinto según el nivel
		case 1:
			dimension.set(5.0f, 1.5f);
			position.set(xBase, yBase - 0.15f);
			break;
		case 2:
			dimension.set(5.8f, 2.0f);
			position.set(xBase, yBase - 0.4f);
			break;
		case 3:
			dimension.set(6.0f, 4.0f);
			position.set(xBase, yBase - 0.15f);
			regGoal = Assets.instance.goal.goal3;
			break;
		case 4:
			dimension.set(6.0f, 4.0f);
			position.set(xBase, yBase + 0.05f);
			regGoal = Assets.instance.goal.goal4;
			break;
		case 5:
			dimension.set(6.0f, 4.0f);
			position.set(xBase, yBase - 0.45f);
			regGoal = Assets.instance.goal.goal5;
			break;
		default:
			dimension.set(4.0f, 4.0f);
			position.set(xBase, yBase - 0.3f);
			regGoal = Assets.instance.goal.goal6;
		}
		bounds.set(1, Float.MIN_VALUE, 10, Float.MAX_VALUE);
		origin.set(dimension.x / 2.0f, -1.0f);
		finish = false;
	}

	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		reg = regGoal;
		batch.draw(reg.getTexture(), position.x - origin.x, position.y
				- origin.y, origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (finish) {
			velocity.x = -terminalVelocity.x;
		} else {
			velocity.x = 0;
		}
	}

}
