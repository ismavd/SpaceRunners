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

/**
 * @author Isma
 *
 */
public class MyGdxGame extends Game
{
	//private OrthographicCamera camera;
	//private SpriteBatch batch;
	//private Texture texture;
	//private Sprite sprite;
	//private float rot;
	
	private boolean paused;
	
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	private static final String TAG = MyGdxGame.class.getName();
	
	 // constant useful for logging
    //public static final String LOG = MyGdxGame.class.getSimpleName();

    // a libgdx helper class that logs the current FPS each second
    //private FPSLogger fpsLogger;
	
	@Override
	public void create() 
	{
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Start game at menu screen
		setScreen(new MenuScreen(this));
		
		// Initialize controller and renderer
		//worldController = new WorldController();
		//worldRenderer = new WorldRenderer(worldController);
	}

//	
//	
//	@Override
//	public void resize(int width, int height) 
//	{
//		// TODO Auto-generated method stub
//		//Gdx.app.log( MyGdxGame.LOG, "Resizing game to: " + width + " x " + height );
//		worldRenderer.resize(width, height);
//	}
//
//	@Override
//	public void render() 
//	{
//		// TODO Auto-generated method stub
//		// the following code clears the screen with the given RGB color (green)
//		
//		// Do not update game world when paused.
//		if (!paused) 
//		{
//			// Update game world by the time that has passed
//			// since last rendered frame.
//			worldController.update(Gdx.graphics.getDeltaTime());
//		}
//		
//		// Sets the clear screen color to: Cornflower Blue
//		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
//		// Clears the screen
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		// Render game world to screen
//		worldRenderer.render();
//		
//		
//		/*
//        Gdx.gl.glClearColor( 1f, 1f, 1f, 1f );
//        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
//
//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
//        
//        final float degressPerSecond = 10.0f;
//        rot = (rot + Gdx.graphics.getDeltaTime() *
//        degressPerSecond) % 360;
//        sprite.setRotation(rot);
//        
//        final float shakeAmplitudeInDegrees = 5.0f;
//        float shake = MathUtils.sin(rot) * shakeAmplitudeInDegrees;
//        sprite.setRotation(shake);
//        
//        sprite.draw(batch);
//        batch.end();*/
//        
//        // output the current FPS
//        //fpsLogger.log();
//	}
//
//	@Override
//	public void pause() 
//	{
//		// TODO Auto-generated method stub
//		//Gdx.app.log( MyGdxGame.LOG, "Pausing game" );
//		paused = true;
//	}
//
//	@Override
//	public void resume() 
//	{
//		Assets.instance.init(new AssetManager());
//		paused = false;
//	}
//
//	@Override
//	public void dispose() 
//	{
//		worldRenderer.dispose();
//		Assets.instance.dispose();
//	}
	
	
}
