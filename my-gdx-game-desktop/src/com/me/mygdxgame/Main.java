package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Main {
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;

	public static void main(String[] args) {
		if (rebuildAtlas) {
			Settings settings = new Settings();
		
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.debug = drawDebugOutline;

			TexturePacker2.process(settings, "assets-raw/images",
					"../my-gdx-game-android/assets/images", "spacerunners.pack");
			TexturePacker2.process(settings, "assets-raw/images-ui",
					"../my-gdx-game-android/assets/images",
					"spacerunners-ui.pack");
			TexturePacker2.process(settings, "assets-raw/images-pause",
					"../my-gdx-game-android/assets/images", "spacerunners-pause.pack");
			TexturePacker2.process(settings, "assets-raw/images",
					"bin/images", "spacerunners.pack");
			TexturePacker2.process(settings, "assets-raw/images-ui",
					"bin/images",
					"spacerunners-ui.pack");
			TexturePacker2.process(settings, "assets-raw/images-pause",
					"bin/images", "spacerunners-pause.pack");
			
		}

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Space Runners";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 480;

		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
