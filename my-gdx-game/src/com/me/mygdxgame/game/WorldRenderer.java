package com.me.mygdxgame.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.me.mygdxgame.objects.AbstractGameObject;
import com.me.mygdxgame.objects.Astronaut;
import com.me.mygdxgame.objects.Enemy;
import com.me.mygdxgame.objects.Laser;
import com.me.mygdxgame.utils.Constants;
import com.me.mygdxgame.utils.GamePreferences;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class WorldRenderer implements Disposable {
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;
	
	// Constructor
	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}

	private void init() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
				Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true); // Invierte el eje y
		cameraGUI.update();
	}

	public void render() {
		renderWorld(batch);
		renderGui(batch);
	}

	private void renderWorld(SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}

	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float) height)
				* (float) width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2,
				cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
	
	// Nota: la distribución de la GUI es distinta según el menú de pausa

	private void renderGuiPîeces(SpriteBatch batch) {
		float x = -15;
		float y = -15;
		if (!worldController.isPaused()) {
			batch.draw(Assets.instance.piece.pieceIcon, x, y, 50, 50, 100, 100,
					0.35f, -0.35f, 0);
			Assets.instance.fonts.defaultBig.draw(batch, worldController.pieces
					+ "", x + 75, y + 37);
			Assets.instance.fonts.defaultBig
					.draw(batch, "Consigue " + worldController.piecesNeeded
							+ " piezas", x + 150, y + 37);
		} else {
			x = cameraGUI.viewportWidth - 200;
			y = 110;
			batch.draw(Assets.instance.piece.pieceIcon, x, y, 50, 50, 100, 100,
					0.35f, -0.35f, 0);
			if (worldController.getNivel() != 1)
				Assets.instance.fonts.defaultBig.draw(batch,
						worldController.pieces + "/"
								+ worldController.piecesNeeded, x + 75, y + 37);
			else
				Assets.instance.fonts.defaultBig.draw(batch,
						worldController.pieces + "", x + 75, y + 37);
		}
	}

	private void renderGuiTime(SpriteBatch batch) {
		float x;
		float y;
		String time = Integer.toString(worldController.time / 60);
		if (!worldController.isPaused()) {
			x = cameraGUI.viewportWidth - 450;// 600
			y = -15;
			if (worldController.time >= 0) {
				Assets.instance.fonts.defaultBig.draw(batch, "TIEMPO:", x + 75,
						y + 37);
				Assets.instance.fonts.defaultBig.draw(batch, "" + time,
						x + 220, y + 37);
			} else {
				Assets.instance.fonts.defaultBig.draw(batch, "¡HUYE!", x + 75,
						y + 37);
			}
		} else {
			x = cameraGUI.viewportWidth - 160;
			y = 70;
			if (worldController.time >= 0) {
				Assets.instance.fonts.defaultBig.draw(batch, "TIEMPO:", x, y);
				Assets.instance.fonts.defaultBig.draw(batch, "" + time, x,
						y + 37);
			} else {
				Assets.instance.fonts.defaultBig.draw(batch, "¡HUYE!", x, y);
				Assets.instance.fonts.defaultBig.draw(batch, "¡RÁPIDO!", x,
						y + 37);
			}
		}
	}

	private void renderGuiExtraLive(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 200;
		float y = -15;
		for (int i = 0; i < Constants.LIVES_START; i++) {
			if (worldController.lives <= i) {
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			}

			batch.draw(Assets.instance.life.life, x + i * 50, y, 50, 50, 120,
					100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
		}
	}

	private void renderGuiFpsCounter(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;

		if (fps >= 45) {
			// 45 o más FPS se muestran en verde
			fpsFont.setColor(0, 1, 0, 1);
		} else if (fps >= 30) {
			// Entre 30 y 44 FPS se muestra en amarillo
			fpsFont.setColor(1, 1, 0, 1);
		} else {
			// Menos de 30 FPS se muestra en rojo
			fpsFont.setColor(1, 0, 0, 1);
		}
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); // blanco
	}

	private void renderGui(SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();

		// Dibuja el icono de piezas, la cantidad actual y la necesaria para superar el nivel 
		// (anclado arriba a la izquierda)
		renderGuiPîeces(batch);
		// Dibuja el tiempo (anclado arriba al centro)
		renderGuiTime(batch);
		// Dibuja el tiempo restante de vuelo (anclado arriba a la izquierda)
		renderGuiPowerBarPowerup(batch);
		// Dibuja las vidas extra (anchored to top right edge)
		renderGuiExtraLive(batch);

		// Sólo en dispositivos móviles mostramos los botones.
		if (Gdx.app.getType() == ApplicationType.Android
				|| Gdx.app.getType() == ApplicationType.iOS) {
			renderGuiLeftButton(batch);
			renderGuiRightButton(batch);
			// renderGuiShootButton(batch);
			renderGuiJumpButton(batch);
		}
		// Dibuja el texto con los FPS (anchored to bottom right edge)
		if (GamePreferences.instance.showFpsCounter) {
			renderGuiFpsCounter(batch);
		}

		// Dibuja el texto de Game Over
		renderGuiGameOverMessage(batch);
		// Dibuja el texto de nivel completado
		renderGuiGoalMessage(batch);
		// Dibuja el menú de pausa
		renderPauseMenu(batch);

		batch.end();
	}

	private void renderGuiGameOverMessage(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;

		if (worldController.isGameOver()) {
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			fontGameOver.drawMultiLine(batch, "GAME OVER", x, y, 0,
					BitmapFont.HAlignment.CENTER);
			fontGameOver.setColor(1, 1, 1, 1);
		}
	}

	private void renderGuiGoalMessage(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;

		if (worldController.isGoalReached()) {
			BitmapFont fontGoal = Assets.instance.fonts.defaultBig;
			fontGoal.setColor(1, 0.75f, 0.25f, 1);
			fontGoal.drawMultiLine(batch, "¡NIVEL COMPLETADO!", x, y - 150, 0,
					BitmapFont.HAlignment.CENTER);
			fontGoal.setColor(1, 1, 1, 1);
		}
	}

	private void renderPauseMenu(SpriteBatch batch) {
		float x = 0;
		float y = 0;

		if (worldController.isPaused()) {
			batch.draw(Assets.instance.pause.pause, x, y, 0, 0, 280, -480, 1f,
					-1f, 0);
			batch.draw(Assets.instance.pause.title, x + 50, y, 0, 10, 150, -40,
					1f, -1f, 0);
			if (worldController.cPlay.contains((float) Gdx.input.getX(),
					(float) Gdx.input.getY()))
				batch.draw(Assets.instance.pause.playDown, x, y, 90, 90, 800,
						-800, 0.1f, -0.1f, 0);
			else
				batch.draw(Assets.instance.pause.play, x, y, 90, 90, 800, -800,
						0.1f, -0.1f, 0);
			batch.draw(Assets.instance.pause.restart, x, y, 90, 180, 800, -800,
					0.1f, -0.1f, 0);
			batch.draw(Assets.instance.pause.selectLevel, x, y, 90, 270, 800,
					-800, 0.1f, -0.1f, 0);
			batch.draw(Assets.instance.pause.home, x, y, 20, 360, 800, -800,
					0.1f, -0.1f, 0);
			batch.draw(Assets.instance.pause.sound, x, y, 180, 360, 800, -800,
					0.1f, -0.1f, 0);
			if (!GamePreferences.instance.sound)
				batch.draw(Assets.instance.pause.sound, x, y, 180, 360, 800,
						-800, 0.1f, -0.1f, 0);
		}
	}

	private void renderGuiPowerBarPowerup(SpriteBatch batch) {
		float x = -15;
		float y = 30;
		float timeLeftPowerBarPowerup = worldController.level.astronaut.timeLeftBarPowerup;
		if (timeLeftPowerBarPowerup > 0) {
			
			if (timeLeftPowerBarPowerup < 4) {
				if (((int) (timeLeftPowerBarPowerup * 5) % 2) != 0) {
					batch.setColor(1, 1, 1, 0.5f);
				}
			}

			batch.draw(Assets.instance.flyPower.bar, x, y, 50, 50, 100, 100,
					0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
			Assets.instance.fonts.defaultSmall.draw(batch, ""
					+ (int) timeLeftPowerBarPowerup, x + 60, y + 57);
		}
	}

	private void renderGuiLeftButton(SpriteBatch batch) {
		float x = 0;
		float y = cameraGUI.viewportHeight - 100;
		batch.draw(Assets.instance.leftButton.left, x, y, 50, 50, 100, 100, 1f,
				-1f, 0);
	}

	private void renderGuiRightButton(SpriteBatch batch) {
		float x = 100;
		float y = cameraGUI.viewportHeight - 100;
		batch.draw(Assets.instance.rightButton.right, x, y, 50, 50, 100, 100,
				1f, -1f, 0);
	}

	// Método usado pero conservado para una futura ampliación del juego
	private void renderGuiShootButton(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 200;
		float y = cameraGUI.viewportHeight - 100;
		batch.draw(Assets.instance.jumpButton.jump, x, y, 50, 50, 100, 100, 1f,
				-1f, 0);
	}

	private void renderGuiJumpButton(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 100;
		float y = cameraGUI.viewportHeight - 100;
		batch.draw(Assets.instance.jumpButton.jump, x, y, 50, 50, 100, 100, 1f,
				-1f, 0);
	}
}
