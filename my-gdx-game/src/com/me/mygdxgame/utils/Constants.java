package com.me.mygdxgame.utils;

import java.util.HashMap;

public class Constants {
	// El mundo de juego visible es 5 metros de ancho
	public static final float VIEWPORT_WIDTH = 5.0f;
	// El mundo de juego visible es 5 metros de alto
	public static final float VIEWPORT_HEIGHT = 5.0f;
	// Anchura de la GUI superior
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	// Altura de la GUI superior
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	// Anchura de la GUI inferior
	public static final float VIEWPORT_BUTTONS_WIDTH = 100.0f;
	// Altura de la GUI inferior
	public static final float VIEWPORT_BUTTONS_HEIGHT = 50.0f;
	// Ruta del fichero del nivel 1
	public static final String LEVEL_01 = "levels/level-01.png";
	// Ruta del fichero del nivel 2
	public static final String LEVEL_02 = "levels/level-02.png";
	// Ruta del fichero del nivel 3
	public static final String LEVEL_03 = "levels/level-03.png";
	// Ruta del fichero del nivel 4
	public static final String LEVEL_04 = "levels/level-04.png";
	// Ruta del fichero del nivel 5
	public static final String LEVEL_05 = "levels/level-05.png";
	// Ruta del fichero del nivel 6
	public static final String LEVEL_06 = "levels/level-06.png";

	// Aquí se cargarán los niveles en la pantalla LevelScreen
	public static HashMap<Integer, String> niveles;

	// Vidas extra iniciales
	public static final int LIVES_START = 3;
	// Duración del power-up de vuelo
	public static final float ITEM_FEATHER_POWERUP_DURATION = 3;
	// Tiempo que dura la pantalla de Game Over
	public static final float TIME_DELAY_GAME_OVER = 5;
	// Tiempo que dura la pantalla de juego terminado
	public static final float TIME_DELAY_GAME_FINISHED = 5;

	// Fichero de preferencias
	public static final String PREFERENCES = "canyonbunny.prefs";

	// Ficheros de texturas de objetos
	public static final String TEXTURE_ATLAS_OBJECTS = "images/spacerunners.pack";
	public static final String TEXTURE_ATLAS_UI = "images/spacerunners-ui.pack";
	public static final String TEXTURE_ATLAS_PAUSE = "images/spacerunners-pause.pack";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";
	// Ficheros de skins
	public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
	public static final String SKIN_SPACERUNNERS_UI = "images/spacerunners-ui.json";
	public static final String SKIN_BUTTONS = "images/inputbuttons.json";
}
