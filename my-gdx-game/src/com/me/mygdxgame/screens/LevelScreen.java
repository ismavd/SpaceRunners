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
import com.esotericsoftware.tablelayout.Cell;
import com.me.mygdxgame.game.Assets;
import com.me.mygdxgame.utils.Constants;
import com.me.mygdxgame.utils.GamePreferences;
import com.me.mygdxgame.utils.CharacterSkin;

public class LevelScreen extends AbstractGameScreen
{

	private Stage stage;
	private Skin skinCanyonBunny;
	
	private Image imgBackground;
	
	// Menú de selección de nivel.
	private Button btnNivel1;
	private Button btnNivel2;
	private Button btnNivel3;
	private Button btnNivel4;
	private Button btnNivel5;
	private Button btnNivel6;
	
	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;
	
	private static final String TAG = MenuScreen.class.getName();

	private Skin skinLibgdx;

	/*-----------------------------------------MÉTODOS----------------------------------------------*/
	
	// Constructor
	public LevelScreen(Game game) 
	{
		super(game);
		cargarNiveles();
	}
	
	// 
	private void cargarNiveles() 
	{
		Constants.niveles = new HashMap<Integer, String>();
		Constants.niveles.put(1, Constants.LEVEL_01);
		Constants.niveles.put(2, Constants.LEVEL_02);
		Constants.niveles.put(3, Constants.LEVEL_03);
		Constants.niveles.put(4, Constants.LEVEL_04);
		Constants.niveles.put(5, Constants.LEVEL_05);
		Constants.niveles.put(6, Constants.LEVEL_06);
	}
	
	
	private void rebuildStage() 
	{
		skinCanyonBunny = new Skin(Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

		// Generamos las diferentes capas de los menús.
		Table layerBackground = buildBackgroundLayer(); //Capa con el fondo de pantalla.
		Table layerObjects = buildObjectsLayer(); //Capa con 
		
		// assemble stage for menu screen
		stage.clear(); //Limpiamos 
		
		//Creamos una nueva pila donde incluiremos las capas con las diferentes imágenes.
		Stack stack = new Stack();  
		stage.addActor(stack); //Añadimos la pila.
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT); //Adaptamos el tamaño de la pila a la ventana de juego.
		
		//Añadimos las capas a la pila.
		stack.add(layerBackground);
		stack.add(layerObjects);
	}

	//Devuelve un elemento Table (capa) que contiene el fondo del menú principal.
	private Table buildBackgroundLayer() 
	{
		Table layer = new Table();
		imgBackground = new Image(skinCanyonBunny, "background"); //Buscamos la imágen de fondo.
		layer.add(imgBackground); //La añadimos a la capa.
		
		return layer;
	}

	
	private Table buildObjectsLayer() 
	{
		Table layer = new Table();

		// Distribuimos las columnas que se añadan para que las de los extremos se junten.
		layer.defaults().expand(); //Expandimos las celdas de la tabla para distribuirlas de igual forma por la pantalla.
		layer.columnDefaults(0).padLeft(100.0f);
		layer.columnDefaults(2).padRight(100.0f);
		
		btnNivel1 = new Button(skinCanyonBunny, "level-01");
		layer.add(btnNivel1); //Añadimos el primer botón
		
		btnNivel1.addListener(new ChangeListener() 
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				onPlayClicked(1,0);
			}
		});
		
		//Botón 2
		btnNivel2 = new Button(skinCanyonBunny, "level-02");
		layer.add(btnNivel2);
		
		btnNivel2.addListener(new ChangeListener() 
		{
			public void changed(ChangeEvent event, Actor actor) 
			{
				onPlayClicked(2,0);
			}
		});
		
		//Botón 3
		btnNivel3 = new Button(skinCanyonBunny, "level-03");
		layer.add(btnNivel3);
		
		btnNivel3.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor) 
			{
				onPlayClicked(3,0);
			}
		});
		
		layer.row(); //Coloca los siguientes elementos de la capa en otra fila.
		
		//Botón 4
		btnNivel4 = new Button(skinCanyonBunny, "level-04");
		layer.add(btnNivel4);

		btnNivel4.addListener(new ChangeListener() 
		{
			public void changed(ChangeEvent event, Actor actor) 
			{
				onPlayClicked(4,0);
			}
		});
		
		//Botón 5
		btnNivel5 = new Button(skinCanyonBunny, "level-05");
		layer.add(btnNivel5);

		btnNivel5.addListener(new ChangeListener() 
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				onPlayClicked(5,0);
			}
		});
		
		//Botón 6
		btnNivel6 = new Button(skinCanyonBunny, "level-06");
		layer.add(btnNivel6);

		btnNivel6.addListener(new ChangeListener() 
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				onPlayClicked(6,0);
			}
		});
		
		if (debugEnabled)
		{
			layer.debug();
		}
		return layer;
	}
	
	// Carga el nivel seleccionado al pulsar sobre el botón correspondiente.
	private void onPlayClicked(int level, int score) 
	{
		game.setScreen(new GameScreen(game,level,score));
	}
	
	
	@Override
	public void show() 
	{
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}

	
	@Override
	public void hide() 
	{
		stage.dispose();
		skinCanyonBunny.dispose();
		skinLibgdx.dispose();
	}

	
	@Override
	public void pause() 
	{
		super.pause();
	}
	
	//Renderiza la pantalla del menú principal.
	public void render(float deltaTime) 
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (debugEnabled) 
		{
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0) 
			{
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
		
		stage.act(deltaTime);
		stage.draw();
		Table.drawDebug(stage);
	}
	
	@Override
	public void resize(int width, int height) 
	{
		// Establecemos de nuevo los elementos del menú al nuevo tamaño de ventana.
		stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, false);
	}
}
