package com.me.mygdxgame.screens;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.me.mygdxgame.game.Assets;
import com.me.mygdxgame.utils.Constants;
import com.me.mygdxgame.utils.GamePreferences;
import com.me.mygdxgame.utils.CharacterSkin;

public class LevelScreen extends AbstractGameScreen{

	private Stage stage;
	private Skin skinCanyonBunny;
	// menu
	private Image imgBackground;
	private Image imgLogo;
	private Image imgInfo;
	private Image imgCoins;
	private Image imgBunny;
	private Button btnMenuPlay;
	private Button btnMenuOptions;
	// level menu
	private Button btnMenuPlay1;
	private Button btnMenuPlay2;
	private Button btnMenuPlay3;
	private Button btnMenuPlay4;
	private Button btnMenuPlay5;
	// options
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private Slider sldMusic;
	private SelectBox selCharSkin;
	private Image imgCharSkin;
	private CheckBox chkShowFpsCounter;
	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;
	
	private static final String TAG = MenuScreen.class.getName();

	private Skin skinLibgdx;

	
	public LevelScreen(Game game) {
		super(game);
		cargarNiveles();
	}
	
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
				Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

		// build all layers
		Table layerBackground = buildBackgroundLayer();
		Table layerObjects = buildObjectsLayer();
		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerObjects);
	}

	
	private Table buildBackgroundLayer() {
		Table layer = new Table();
		// + Background
		imgBackground = new Image(skinCanyonBunny, "background");
		layer.add(imgBackground);
		return layer;
	}

	private Table buildObjectsLayer() {
		Table layer = new Table();
		//layer.add().expand(100, 100);
		layer.left().top();
		// + Play Button
		btnMenuPlay = new Button(skinCanyonBunny, "level-01");
		btnMenuPlay.setPosition(0, 0);
		layer.add(btnMenuPlay);
		
		/*layer.defaults().expand().fill().padBottom(4f);
		layer.columnDefaults(0).left();
		layer.columnDefaults(1).right().width(50f);
*/		
//		layer.setPosition(0, 0);
		
		btnMenuPlay.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(1);
			}
		});
		
		btnMenuPlay1 = new Button(skinCanyonBunny, "level-02");
		//btnMenuPlay1.setPosition(0, 100);
		layer.add(btnMenuPlay1);
		//layer.add(btnMenuPlay1);
		//layer.setPosition(0, 0);
		
		btnMenuPlay1.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(2);
			}
		});
		
		//layer.row();
		
		btnMenuPlay2 = new Button(skinCanyonBunny, "level-03");
		btnMenuPlay2.setPosition(0, 50);
		layer.add(btnMenuPlay2);
		//layer.add(btnMenuPlay2);
		//layer.setPosition(0, 0);
		
		btnMenuPlay2.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(3);
			}
		});
		
		layer.row();
		
		btnMenuPlay3 = new Button(skinCanyonBunny, "level-04");
		btnMenuPlay3.setPosition(0, 50);
		layer.add(btnMenuPlay3);
		//layer.add(btnMenuPlay3);
		//layer.setPosition(0, 0);
		
		btnMenuPlay3.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(4);
			}
		});
		
		//layer.row();
		
		btnMenuPlay4 = new Button(skinCanyonBunny, "level-05");
		btnMenuPlay4.setPosition(0, 50);
		layer.add(btnMenuPlay4);
		//layer.add(btnMenuPlay4);
		//layer.setPosition(0, 0);
		
		btnMenuPlay4.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(5);
			}
		});
		
		//layer.row();
		
		btnMenuPlay5 = new Button(skinCanyonBunny, "level-06");
		btnMenuPlay5.setPosition(0, 50);
		layer.add(btnMenuPlay5);
		//layer.add(btnMenuPlay5);
		//layer.setPosition(0, 0);
		
		btnMenuPlay5.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked(6);
			}
		});
		
		//layer.row();
		
		if (debugEnabled)
			layer.debug();
		return layer;
	}
	
	private void onPlayClicked(int level) {
		game.setScreen(new GameScreen(game,level));
	}
	
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}

	@Override
	public void hide() {
		stage.dispose();
		skinCanyonBunny.dispose();
		skinLibgdx.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
	}
	
	public void render(float deltaTime) {
		// TODO Auto-generated method stub
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
}
