package com.me.mygdxgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public class AbstractGameScreen implements Screen
{
	protected DirectedGame game;
	
	public AbstractGameScreen (DirectedGame game)
	{
		this.game = game;
	}

	@Override
	public void render(float delta) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub
		//Assets.instance.init(new AssetManager());
	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub
		//Assets.instance.dispose();
	}

	public InputProcessor getInputProcessor() {
		// TODO Auto-generated method stub
		return null;
	}

}
