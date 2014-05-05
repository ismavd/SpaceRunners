package com.me.mygdxgame.utils;

import java.util.HashMap;

public class Constants {
	// Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 5.0f;
	// Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 5.0f;
	// GUI Width
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	// GUI Height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	// GUI Width
	public static final float VIEWPORT_BUTTONS_WIDTH = 100.0f;
	// GUI Height
	public static final float VIEWPORT_BUTTONS_HEIGHT = 50.0f;
	// Location of image file for level 01
	public static final String LEVEL_01 = "levels/level-01.png";
	// Location of image file for level 02
	public static final String LEVEL_02 = "levels/level-02.png";
	// Location of image file for level 02
	public static final String LEVEL_03 = "levels/level-03.png";
	// Location of image file for level 02
	public static final String LEVEL_04 = "levels/level-04.png";
	// Location of image file for level 02
	public static final String LEVEL_05 = "levels/level-05.png";
	// Location of image file for level 02
	public static final String LEVEL_06 = "levels/level-06.png";

	// Aquí se cargarán los niveles en la pantalla LevelScreen
	public static HashMap<Integer, String> niveles;

	// Amount of extra lives at level start
	public static final int LIVES_START = 3;
	// Duration of feather power-up in seconds
	public static final float ITEM_FEATHER_POWERUP_DURATION = 9;
	// Delay after game over
	public static final float TIME_DELAY_GAME_OVER = 3;
	// Delay after game finished
	public static final float TIME_DELAY_GAME_FINISHED = 3;

	// Game preferences file
	public static final String PREFERENCES = "canyonbunny.prefs";

	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/spacerunners.pack";
	public static final String TEXTURE_ATLAS_UI = "images/spacerunners-ui.pack";
	public static final String TEXTURE_ATLAS_PAUSE = "images/spacerunners-pause.pack";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";
	// Location of description file for skins
	public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
	public static final String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";
	public static final String SKIN_BUTTONS = "images/inputbuttons.json";
}
