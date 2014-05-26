package com.me.mygdxgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.assets.AssetManager;
import com.me.mygdxgame.game.Assets;
import com.me.mygdxgame.game.WorldController;
import com.me.mygdxgame.game.WorldRenderer;
import com.me.mygdxgame.screens.MenuScreen;
import com.me.mygdxgame.screens.DirectedGame;
import com.me.mygdxgame.utils.AudioManager;
import com.me.mygdxgame.utils.GamePreferences;
import com.badlogic.gdx.math.Interpolation;
import com.me.mygdxgame.screens.ScreenTransition;
import com.me.mygdxgame.screens.ScreenTransitionSlice;

public class MyGdxGame extends DirectedGame
{
	private boolean paused;
	
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	private static final String TAG = MyGdxGame.class.getName();
	
	@Override
	public void create() 
	{
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		//AudioManager.instance.play(Assets.instance.music.song01);
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	}

}
