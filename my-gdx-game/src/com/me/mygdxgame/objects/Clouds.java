package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.mygdxgame.game.Assets;

public class Clouds extends AbstractGameObject {
	private float length;
	private Array<TextureRegion> regClouds;
	private Array<Cloud> clouds;

	private class Cloud extends AbstractGameObject {
		private TextureRegion regCloud;

		public Cloud() {
		}

		public void setRegion(TextureRegion region) {
			regCloud = region;
		}

		@Override
		public void render(SpriteBatch batch) {
			TextureRegion reg = regCloud;
			batch.draw(reg.getTexture(), position.x + origin.x, position.y
					+ origin.y, origin.x, origin.y, dimension.x, dimension.y,
					scale.x, scale.y, rotation, reg.getRegionX(),
					reg.getRegionY(), reg.getRegionWidth(),
					reg.getRegionHeight(), false, false);
		}
	}

	public Clouds(float length) {
		this.length = length;
		init();
	}

	private void init() {
		dimension.set(3.0f, 1.5f);
		regClouds = new Array<TextureRegion>();
		regClouds.add(Assets.instance.levelDecoration.cloud);
		int distFac = 5;
		int numClouds = (int) (length / distFac);
		clouds = new Array<Cloud>(2 * numClouds);
		for (int i = 0; i < numClouds; i++) {
			Cloud cloud = spawnCloud();
			cloud.position.x = i * distFac;
			clouds.add(cloud);
		}
	}

	private Cloud spawnCloud() {
		Cloud cloud = new Cloud();
		cloud.dimension.set(dimension);
		cloud.setRegion(regClouds.random());
		// Se establece la posici�n de la nube
		Vector2 pos = new Vector2();
		pos.x = length + 10; // Posici�n despu�s del final del nivel
		pos.y += MathUtils.random(1.75f, 30);
		// Coordenada y generada aleatoriamente
		pos.y += MathUtils.random(0.0f, 0.2f)
				* (MathUtils.randomBoolean() ? 1 : -1);
		cloud.position.set(pos);
		// Velocidad
		Vector2 speed = new Vector2();
		speed.x += 0.5f; // Velocidad base
		// Velocidad adicional aleatoria
		speed.x += MathUtils.random(0.0f, 0.75f);
		cloud.terminalVelocity.set(speed);
		speed.x *= -1; // Movimiento hacia la izquierda
		cloud.velocity.set(speed);
		return cloud;
	}

	@Override
	public void render(SpriteBatch batch) {
		for (Cloud cloud : clouds)
			cloud.render(batch);
	}

	@Override
	public void update(float deltaTime) {
		for (int i = clouds.size - 1; i >= 0; i--) {
			Cloud cloud = clouds.get(i);
			cloud.update(deltaTime);
			if (cloud.position.x < -10) {
				clouds.removeIndex(i);
				clouds.add(spawnCloud());
			}
		}
	}

}
