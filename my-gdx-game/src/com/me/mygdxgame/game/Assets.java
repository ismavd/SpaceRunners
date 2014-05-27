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
	public AssetAstronaut astronaut;
	public AssetLife life;
	
	// Enmigos
	public AssetEnemy enemy;
	public AssetEnemyForward enemyFwd;
	public AssetGiant giant;
	
	// Plataformas
	public AssetRock rock;
	public AssetPlatform platform;
	public AssetMovingPlatform movingPlatform;
	public AssetForwardPlatform fwdPlatform;
	public AssetFallingPlatform fallPlatform;
	public AssetBouncingPlatform bncPlatform;
	public AssetWall wall;
	
	// Cajas
	public AssetBox box;
	
	// Laser
	public AssetLaser laser;
	
	// Objetos
	public AssetPiece piece;
	public AssetFlyPower flyPower;
	public AssetCheckpoint checkpoint;
	public AssetExtraLife ExtraLife;
	public AssetGoal goal;
	
	public AssetLevelDecoration levelDecoration;
	public AssetFonts fonts;
	
	// Botones de control
	public AssetLeftButton leftButton;
	public AssetRightButton rightButton;
	public AssetJumpButton jumpButton;
	
	// Menú de pausa
	public AssetPause pause;
	
	// Sonido
	public AssetSounds sounds;
	public AssetMusic music;

	// Patrón Singleton
	private Assets() 
	{
		
	}

	public void init(AssetManager assetManager) 
	{
		this.assetManager = assetManager;
		
		assetManager.setErrorListener(this);
		
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_PAUSE, TextureAtlas.class);
		
		assetManager.load("sounds/jump.wav", Sound.class);
		assetManager.load("sounds/jump_with_feather.wav", Sound.class);
		assetManager.load("sounds/pickup_coin.wav", Sound.class);
		assetManager.load("sounds/pickup_feather.wav", Sound.class);
		assetManager.load("sounds/live_lost.wav", Sound.class);
		
		assetManager.load("music/Stellardrone - Billions And Billions.mp3", Music.class);
		assetManager.load("music/Stellardrone - Eclipse On The Moon.mp3", Music.class);
		
		
		assetManager.finishLoading();
		Gdx.app.debug(TAG,
				"# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureAtlas atlasPause = assetManager.get(Constants.TEXTURE_ATLAS_PAUSE);
		for (Texture t : atlasPause.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// Creación de los recursos de creación de objetos
		fonts = new AssetFonts();
		astronaut = new AssetAstronaut(atlas);
		life = new AssetLife(atlas);
		enemy = new AssetEnemy(atlas);
		enemyFwd = new AssetEnemyForward(atlas);
		giant = new AssetGiant(atlas);
		rock = new AssetRock(atlas);
		platform = new AssetPlatform(atlas);
		movingPlatform = new AssetMovingPlatform(atlas);
		fwdPlatform = new AssetForwardPlatform(atlas);
		fallPlatform = new AssetFallingPlatform(atlas);
		bncPlatform = new AssetBouncingPlatform(atlas);
		wall = new AssetWall(atlas);
		box = new AssetBox(atlas);
		laser = new AssetLaser(atlas);
		piece = new AssetPiece(atlas);
		flyPower = new AssetFlyPower(atlas);
		checkpoint = new AssetCheckpoint(atlas);
		
		ExtraLife = new AssetExtraLife(atlas);
		// Meta
		goal = new AssetGoal(atlas);
		// Botones
		leftButton = new AssetLeftButton(atlas);
		rightButton = new AssetRightButton(atlas);
		jumpButton = new AssetJumpButton(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		// Sonido
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
		// Menú de pausa
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

	public class AssetAstronaut 
	{
		public final AtlasRegion astronaut;
		public final Animation animStanding;
		public final Animation animMoving;
		public final Animation animJumping;
		public final Animation animFlying;

		public AssetAstronaut(TextureAtlas atlas) 
		{
			//astronaut = atlas.findRegion("anim_astronaut_normal");
			astronaut = atlas.findRegion("astronaut");
			Array<AtlasRegion> regions = null;
			//AtlasRegion region = astronaut;
			
			regions = atlas.findRegions("astronaut-stand");
			animStanding = new Animation(8.0f / 10.0f, regions,
			Animation.LOOP_PINGPONG);
			
			regions = atlas.findRegions("astronaut-moving");
			animMoving = new Animation(1.0f / 10.0f, regions,
			Animation.LOOP_PINGPONG);
			
			regions = atlas.findRegions("astronaut-jump");
			animJumping = new Animation(1.0f / 10.0f, regions);
			
			regions = atlas.findRegions("astronaut-jump");
			animFlying = new Animation(1.0f / 10.0f, regions);
		}
	}
	
	public class AssetLife
	{
		public final AtlasRegion life;

		public AssetLife(TextureAtlas atlas) 
		{
			life = atlas.findRegion("life");
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
		public final AtlasRegion rock;

		public AssetRock(TextureAtlas atlas)
		{
			rock = atlas.findRegion("rock");
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
	
	public class AssetForwardPlatform 
	{
		public final AtlasRegion platform;

		public AssetForwardPlatform(TextureAtlas atlas)
		{
			platform = atlas.findRegion("platform");
		}
	}
	
	public class AssetFallingPlatform 
	{
		public final AtlasRegion platform;

		public AssetFallingPlatform(TextureAtlas atlas)
		{
			platform = atlas.findRegion("platform");
		}
	}
	
	public class AssetBouncingPlatform
	{
		public final AtlasRegion bncPlatform;

		public AssetBouncingPlatform(TextureAtlas atlas)
		{
			bncPlatform = atlas.findRegion("platform");
		}
	}
	
	public class AssetWall
	{
		public final AtlasRegion wall;

		public AssetWall(TextureAtlas atlas)
		{
			wall = atlas.findRegion("wall");
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

	public class AssetPiece 
	{
		public final AtlasRegion pieceIcon;
		public final AtlasRegion piece1;
		public final AtlasRegion piece2;
		public final AtlasRegion piece3;
		public final AtlasRegion piece4;
		public final AtlasRegion piece5;
		public final AtlasRegion piece6;
		public final AtlasRegion piece7;
		//public final Animation animPiece;

		public AssetPiece(TextureAtlas atlas) 
		{
			pieceIcon = atlas.findRegion("piece-icon");
			piece1 = atlas.findRegion("piece1");		
			piece2 = atlas.findRegion("piece2");
			piece3 = atlas.findRegion("piece3");
			piece4 = atlas.findRegion("piece4");
			piece5 = atlas.findRegion("piece5");
			piece6 = atlas.findRegion("piece6");
			piece7 = atlas.findRegion("piece7");
		}
	}

	public class AssetFlyPower 
	{
		public final AtlasRegion bar;

		public AssetFlyPower(TextureAtlas atlas)
		{
			bar = atlas.findRegion("PowerUp");
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

	// Vida extra
	public class AssetExtraLife
	{
		public final AtlasRegion life;

		public AssetExtraLife(TextureAtlas atlas)
		{
			life = atlas.findRegion("life");
		}
	}
	
	// Meta: notar que hay una imagen distinta por nivel
	public class AssetGoal
	{
		public final AtlasRegion goal1;
		public final AtlasRegion goal2;
		public final AtlasRegion goal3;
		public final AtlasRegion goal4;
		public final AtlasRegion goal5;
		public final AtlasRegion goal6;

		public AssetGoal(TextureAtlas atlas)
		{
			goal1 = atlas.findRegion("goal1");
			goal2 = atlas.findRegion("goal2");
			goal3 = atlas.findRegion("goal3");
			goal4 = atlas.findRegion("goal4");
			goal5 = atlas.findRegion("goal5");
			goal6 = atlas.findRegion("goal6");
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
		public final AtlasRegion playDown;
		public final AtlasRegion restartDown;
		public final AtlasRegion selectLevelDown;
		public final AtlasRegion homeDown;
		public final AtlasRegion soundDown;
		
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
			playDown = atlas.findRegion("boton-jugar-dn");
			playDown.flip(false, true);
			restartDown = atlas.findRegion("boton-restart-dn");
			restartDown.flip(false, true);
			selectLevelDown = atlas.findRegion("boton-seleccion-nivel-dn");
			selectLevelDown.flip(false, true);
			homeDown = atlas.findRegion("boton-menu-principal-dn");
			homeDown.flip(false, true);
			soundDown = atlas.findRegion("boton-sonido-dn");
			soundDown.flip(false, true);
			slash = atlas.findRegion("silencio");
		}
	}

	public class AssetLevelDecoration 
	{
		public final AtlasRegion cloud;
		public final AtlasRegion mountain1;
		public final AtlasRegion mountain2;
		public final AtlasRegion mountain3;
		
		public final AtlasRegion poisonOverlay;

		public AssetLevelDecoration(TextureAtlas atlas) 
		{
			cloud = atlas.findRegion("nube-de-polvo");
			mountain1 = atlas.findRegion("mountains1");
			mountain2 = atlas.findRegion("mountains2");
			mountain3 = atlas.findRegion("mountains3");
			poisonOverlay = atlas.findRegion("poison_overlay");
			
		}
	}

	public class AssetFonts
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts() 
		{
			defaultSmall = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			
			defaultSmall.setScale(0.75f);
			defaultNormal.setScale(1.0f);
			defaultBig.setScale(2.0f);
			
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
		public final Sound jumpWithFlyPower;
		public final Sound pickupCoin;
		public final Sound pickupFlyPower;
		public final Sound liveLost;

		public AssetSounds(AssetManager am) 
		{
			jump = am.get("sounds/jump.wav", Sound.class);
			jumpWithFlyPower = am.get("sounds/jump_with_feather.wav",
					Sound.class);
			pickupCoin = am.get("sounds/pickup_coin.wav", Sound.class);
			pickupFlyPower = am.get("sounds/pickup_feather.wav", Sound.class);
			liveLost = am.get("sounds/live_lost.wav", Sound.class);
		}
	}

	public class AssetMusic 
	{
		public final Music song01;
		public final Music song02;

		public AssetMusic(AssetManager am) 
		{
			song01 = am.get("music/Stellardrone - Billions And Billions.mp3",
					Music.class);
			song02 = am.get("music/Stellardrone - Eclipse On The Moon.mp3",
					Music.class);
		}
	}

}
