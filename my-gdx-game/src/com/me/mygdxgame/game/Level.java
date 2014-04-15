package com.me.mygdxgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.me.mygdxgame.objects.AbstractGameObject;
import com.me.mygdxgame.objects.Checkpoint;
import com.me.mygdxgame.objects.Clouds;
import com.me.mygdxgame.objects.Enemy;
import com.me.mygdxgame.objects.Giant;
import com.me.mygdxgame.objects.Mountains;
import com.me.mygdxgame.objects.Platform;
import com.me.mygdxgame.objects.Rock;
import com.me.mygdxgame.objects.WaterOverlay;
import com.me.mygdxgame.objects.BunnyHead;
import com.me.mygdxgame.objects.GoldCoin;
import com.me.mygdxgame.objects.Feather;
import com.me.mygdxgame.objects.Carrot;
import com.me.mygdxgame.objects.Goal;
import com.me.mygdxgame.utils.AudioManager;

public class Level {
	public static final String TAG = Level.class.getName();

	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), // black
		ROCK(0, 255, 0), // green
		PLATFORM(128, 128, 128), // grey
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		ITEM_FEATHER(255, 0, 255), // purple
		ITEM_GOLD_COIN(255, 255, 0), // yellow
		GOAL(255, 0, 0), // red
		ITEM_CARROT(0, 0, 255), // blue
		CHECKPOINT(255, 69, 0), // orange
		ENEMY(63,72,204), // indigo
		GIANT(128,64,0); // brown

		private int color;

		private BLOCK_TYPE(int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}

		public boolean sameColor(int color) {
			return this.color == color;
		}

		public int getColor() {
			return color;
		}
	}

	// objects
	public Array<Rock> rocks;
	public Array<Platform> platforms;
	// decoration
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;
	// actors
	public BunnyHead bunnyHead;
	public Array<Enemy> enemies;
	public Giant giant;
	// items
	public Array<GoldCoin> goldcoins;
	public Array<Feather> feathers;
	public Array<Carrot> carrots;
	public Array<Checkpoint> checkpoint;
	// goal
	public Goal goal;

	public Level(String filename, boolean checkpointReached) {
		init(filename, checkpointReached);
		//AudioManager.instance.play(Assets.instance.music.song02);
	}

	private void init(String filename, boolean checkpointReached) {
		// player character
		bunnyHead = null;
		// enemies
		enemies = new Array<Enemy>();
		giant = null;
		// objects
		rocks = new Array<Rock>();
		platforms = new Array<Platform>();
		goldcoins = new Array<GoldCoin>();
		feathers = new Array<Feather>();
		carrots = new Array<Carrot>();
		checkpoint = new Array<Checkpoint>();
		// load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		// scan pixels from top-left to bottom-right
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				// height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;
				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				// find matching color value to identify block type at (x,y)
				// point and create the corresponding game object if there is
				// a match
				// empty space
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					// do nothing
				}
				// rock
				else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
					if (lastPixel != currentPixel) {
						obj = new Rock();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y
								 * heightIncreaseFactor + offsetHeight);
						rocks.add((Rock) obj);
					} else {
						rocks.get(rocks.size - 1).increaseLength(1);
					}
				}
				else if (BLOCK_TYPE.PLATFORM.sameColor(currentPixel)) {
					if (lastPixel != currentPixel) {
						obj = new Platform();
						offsetHeight = -1.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);						
						platforms.add((Platform) obj);
					} else {
						platforms.get(platforms.size - 1).increaseLength(1);
					}
				}
				// player spawn point
				else if (!checkpointReached
						&& BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
					obj = new BunnyHead();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					bunnyHead = (BunnyHead) obj;
				}
				// feather
				else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {
					obj = new Feather();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					feathers.add((Feather) obj);
				}
				// gold coin
				else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
					obj = new GoldCoin();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					goldcoins.add((GoldCoin) obj);
				}
				// carrot
				else if (BLOCK_TYPE.ITEM_CARROT.sameColor(currentPixel)) {
					obj = new Carrot();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					carrots.add((Carrot) obj);
				}
				// checkpoint
				else if (BLOCK_TYPE.CHECKPOINT.sameColor(currentPixel)) {
					obj = new Checkpoint();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					checkpoint.add((Checkpoint) obj);
					if (checkpointReached) {
						obj = new BunnyHead();
						offsetHeight = -3.0f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y
								+ offsetHeight);
						bunnyHead = (BunnyHead) obj;
					}
				}
				// enemies
				else if (BLOCK_TYPE.ENEMY.sameColor(currentPixel)) {
					obj = new Enemy();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					((Enemy)obj).initMove(obj.position.y);
					enemies.add((Enemy) obj);
				}
				// giant
				else if (BLOCK_TYPE.GIANT.sameColor(currentPixel)) {
					obj = new Giant();
					offsetHeight = -9f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					giant = (Giant) obj;
				}
				// goal
				else if (BLOCK_TYPE.GOAL.sameColor(currentPixel)) {
					obj = new Goal();
					offsetHeight = -7.0f;
					obj.position.set(pixelX, baseHeight + offsetHeight);
					goal = (Goal) obj;
				}
				// unknown object/pixel color
				else {
					int r = 0xff & (currentPixel >>> 24); // red color channel
					int g = 0xff & (currentPixel >>> 16); // green color channel
					int b = 0xff & (currentPixel >>> 8); // blue color channel
					int a = 0xff & currentPixel; // alpha channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
							+ pixelY + ">: r<" + r + "> g<" + g + "> b<" + b
							+ "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}
		// decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1, -1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0, -3.75f);
		// free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}

	public void render(SpriteBatch batch) {
		// Draw Mountains
		mountains.render(batch);
		// Draw Goal
		goal.render(batch);
		// Draw Rocks
		for (Rock rock : rocks)
			rock.render(batch);
		// Draw Platforms
		for (Platform platform : platforms)
			platform.render(batch);
		// Draw Gold Coins
		for (GoldCoin goldCoin : goldcoins)
			goldCoin.render(batch);
		// Draw Feathers
		for (Feather feather : feathers)
			feather.render(batch);
		// Draw Carrots
		for (Carrot carrot : carrots)
			carrot.render(batch);
		// Draw Enemies
		for (Enemy enemy : enemies)
			enemy.render(batch);
		// Draw Giant if he is in the level
		if (giant!=null)
			giant.render(batch);
		// Draw Checkpoints
		for (Checkpoint cp : checkpoint)
			cp.render(batch);
		// Draw Player Character
		bunnyHead.render(batch);
		// Draw Water Overlay
		waterOverlay.render(batch);
		// Draw Clouds
		clouds.render(batch);
	}

	public void update(float deltaTime) {
		bunnyHead.update(deltaTime);
		for (Rock rock : rocks)
			rock.update(deltaTime);
		for (Platform platform : platforms)
			platform.update(deltaTime);
		for (GoldCoin goldCoin : goldcoins)
			goldCoin.update(deltaTime);
		for (Feather feather : feathers)
			feather.update(deltaTime);
		for (Carrot carrot : carrots)
			carrot.update(deltaTime);
		for (Checkpoint cp : checkpoint)
			cp.update(deltaTime);
		for (Enemy enemy : enemies)
			enemy.update(deltaTime);
		if (giant != null)
			giant.update(deltaTime);
		clouds.update(deltaTime);
	}
}
