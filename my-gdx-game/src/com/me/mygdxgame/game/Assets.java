package com.me.mygdxgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.me.mygdxgame.objects.Laser;
import com.me.mygdxgame.utils.Constants;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Assets implements Disposable, AssetErrorListener 
{

	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	public AssetBunny bunny;
	public AssetBunnyPower bunnyPower;
	
	//Enemies
	public AssetEnemy enemy;
	public AssetEnemyForward enemyFwd;
	public AssetGiant giant;
	
	//Platforms
	public AssetRock rock;
	public AssetPlatform platform;
	public AssetMovingPlatform movingPlatform;
	
	// Boxes
	public AssetBox box;
	
	// Laser
	public AssetLaser laser;
	
	//Items
	public AssetGoldCoin goldCoin;
	public AssetFeather feather;
	public AssetCheckpoint checkpoint;
	public AssetCarrot carrot;
	
	public AssetLevelDecoration levelDecoration;
	public AssetFonts fonts;
	
	// Input buttons
	public AssetLeftButton leftButton;
	public AssetRightButton rightButton;
	public AssetJumpButton jumpButton;
	
	// Pause menu
	public AssetPause pause;
	//public Asset
	
	// Sound
	public AssetSounds sounds;
	public AssetMusic music;

	// singleton: prevent instantiation from other classes
	private Assets() 
	{
		
	}

	public void init(AssetManager assetManager) 
	{
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_PAUSE, TextureAtlas.class);
		// load sounds
		assetManager.load("sounds/jump.wav", Sound.class);
		assetManager.load("sounds/jump_with_feather.wav", Sound.class);
		assetManager.load("sounds/pickup_coin.wav", Sound.class);
		assetManager.load("sounds/pickup_feather.wav", Sound.class);
		assetManager.load("sounds/live_lost.wav", Sound.class);
		// load music
		assetManager.load("music/keith303_-_brand_new_highscore.mp3", Music.class);
		//assetManager.load("music/song02.mp3", Music.class);
		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG,
				"# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureAtlas atlasPause = assetManager.get(Constants.TEXTURE_ATLAS_PAUSE);
		for (Texture t : atlasPause.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// create game resource objects
		fonts = new AssetFonts();
		bunny = new AssetBunny(atlas);
		bunnyPower = new AssetBunnyPower(atlas);
		enemy = new AssetEnemy(atlas);
		enemyFwd = new AssetEnemyForward(atlas);
		giant = new AssetGiant(atlas);
		rock = new AssetRock(atlas);
		platform = new AssetPlatform(atlas);
		movingPlatform = new AssetMovingPlatform(atlas);
		box = new AssetBox(atlas);
		laser = new AssetLaser(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		feather = new AssetFeather(atlas);
		checkpoint = new AssetCheckpoint(atlas);
		// Carrot
		carrot = new AssetCarrot(atlas);
		// Buttons
		leftButton = new AssetLeftButton(atlas);
		rightButton = new AssetRightButton(atlas);
		jumpButton = new AssetJumpButton(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		// Sound
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
		// Pause menu
		pause = new AssetPause(atlasPause);
	}

	@Override
	public void dispose() 
	{
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) 
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception) throwable);
	}

	/*
	 * @Override public void error(String filename, Class type, Throwable
	 * throwable) { Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'",
	 * (Exception) throwable); }
	 */

	public class AssetBunny 
	{
		public final AtlasRegion head;
		public final Animation animNormal;
		public final Animation animCopterTransform;
		public final Animation animCopterTransformBack;
		public final Animation animCopterRotate;

		public AssetBunny(TextureAtlas atlas) 
		{
			head = atlas.findRegion("bunny_head");
			Array<AtlasRegion> regions = null;
			AtlasRegion region = null;
			// Animation: Bunny Normal
			regions = atlas.findRegions("anim_bunny_normal");
			animNormal = new Animation(1.0f / 10.0f, regions,
			Animation.LOOP_PINGPONG);
			// Animation: Bunny Copter - knot ears
			regions = atlas.findRegions("anim_bunny_copter");
			animCopterTransform = new Animation(1.0f / 10.0f, regions);
			// Animation: Bunny Copter - unknot ears
			regions = atlas.findRegions("anim_bunny_copter");
			animCopterTransformBack = new Animation(1.0f / 10.0f, regions,
			Animation.REVERSED);
			// Animation: Bunny Copter - rotate ears
			regions = new Array<AtlasRegion>();
			regions.add(atlas.findRegion("anim_bunny_copter", 4));
			regions.add(atlas.findRegion("anim_bunny_copter", 5));
			animCopterRotate = new Animation(1.0f / 15.0f, regions);
		}
	}

	public class AssetBunnyPower
	{
		public final AtlasRegion head;

		public AssetBunnyPower(TextureAtlas atlas) 
		{
			head = atlas.findRegion("bunny_power");
		}
	}
	
	public class AssetEnemy 
	{
		public final AtlasRegion enemy;
		
		public AssetEnemy(TextureAtlas atlas) 
		{
			enemy = atlas.findRegion("enemy");
		}
	}
	
	public class AssetEnemyForward 
	{
		public final AtlasRegion enemy;
		
		public AssetEnemyForward(TextureAtlas atlas) 
		{
			enemy = atlas.findRegion("enemy-forward");
		}
	}
	
	public class AssetGiant
	{
		public final AtlasRegion giant;
		
		public AssetGiant(TextureAtlas atlas) 
		{
			giant = atlas.findRegion("giant");
		}
	}

	public class AssetRock 
	{
		public final AtlasRegion edge;
		public final AtlasRegion middle;

		public AssetRock(TextureAtlas atlas)
		{
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}

	public class AssetPlatform 
	{
		public final AtlasRegion platform;

		public AssetPlatform(TextureAtlas atlas)
		{
			platform = atlas.findRegion("platform");
		}
	}
	
	public class AssetMovingPlatform 
	{
		public final AtlasRegion platform;

		public AssetMovingPlatform(TextureAtlas atlas)
		{
			platform = atlas.findRegion("platform");
		}
	}
	
	public class AssetBox 
	{
		public final AtlasRegion box;

		public AssetBox(TextureAtlas atlas)
		{
			box = atlas.findRegion("box");
		}
	}
	
	public class AssetLaser 
	{
		public final AtlasRegion laser;

		public AssetLaser(TextureAtlas atlas)
		{
			laser = atlas.findRegion("laser");
		}
	}

	public class AssetGoldCoin 
	{
		public final AtlasRegion goldCoin;
		public final Animation animGoldCoin;

		public AssetGoldCoin(TextureAtlas atlas) 
		{
			goldCoin = atlas.findRegion("item_gold_coin");		
			// Animation: Gold Coin
			Array<AtlasRegion> regions = atlas.findRegions("anim_gold_coin");
			AtlasRegion region = regions.first();
			for (int i = 0; i < 10; i++)
				regions.insert(0, region);
			animGoldCoin = new Animation(1.0f / 20.0f, regions, Animation.LOOP_PINGPONG);
		}
	}

	public class AssetFeather 
	{
		public final AtlasRegion feather;

		public AssetFeather(TextureAtlas atlas)
		{
			feather = atlas.findRegion("PowerUp");
		}
	}

	public class AssetCheckpoint 
	{
		public final AtlasRegion checkpoint;

		public AssetCheckpoint(TextureAtlas atlas) 
		{
			checkpoint = atlas.findRegion("checkpoint");
		}
	}

	// Carrot
	public class AssetCarrot
	{
		public final AtlasRegion carrot;

		public AssetCarrot(TextureAtlas atlas)
		{
			carrot = atlas.findRegion("carrot");
		}
	}

	public class AssetLeftButton 
	{
		public final AtlasRegion left;

		public AssetLeftButton(TextureAtlas atlas) 
		{
			left = atlas.findRegion("boton_atras");
		}
	}

	public class AssetRightButton 
	{
		public final AtlasRegion right;

		public AssetRightButton(TextureAtlas atlas) 
		{
			right = atlas.findRegion("boton_adelante");
		}
	}

	public class AssetJumpButton 
	{
		public final AtlasRegion jump;

		public AssetJumpButton(TextureAtlas atlas) 
		{
			jump = atlas.findRegion("boton_jump");
		}
	}
	
	public class AssetPause
	{
		public final AtlasRegion pause;
		public final AtlasRegion title;
		public final AtlasRegion play;
		public final AtlasRegion restart;
		public final AtlasRegion selectLevel;
		public final AtlasRegion home;
		public final AtlasRegion sound;
		public final AtlasRegion slash;
		
		public AssetPause(TextureAtlas atlas)
		{
			pause = atlas.findRegion("background-pausa");
			pause.flip(false, true);
			title = atlas.findRegion("texto-pausa");
			title.flip(false, true);
			play = atlas.findRegion("boton-jugar");
			play.flip(false, true);
			restart = atlas.findRegion("boton-restart");
			restart.flip(false, true);
			selectLevel = atlas.findRegion("boton-seleccion-nivel");
			selectLevel.flip(false, true);
			home = atlas.findRegion("boton-menu-principal");
			home.flip(false, true);
			sound = atlas.findRegion("boton-sonido");
			sound.flip(false, true);
			slash = atlas.findRegion("silencio");
		}
	}

	public class AssetLevelDecoration 
	{
		/*public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;*/
		public final AtlasRegion cloud;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		public final AtlasRegion goal;

		public AssetLevelDecoration(TextureAtlas atlas) 
		{
			/*cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");*/
			cloud = atlas.findRegion("nube-de-polvo");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay = atlas.findRegion("water_overlay");
			goal = atlas.findRegion("goal");
		}
	}

	public class AssetFonts
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts() 
		{
			// create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			// set font sizes
			defaultSmall.setScale(0.75f);
			defaultNormal.setScale(1.0f);
			defaultBig.setScale(2.0f);
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	public class AssetSounds
	{
		public final Sound jump;
		public final Sound jumpWithFeather;
		public final Sound pickupCoin;
		public final Sound pickupFeather;
		public final Sound liveLost;

		public AssetSounds(AssetManager am) 
		{
			jump = am.get("sounds/jump.wav", Sound.class);
			jumpWithFeather = am.get("sounds/jump_with_feather.wav",
					Sound.class);
			pickupCoin = am.get("sounds/pickup_coin.wav", Sound.class);
			pickupFeather = am.get("sounds/pickup_feather.wav", Sound.class);
			liveLost = am.get("sounds/live_lost.wav", Sound.class);
		}
	}

	public class AssetMusic 
	{
		public final Music song01;
		//public final Music song02;

		public AssetMusic(AssetManager am) 
		{
			song01 = am.get("music/keith303_-_brand_new_highscore.mp3",
					Music.class);
			/*song02 = am.get("music/song02.mp3",
					Music.class);*/
		}
	}

}
