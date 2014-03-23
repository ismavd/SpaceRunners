package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Main 
{
	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = true;
	
	public static void main(String[] args) 
	{
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.debug = drawDebugOutline;
			TexturePacker2.process(settings, "assets-raw/images",
			"../my-gdx-game-android/assets/images",
			"canyonbunny.pack");
			TexturePacker2.process(settings, "assets-raw/images-ui",
					"../my-gdx-game-android/assets/images",
					"canyonbunny-ui.pack");
		}
		
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Space Runners";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
		
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
