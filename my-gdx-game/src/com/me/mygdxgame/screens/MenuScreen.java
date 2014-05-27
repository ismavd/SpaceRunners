package com.me.mygdxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.me.mygdxgame.game.Assets;
import com.me.mygdxgame.utils.Constants;
import com.me.mygdxgame.utils.GamePreferences;
import com.me.mygdxgame.utils.CharacterSkin;
import com.me.mygdxgame.utils.AudioManager;
import com.badlogic.gdx.InputProcessor;

public class MenuScreen extends AbstractGameScreen {

	private Stage stage;
	private Skin skinSpaceRunners;

	// Botones e im�genes del men� principal.
	private Image imgBackground;
	private Image imgLogo;
	private Image imgTitle;

	private Image imgMoon;
	private Image imgAstronaut;
	private Image imgAsteroids;
	private Image imgShuttle;
	private Button btnMenuPlay;
	private Button btnMenuOptions;

	// Men� de opciones
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private Slider sldMusic;
	private SelectBox selCharSkin;
	private Image imgCharSkin;

	// Debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;

	private Skin skinLibgdx;
	private Button btnMenuExit;

	// Constructor
	public MenuScreen(DirectedGame game) {
		super(game);
	}

	private void loadSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.load();
		chkSound.setChecked(prefs.sound);
		sldSound.setValue(prefs.volSound);
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.volMusic);
		selCharSkin.setSelection(prefs.charSkin);
		onCharSkinSelected(prefs.charSkin);
	}

	private void saveSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = chkSound.isChecked();
		prefs.volSound = sldSound.getValue();
		prefs.music = chkMusic.isChecked();
		prefs.volMusic = sldMusic.getValue();
		prefs.charSkin = selCharSkin.getSelectionIndex();
		prefs.save();
	}

	private void onCharSkinSelected(int index) {
		CharacterSkin skin = CharacterSkin.values()[index];
		imgCharSkin.setColor(skin.getColor());
	}

	private void onSaveClicked() {
		saveSettings();
		onCancelClicked();
	}

	private void onCancelClicked() {
		btnMenuPlay.setVisible(true);
		btnMenuOptions.setVisible(true);
		btnMenuExit.setVisible(true);
		winOptions.setVisible(false);
		AudioManager.instance.onSettingsUpdated();
	}

	private void rebuildStage() {
		skinSpaceRunners = new Skin(
				Gdx.files.internal(Constants.SKIN_SPACERUNNERS_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

		Table layerBackground = buildBackgroundLayer();
		Table layerObjects = buildObjectsLayer();
		Table layerLogos = buildLogosLayer();
		Table layerControls = buildControlsLayer();
		Table layerOptionsWindow = buildOptionsWindowLayer();

		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerObjects);
		stack.add(layerLogos);
		stack.add(layerControls);
		stage.addActor(layerOptionsWindow);
	}

	private Table buildBackgroundLayer() {
		Table layer = new Table();
		// + Background
		imgBackground = new Image(skinSpaceRunners, "background");
		layer.add(imgBackground);
		return layer;
	}

	private Table buildObjectsLayer() {

		Table layer = new Table();

		imgMoon = new Image(skinSpaceRunners, "moon");
		layer.addActor(imgMoon);
		imgMoon.setPosition(180, 40);
		imgMoon.setWidth(300);
		imgMoon.setHeight(300);

		imgAstronaut = new Image(skinSpaceRunners, "astronaut");
		layer.addActor(imgAstronaut);
		imgAstronaut.setPosition(180, 50);
		imgAstronaut.setOrigin(imgAstronaut.getWidth(),
				imgAstronaut.getHeight());

		imgAsteroids = new Image(skinSpaceRunners, "asteroids");
		layer.addActor(imgAsteroids);
		imgAsteroids.setPosition(130, 10);
		imgAsteroids.setWidth(400);
		imgAsteroids.setHeight(400);

		imgShuttle = new Image(skinSpaceRunners, "shuttle");
		layer.addActor(imgShuttle);
		imgShuttle.setPosition(10, 250);
		imgShuttle.setWidth(120);
		imgShuttle.setHeight(110);

		// Para realizar movimientos como el de la rotaci�n de la imagen,
		// primero debemos establecer el origen de la misma.
		imgAstronaut.setOrigin(imgAstronaut.getWidth() / 2,
				imgAstronaut.getHeight() / 2);

		// Por �ltimo a�adimos una acci�n que se repita constantemente.
		imgAstronaut.addAction(Actions.forever(rotateBy(22.5f, 2,
				Interpolation.linear)));
		return layer;
	}

	private Table buildLogosLayer() {
		Table layer = new Table();

		// T�tulo
		
		imgTitle = new Image(skinSpaceRunners, "title");
		layer.addActor(imgTitle);
		imgTitle.setPosition(30, 370);
		imgTitle.setWidth(700);
		imgTitle.setHeight(100);

		// Logo de Isseral Studios

		imgLogo = new Image(skinSpaceRunners, "isseral");
		layer.addActor(imgLogo);
		imgLogo.setPosition(20, 10);
		imgLogo.setWidth(90);
		imgLogo.setHeight(120);

		if (debugEnabled) {
			layer.debug();
		}
		return layer;
	}

	private Table buildControlsLayer() {
		Table layer = new Table();
		layer.defaults().expand().right().padRight(40.0f);
		
		// Bot�n Jugar
		btnMenuPlay = new Button(skinSpaceRunners, "play");
		layer.add(btnMenuPlay);
		layer.padTop(100.0f);

		btnMenuPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked();
			}
		});

		layer.row(); // Pasamos a otra fila.

		// Bot�n de Opciones
		btnMenuOptions = new Button(skinSpaceRunners, "options");
		layer.add(btnMenuOptions);
		layer.padTop(100.0f);

		btnMenuOptions.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onOptionsClicked();
			}
		});

		layer.row(); // Pasamos a otra fila.

		// Bot�n de Salir
		btnMenuExit = new Button(skinSpaceRunners, "exit");
		layer.add(btnMenuExit);
		layer.padTop(100.0f);

		btnMenuExit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onExitClicked();
			}
		});

		if (debugEnabled) {
			layer.debug();
		}
		return layer;
	}

	// Cierra la aplicaci�n al pulsar sobre el bot�n Salir.
	protected void onExitClicked() {
		Gdx.app.exit();
	}

	private Table buildOptionsWindowLayer() {
		winOptions = new Window("Opciones", skinLibgdx);

		winOptions.add(buildOptWinAudioSettings()).row();

		winOptions.add(buildOptWinSkinSelection()).row();

		winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

		winOptions.setColor(1, 1, 1, 0.8f);

		winOptions.setVisible(false);
		if (debugEnabled) {
			winOptions.debug();
		}

		winOptions.pack();

		winOptions.setPosition(
				Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
		return winOptions;
	}

	// Pasa al men� de selecci�n de nivel al pulsar en el bot�n Jugar.
	private void onPlayClicked() {
		game.setScreen(new LevelScreen(game));
	}

	// Carga el men� de opciones al pulsar el bot�n de Opciones.
	private void onOptionsClicked() {
		loadSettings();

		// Ocultamos los botones del men� principal.
		btnMenuPlay.setVisible(false);
		btnMenuOptions.setVisible(false);
		btnMenuExit.setVisible(false);

		// Mostramos el men� de opciones.
		winOptions.setVisible(true);

	}

	private Table buildOptWinAudioSettings() {
		Table tbl = new Table();

		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE))
				.colspan(3);

		tbl.row();

		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);

		chkSound = new CheckBox("", skinLibgdx);
		tbl.add(chkSound);
		tbl.add(new Label("Sonido", skinLibgdx));
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldSound);
		sldSound.setWidth(500);
		sldSound.setHeight(50);

		tbl.row();

		chkMusic = new CheckBox("", skinLibgdx);
		tbl.add(chkMusic);
		tbl.add(new Label("Musica", skinLibgdx));
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldMusic);

		tbl.row();

		return tbl;
	}

	private Table buildOptWinSkinSelection() {
		Table tbl = new Table();

		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Skins del astronauta", skinLibgdx, "default-font",
				Color.ORANGE)).colspan(2);

		tbl.row();

		selCharSkin = new SelectBox(CharacterSkin.values(), skinLibgdx);
		selCharSkin.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onCharSkinSelected(((SelectBox) actor).getSelectionIndex());
			}
		});

		tbl.add(selCharSkin).width(120).padRight(20);

		imgCharSkin = new Image(Assets.instance.astronaut.astronaut);
		tbl.add(imgCharSkin).width(25).height(50);

		return tbl;
	}

	private Table buildOptWinButtons() {
		Table tbl = new Table();

		Label lbl = null;
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);

		tbl.row();

		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);

		tbl.row();

		btnWinOptSave = new TextButton("Guardar", skinLibgdx);
		tbl.add(btnWinOptSave).padRight(30);

		btnWinOptSave.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onSaveClicked();
			}
		});

		btnWinOptCancel = new TextButton("Cancelar", skinLibgdx);
		tbl.add(btnWinOptCancel);

		btnWinOptCancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onCancelClicked();
			}
		});

		return tbl;
	}

	@Override
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
		stage.setViewport(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT, false);
	}

	@Override
	public void show() {
		stage = new Stage();
		rebuildStage();
		AudioManager.instance.play(Assets.instance.music.song01);
	}

	@Override
	public void hide() {
		stage.dispose();
		skinSpaceRunners.dispose();
		skinLibgdx.dispose();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}
}
