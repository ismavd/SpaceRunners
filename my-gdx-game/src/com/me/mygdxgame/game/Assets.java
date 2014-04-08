package com.me.mygdxgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.me.mygdxgame.utils.Constants;

public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	public AssetBunny bunny;
	public AssetBunnyPower bunnyPower;
	public AssetRock rock;
	public AssetGoldCoin goldCoin;
	public AssetFeather feather;
	public AssetCheckpoint checkpoint;
	// Carrot
	public AssetCarrot carrot;
	public AssetLevelDecoration levelDecoration;
	public AssetFonts fonts;
	// Input buttons
	public AssetLeftButton leftButton;
	public AssetRightButton rightButton;
	public AssetJumpButton jumpButton;

	// singleton: prevent instantiation from other classes
	private Assets() {
	}

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
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
		// create game resource objects
		fonts = new AssetFonts();
		bunny = new AssetBunny(atlas);
		bunnyPower = new AssetBunnyPower(atlas);
		rock = new AssetRock(atlas);
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
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'",
				(Exception) throwable);
	}

	/*
	 * @Override public void error(String filename, Class type, Throwable
	 * throwable) { Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'",
	 * (Exception) throwable); }
	 */

	public class AssetBunny {
		public final AtlasRegion head;

		public AssetBunny(TextureAtlas atlas) {
			head = atlas.findRegion("bunny_head");
		}
	}
	
	public class AssetBunnyPower {
		public final AtlasRegion head;

		public AssetBunnyPower(TextureAtlas atlas) {
			head = atlas.findRegion("bunny_power");
		}
	}

	public class AssetRock {
		public final AtlasRegion edge;
		public final AtlasRegion middle;

		public AssetRock(TextureAtlas atlas) {
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}

	public class AssetGoldCoin {
		public final AtlasRegion goldCoin;

		public AssetGoldCoin(TextureAtlas atlas) {
			goldCoin = atlas.findRegion("item_gold_coin");
		}
	}

	public class AssetFeather {
		public final AtlasRegion feather;

		public AssetFeather(TextureAtlas atlas) {
			feather = atlas.findRegion("item_feather");
		}
	}
	
	public class AssetCheckpoint {
		public final AtlasRegion checkpoint;

		public AssetCheckpoint(TextureAtlas atlas) {
			checkpoint = atlas.findRegion("checkpoint");
		}
	}
	
	// Carrot
	public class AssetCarrot {
		public final AtlasRegion carrot;

		public AssetCarrot(TextureAtlas atlas) {
			carrot = atlas.findRegion("carrot");
		}
	}
	
	public class AssetLeftButton {
		public final AtlasRegion left;

		public AssetLeftButton(TextureAtlas atlas) {
			left = atlas.findRegion("boton_atras");
		}
	}
	
	public class AssetRightButton {
		public final AtlasRegion right;

		public AssetRightButton(TextureAtlas atlas) {
			right = atlas.findRegion("boton_adelante");
		}
	}
	
	public class AssetJumpButton {
		public final AtlasRegion jump;

		public AssetJumpButton(TextureAtlas atlas) {
			jump = atlas.findRegion("boton_jump");
		}
	}

	public class AssetLevelDecoration {
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		public final AtlasRegion goal;

		public AssetLevelDecoration(TextureAtlas atlas) {
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay = atlas.findRegion("water_overlay");
			goal = atlas.findRegion("goal");
		}
	}

	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts() {
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

}
