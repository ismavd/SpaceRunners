package com.me.mygdxgame.screens;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.me.mygdxgame.game.Assets;
import com.me.mygdxgame.utils.AudioManager;
import com.me.mygdxgame.utils.Constants;

public class LevelScreen extends AbstractGameScreen {

	private Stage stage;
	private Skin skinCanyonBunny;

	private Image imgBackground;

	// Men� de selecci�n de nivel.
	private Button btnNivel1;
	private Button btnNivel2;
	private Button btnNivel3;
	private Button btnNivel4;
	private Button btnNivel5;
	private Button btnNivel6;
	private Button btnExit;
	private Button btnNext;

	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;

	private Skin skinLibgdx;

	/*-----------------------------------------M�TODOS----------------------------------------------*/

	// Constructor
	public LevelScreen(DirectedGame game) {
		super(game);
		cargarNiveles();
	}

	//
	private void cargarNiveles() {
		Constants.niveles = new HashMap<Integer, String>();
		Constants.niveles.put(1, Constants.LEVEL_01);
		Constants.niveles.put(2, Constants.LEVEL_02);
		Constants.niveles.put(3, Constants.LEVEL_03);
		Constants.niveles.put(4, Constants.LEVEL_04);
		Constants.niveles.put(5, Constants.LEVEL_05);
		Constants.niveles.put(6, Constants.LEVEL_06);
	}

	private void rebuildStage() {
		skinCanyonBunny = new Skin(
				Gdx.files.internal(Constants.SKIN_SPACERUNNERS_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

		// Generamos las diferentes capas de los men�s.
		Table layerBackground = buildBackgroundLayer(); // Capa con el fondo de
														// pantalla.
		Table layerObjects = buildObjectsLayer(); // Capa con los botones de
													// nivel
		Table layerExit = buildExitLayer(); // Capa con el bot�n de salida
		Table layerNextWorld = buildNextWorldLayer(); // Capa con el bot�n de
														// cambio de mundo
		// layerExit.setSize(1, 1);
		stage.clear(); // Limpiamos

		// Creamos una nueva pila donde incluiremos las capas con las diferentes
		// im�genes.
		Stack stack = new Stack();
		stage.addActor(stack); // A�adimos la pila.
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT); // Adaptamos el tama�o de la
												// pila a la ventana de juego.

		// A�adimos las capas a la pila.
		stack.add(layerBackground);
		stack.add(layerObjects);
		stack.add(layerExit);
		stack.add(layerNextWorld);
	}

	// Devuelve un elemento Table (capa) que contiene el fondo del men�
	// principal.
	private Table buildBackgroundLayer() {
		Table layer = new Table();
		imgBackground = new Image(skinCanyonBunny, "background"); // Buscamos la
																	// im�gen de
																	// fondo.
		layer.add(imgBackground); // La a�adimos a la capa.

		return layer;
	}

	private Table buildObjectsLayer() {
		Table layer = new Table();

		// Distribuimos las columnas que se a�adan para que las de los extremos
		// se junten.
		layer.defaults().expand(); // Expandimos las celdas de la tabla para
									// distribuirlas de igual forma por la
									// pantalla.
		layer.columnDefaults(0).padLeft(100.0f);
		layer.columnDefaults(2).padRight(100.0f);

		btnNivel1 = new Button(skinCanyonBunny, "level-01");
		layer.add(btnNivel1); // A�adimos el primer bot�n

		btnNivel1.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(1, 20, 0, 151);
			}
		});

		// Bot�n 2
		btnNivel2 = new Button(skinCanyonBunny, "level-02");
		layer.add(btnNivel2);

		btnNivel2.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(2, 20, 0, 151);
			}
		});

		// Bot�n 3
		btnNivel3 = new Button(skinCanyonBunny, "level-03");
		layer.add(btnNivel3);

		btnNivel3.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(3, 20, 0, 151);
			}
		});

		layer.row(); // Coloca los siguientes elementos de la capa en otra fila.

		// Bot�n 4
		btnNivel4 = new Button(skinCanyonBunny, "level-04");
		layer.add(btnNivel4);

		btnNivel4.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(4, 20, 0, 151);
			}
		});

		// Bot�n 5
		btnNivel5 = new Button(skinCanyonBunny, "level-05");
		layer.add(btnNivel5);

		btnNivel5.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(5, 20, 0, 151);
			}
		});

		// Bot�n 6
		btnNivel6 = new Button(skinCanyonBunny, "level-06");
		layer.add(btnNivel6);

		btnNivel6.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(6, 20, 0, -1);
			}
		});

		if (debugEnabled) {
			layer.debug();
		}
		return layer;
	}

	private Table buildExitLayer() {
		Table layer = new Table();
		layer.padLeft(-700.0f);
		layer.padBottom(-300.0f);
		btnExit = new Button(skinCanyonBunny, "back");
		layer.add(btnExit);
		btnExit.setWidth(150);
		btnExit.setHeight(150);
		btnExit.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onExitClicked();
			}
		});
		return layer;
	}

	private Table buildNextWorldLayer() {
		Table layer = new Table();
		layer.padLeft(700.0f);
		btnNext = new Button(skinCanyonBunny, "next");
		layer.add(btnNext);
		return layer;
	}

	// Carga el nivel seleccionado al pulsar sobre el bot�n correspondiente.
	private void onPlayClicked(int level, int pieces, int score, int time) {
		ScreenTransition transition = ScreenTransitionSlice.init(2,
				ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
		game.setScreen(new GameScreen(game, level, pieces, score, time),
				transition); 
	}

	// Para salir de la pantalla
	private void onExitClicked() {
		game.setScreen(new MenuScreen(game));
	}

	@Override
	public void show() {
		stage = new Stage();
		rebuildStage();
		//AudioManager.instance.play(Assets.instance.music.song01);
	}

	@Override
	public void hide() {
		stage.dispose();
		skinCanyonBunny.dispose();
		skinLibgdx.dispose();
	}

	@Override
	public void pause() {
		super.pause();
	}

	// Renderiza la pantalla del men� principal.
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (debugEnabled) {
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0) {
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
		stage.act(deltaTime);
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		// Establecemos de nuevo los elementos del men� al nuevo tama�o de
		// ventana.
		stage.setViewport(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT, false);
	}

	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}
}
