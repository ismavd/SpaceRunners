package com.me.mygdxgame.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.me.mygdxgame.game.Assets;

public class Mountains extends AbstractGameObject {
	private TextureRegion regMountain1;
	private TextureRegion regMountain2;
	private TextureRegion regMountain3;
	private int length;

	public Mountains(int length) {
		this.length = length;
		init();
	}

	private void init() {
		dimension.set(10, 2);
		regMountain1 = Assets.instance.levelDecoration.mountain1;
		regMountain2 = Assets.instance.levelDecoration.mountain2;
		regMountain3 = Assets.instance.levelDecoration.mountain3;
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
	}

	private void drawMountain(SpriteBatch batch, TextureRegion regMountain, float offsetX, float offsetY,
			float tintColor, float parallaxSpeedX) {
		TextureRegion reg = regMountain;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		// Monta�as generadas en la base de todo el nivel
		int mountainLength = 0;
		mountainLength += MathUtils.ceil(length / (2 * dimension.x)
				* (1 - parallaxSpeedX));
		mountainLength += MathUtils.ceil(0.5f + offsetX);
		for (int i = 0; i < mountainLength; i++) {
			batch.draw(reg.getTexture(), origin.x + xRel + position.x
					* parallaxSpeedX, origin.y + yRel + position.y, origin.x,
					origin.y, dimension.x + 0.01f, dimension.y, scale.x, scale.y,
					rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);	
			xRel += dimension.x;
		}
		batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void render(SpriteBatch batch) {
		// Monta�as a una distancia muy lejana
		drawMountain(batch, regMountain3, 0.5f, 0.5f, 0.5f, 0.8f);
		// Monta�as a una distancia lejana
		drawMountain(batch, regMountain2, 0.25f, 0.25f, 0.5f, 0.5f);
		// Monta�as a una distancia cercana
		drawMountain(batch, regMountain1, 0.0f, 0.0f, 0.5f, 0.3f);
	}

	public void updateScrollPosition(Vector2 camPosition) {
		position.set(camPosition.x, position.y);
	}

}
