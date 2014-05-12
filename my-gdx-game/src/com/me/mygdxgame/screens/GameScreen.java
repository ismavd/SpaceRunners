package com.me.mygdxgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.me.mygdxgame.game.WorldController;
import com.me.mygdxgame.game.WorldRenderer;
import com.me.mygdxgame.utils.Constants;
import com.me.mygdxgame.utils.GamePreferences;

public class GameScreen extends AbstractGameScreen {
	private static final String TAG = GameScreen.class.getName();
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;
	private int nivel;
	private int score;
	
	private boolean debugEnabled = false;
	private float debugRebuildStage;

	public GameScreen(Game game) {
		super(game);
	}
	
	public GameScreen(Game game, int level, int score) {
		super(game);
		this.nivel = level;
		this.score = score;
	}

	@Override
	public void render(float deltaTime) {
		// Do not update game world when paused.
		if (!paused) {
			// Update game world by the time that has passed
			// since last rendered frame.
			worldController.update(deltaTime);
		} else {
			worldController.updatePaused(deltaTime);
		}

		// Sets the clear screen color to: Cornflower Blue
		/*Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f,
				0xff / 255.0f);*/
		
		Gdx.gl.glClearColor(0x9 / 255.0f, 0x23 / 255.0f, 0x47 / 255.0f,
				0xff / 255.0f);

		
		
		// Clears the screen
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// Render game world to screen
		worldRenderer.render();
		//rebuildStage();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		GamePreferences.instance.load();
		worldController = new WorldController(game,nivel,score);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
		worldController.dispose();
		worldRenderer.dispose();
		dispose();
		Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		super.resume();
		// Only called on Android!
		paused = false;
	}
}
