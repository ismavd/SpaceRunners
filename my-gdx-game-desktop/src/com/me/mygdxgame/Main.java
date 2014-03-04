package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
<<<<<<< HEAD

public class Main 
{
	public static void main(String[] args) 
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "My Game";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 600;
=======
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
			"../CanyonBunny-android/assets/images",
			"canyonbunny.pack");
		}
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Space Runners";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
>>>>>>> refs/remotes/origin/master
		
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
