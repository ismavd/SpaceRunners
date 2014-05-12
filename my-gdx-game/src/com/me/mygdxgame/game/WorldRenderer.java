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
import com.me.mygdxgame.objects.BunnyHead;
import com.me.mygdxgame.objects.Enemy;
import com.me.mygdxgame.objects.Laser;
import com.me.mygdxgame.utils.Constants;
import com.me.mygdxgame.utils.GamePreferences;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class WorldRenderer implements Disposable 
{
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;

	/*private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
	private Box2DDebugRenderer b2debugRenderer;*/

	//Constructor
	public WorldRenderer(WorldController worldController) 
	{
		this.worldController = worldController;
		init();
	}

	
	private void init() 
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true); // flip y-axis
		cameraGUI.update();
		//b2debugRenderer = new Box2DDebugRenderer();
	}

	
	public void render() 
	{
		renderWorld(batch);
		renderGui(batch);
	}

	
	private void renderWorld(SpriteBatch batch) 
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
		
		/*if (DEBUG_DRAW_BOX2D_WORLD) {
			b2debugRenderer.render(worldController.b2world, camera.combined);
		}*/
	}

	
	public void resize(int width, int height) 
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float) height) * (float) width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
	}

	@Override
	public void dispose() 
	{
		batch.dispose();
	}

	private void renderGuiScrews(SpriteBatch batch) 
	{
		float x = -15;
		float y = -15;
		if (!worldController.isPaused()) {
			batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.screws, x + 75, y + 37);
		} else {
			
			x = cameraGUI.viewportWidth - 190;
			y = 40;
			batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.screws, x + 75, y + 37);
			
			// Cuando decidamos incluir puntuación habrá que poner este código
			/*x = cameraGUI.viewportWidth - 200;
			y = 110;
			batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.screws, x + 75, y + 37);*/
		}
	}
	
	private void renderGuiScore(SpriteBatch batch) 
	{
		/*float x;
		float y;
		if (!worldController.isPaused()) {
			x = cameraGUI.viewportWidth - 600;
			y = -15;
			Assets.instance.fonts.defaultBig.draw(batch, "PUNTOS:", x + 75, y + 37);
			Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x + 220, y + 37);
		} else {
			x = cameraGUI.viewportWidth - 160;
			y = 70;
			Assets.instance.fonts.defaultBig.draw(batch, "PUNTOS:", x, y);
			Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x, y + 37);
		}*/
	}

	private void renderGuiExtraLive(SpriteBatch batch) 
	{
		float x = cameraGUI.viewportWidth - 200;
		float y = -15;
		for (int i = 0; i < Constants.LIVES_START; i++)
		{
			if (worldController.lives <= i)
			{
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			}
			
			batch.draw(Assets.instance.bunny.head, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
		}
	}

	
	private void renderGuiFpsCounter(SpriteBatch batch) 
	{
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		
		if (fps >= 45) 
		{
			// 45 or more FPS show up in green
			fpsFont.setColor(0, 1, 0, 1);
		} 
		else if (fps >= 30) 
		{
			// 30 or more FPS show up in yellow
			fpsFont.setColor(1, 1, 0, 1);
		} 
		else 
		{
			// less than 30 FPS show up in red
			fpsFont.setColor(1, 0, 0, 1);
		}
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); // white
	}

	
	private void renderGui(SpriteBatch batch) 
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		
		// draw collected gold coins icon + text
		// (anchored to top left edge)
		renderGuiScrews(batch);
		// draw total score (anchored to top center)
		renderGuiScore(batch);
		// draw collected feather icon (anchored to top left edge)
		renderGuiFeatherPowerup(batch);
		// draw extra lives icon + text (anchored to top right edge)
		renderGuiExtraLive(batch);
		
		// //Sólo en dispositivos móviles mostramos los botones.
		if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS) 
		{
			renderGuiLeftButton(batch);
			renderGuiRightButton(batch);
			renderGuiShootButton(batch);
			renderGuiJumpButton(batch);
		}
		// draw FPS text (anchored to bottom right edge)
		if (GamePreferences.instance.showFpsCounter)
		{
			renderGuiFpsCounter(batch);
		}
		
		// draw game over text
		renderGuiGameOverMessage(batch);
		// draw goal text
		renderGuiGoalMessage(batch);
		// draw pause menu
		renderPauseMenu(batch);
		
		batch.end();
	}

	private void renderGuiGameOverMessage(SpriteBatch batch) 
	{
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;
		
		if (worldController.isGameOver()) 
		{
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			fontGameOver.drawMultiLine(batch, "GAME OVER", x, y, 0, BitmapFont.HAlignment.CENTER);
			fontGameOver.setColor(1, 1, 1, 1);
		}
	}

	private void renderGuiGoalMessage(SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;
		
		if (worldController.isGoalReached()) 
		{
			BitmapFont fontGoal = Assets.instance.fonts.defaultBig;
			fontGoal.setColor(1, 0.75f, 0.25f, 1);
			fontGoal.drawMultiLine(batch, "LEVEL COMPLETED!", x, y - 150, 0, BitmapFont.HAlignment.CENTER);
			fontGoal.setColor(1, 1, 1, 1);
		}
	}
	
	
	private void renderPauseMenu(SpriteBatch batch) 
	{
		float x = 0;
		float y = 0;
		
		if (worldController.isPaused()) 
		{
			batch.draw(Assets.instance.pause.pause, x, y, 0, 0, 280, -480, 1f, -1f, 0);
			batch.draw(Assets.instance.pause.title, x + 50, y, 0, 10, 150, -40, 1f, -1f, 0);
			batch.draw(Assets.instance.pause.play, x, y, 90, 90, 800, -800, 0.1f, -0.1f, 0);
			batch.draw(Assets.instance.pause.restart, x, y, 90, 180, 800, -800, 0.1f, -0.1f, 0);
			batch.draw(Assets.instance.pause.selectLevel, x, y, 90, 270, 800, -800, 0.1f, -0.1f, 0);
			batch.draw(Assets.instance.pause.home, x, y, 20, 360, 800, -800, 0.1f, -0.1f, 0);
			batch.draw(Assets.instance.pause.sound, x, y, 180, 360, 800, -800, 0.1f, -0.1f, 0);
			if (!GamePreferences.instance.sound)
				batch.draw(Assets.instance.pause.sound, x, y, 180, 360, 800, -800, 0.1f, -0.1f, 0);
		}
	}

	
	private void renderGuiFeatherPowerup(SpriteBatch batch)
	{
		float x = -15;
		float y = 30;
		float timeLeftFeatherPowerup = worldController.level.bunnyHead.timeLeftFeatherPowerup;
		if (timeLeftFeatherPowerup > 0) 
		{
			// Start icon fade in/out if the left power-up time
			// is less than 4 seconds. The fade interval is set
			// to 5 changes per second.
			if (timeLeftFeatherPowerup < 4) 
			{
				if (((int) (timeLeftFeatherPowerup * 5) % 2) != 0) 
				{
					batch.setColor(1, 1, 1, 0.5f);
				}
			}
			
			batch.draw(Assets.instance.feather.feather, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
			Assets.instance.fonts.defaultSmall.draw(batch, "" + (int) timeLeftFeatherPowerup, x + 60, y + 57);
		}
	}
	
	
	private void renderGuiLeftButton(SpriteBatch batch) 
	{
		float x = 0;
		float y = cameraGUI.viewportHeight - 100;
		batch.draw(Assets.instance.leftButton.left, x, y, 50, 50, 100, 100, 1f, -1f, 0);
	}
	
	private void renderGuiRightButton(SpriteBatch batch) 
	{
		float x = 100;
		float y = cameraGUI.viewportHeight - 100;
		batch.draw(Assets.instance.rightButton.right, x, y, 50, 50, 100, 100, 1f, -1f, 0);
	}
	
	private void renderGuiShootButton(SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 200;
		float y = cameraGUI.viewportHeight - 100;
		batch.draw(Assets.instance.jumpButton.jump, x, y, 50, 50, 100, 100, 1f, -1f, 0);
	}
	
	private void renderGuiJumpButton(SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 100;
		float y = cameraGUI.viewportHeight - 100;
		batch.draw(Assets.instance.jumpButton.jump, x, y, 50, 50, 100, 100, 1f, -1f, 0);
	}
}
