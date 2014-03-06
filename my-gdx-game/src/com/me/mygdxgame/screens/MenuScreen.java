package com.me.mygdxgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class MenuScreen extends AbstractGameScreen
{
	private static final String TAG = MenuScreen.class.getName();
	
	public MenuScreen (Game game) 
	{
		super(game);
	}

	@Override
	public void render(float delta) 
	{
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isTouched())
		{
			game.setScreen(new GameScreen(game));
		}
	}

	@Override
	public void resize(int width, int height) 
	{
		// TODO Auto-generated method stub
		super.resize(width, height);
	}

	@Override
	public void show() 
	{
		// TODO Auto-generated method stub
		super.show();
	}

	@Override
	public void hide() 
	{
		// TODO Auto-generated method stub
		super.hide();
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub
		super.pause();
	}
	
	
}
