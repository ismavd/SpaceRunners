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
import com.badlogic.gdx.InputProcessor;

public class GameScreen extends AbstractGameScreen {
	private static final String TAG = GameScreen.class.getName();
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;
	private int nivel;
	private int score;
	private int piecesNeeded;
	private int time;

	private boolean debugEnabled = false;
	private float debugRebuildStage;

	public GameScreen(DirectedGame game) {
		super(game);
	}

	public GameScreen(DirectedGame game, int level, int piecesNeeded, int score, int time) {
		super(game);
		this.nivel = level;
		this.piecesNeeded = piecesNeeded;
		this.score = score;
		this.time = time;
	}

	@Override
	public void render(float deltaTime) {
		// No actualizar el nivel con el juego en pausa
		if (!paused) {
			// Actualizaci�n por cada frame renderizado
			worldController.update(deltaTime);
		} else {
			worldController.updatePaused(deltaTime);
		}
		
		// Se establece el color del cielo
		Gdx.gl.glClearColor(0x9 / 255.0f, 0x23 / 255.0f, 0x47 / 255.0f,
				0xff / 255.0f);

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		GamePreferences.instance.load();
		worldController = new WorldController(game, nivel, piecesNeeded, score, time);
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
		// La siguiente instruci�n se llama s�lo en Android
		paused = false;
	}

	@Override
	public InputProcessor getInputProcessor() {
		return worldController;
	}
}
