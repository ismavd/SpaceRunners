package com.me.mygdxgame.game;

import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.objects.Box;
import com.me.mygdxgame.objects.BunnyHead;
import com.me.mygdxgame.objects.BunnyHead.JUMP_STATE;
import com.me.mygdxgame.objects.Carrot;
import com.me.mygdxgame.objects.Checkpoint;
import com.me.mygdxgame.objects.Enemy;
import com.me.mygdxgame.objects.Feather;
import com.me.mygdxgame.objects.GoldCoin;
import com.me.mygdxgame.objects.MovingPlatform;
import com.me.mygdxgame.objects.Platform;
import com.me.mygdxgame.objects.Rock;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.me.mygdxgame.screens.GameScreen;
import com.me.mygdxgame.screens.MenuScreen;
import com.me.mygdxgame.utils.CameraHelper;
import com.me.mygdxgame.utils.Constants;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.me.mygdxgame.utils.AudioManager;

public class WorldController extends InputAdapter implements Disposable {
	private static final String TAG = WorldController.class.getName();

	private Game game;

	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private Rectangle r3 = new Rectangle();

	public Circle cLeft = new Circle();
	public Circle cRight = new Circle();
	public Circle cJump = new Circle();

	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int score;

	public boolean checkpointReached;
	private boolean paused;

	private float timeLeftGameOverDelay;

	private boolean goalReached;
	private boolean enemyHit;
	private boolean enemyHitEffectOn;
	private int hitStopCounter;

	private int nivel;
	private boolean finMundo = false;

	// public World b2world;

	public WorldController() {
		init();
	}

	public WorldController(Game game, int level) {
		this.game = game;
		this.nivel = level;
		init();
	}

	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		enemyHitEffectOn = false;
		initLevel();
		cLeft.set((float) 0.0625 * Gdx.graphics.getWidth(), (float) 0.9028
				* Gdx.graphics.getHeight(),
				(float) 0.058594 * Gdx.graphics.getWidth());
		cRight.set((float) 0.1796875 * Gdx.graphics.getWidth(), (float) 0.9028
				* Gdx.graphics.getHeight(),
				(float) 0.058594 * Gdx.graphics.getWidth());
		cJump.set((float) 0.9375 * Gdx.graphics.getWidth(), (float) 0.9028
				* Gdx.graphics.getHeight(),
				(float) 0.058594 * Gdx.graphics.getWidth());
	}

	private void initLevel() {
		score = 0;
		goalReached = false;
		enemyHit = false;
		level = new Level(getLevel(nivel), checkpointReached);
		cameraHelper.setTarget(level.bunnyHead);
		paused = false;
		// initPhysics();
	}

	private String getLevel(int level) {
		switch (level) {
		case 1:
			return Constants.LEVEL_01;
		case 2:
			return Constants.LEVEL_02;
		case 3:
			return Constants.LEVEL_03;
		case 4:
			return Constants.LEVEL_04;
		case 5:
			return Constants.LEVEL_05;
		case 6:
			return Constants.LEVEL_06;
		default:
			return null;
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		// Toggle camera follow
		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null
					: level.bunnyHead);
			Gdx.app.debug(TAG,
					"Camera follow enabled: " + cameraHelper.hasTarget());
		}
		// Back to Menu
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			backToMenu();
		}
		// Pause menu
		else if (keycode == Keys.MENU /* || keycode == Keys.ESCAPE */) {
			if (!paused) {
				game.getScreen().pause();
				paused = true;
			} else {
				game.getScreen().resume();
				paused = false;
			}
		}
		return false;
	}

	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		if (isGameOver() || goalReached) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0) {
				if (goalReached) {
					if (finMundo) {
						backToMenu();
					} else {
						nextLevel();
					}
				} else {
					backToMenu();
				}
			}
		} else if (enemyHitEffectOn) {
			timeLeftGameOverDelay -= deltaTime;
			level.bunnyHead.position.y -= 0.01f;
			hitStopCounter--;
			if (timeLeftGameOverDelay < 0) {
				enemyHitEffectOn = false;
				AudioManager.instance.play(Assets.instance.sounds.liveLost);
				lives--;
				if (isGameOver()) {
					timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
				} else {
					initLevel();
				}
			}
		} else {
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		// b2world.step(deltaTime, 8, 3);
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater() && !enemyHitEffectOn) {
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
			lives--;
			if (isGameOver()) {
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			} else {
				initLevel();
			}
		}
		if (enemyHit && !enemyHitEffectOn) {
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
			enemyHitEffectOn = true;
			timeLeftGameOverDelay = 1;
			level.bunnyHead.position.y += 0.25f;
			hitStopCounter = 10;
		}
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
	}

	private void nextLevel() {
		game.setScreen(new GameScreen(game, nivel + 1));
	}

	// En este m�todo vamos a poner las acciones a realizar en el men� de pausa
	public void updatePaused(float deltaTime) {
		if (Gdx.input.justTouched()) {
			backToMenu();
		}
	}

	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) {
			return;
		}

		if (!cameraHelper.hasTarget(level.bunnyHead)) {
			// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;

			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			}
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				moveCamera(-camMoveSpeed, 0);
			}
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				moveCamera(camMoveSpeed, 0);
			}
			if (Gdx.input.isKeyPressed(Keys.UP)) {
				moveCamera(0, camMoveSpeed);
			}
			if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				moveCamera(0, -camMoveSpeed);
			}
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
				cameraHelper.setPosition(0, 0);
			}
		}

		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;

		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		}
		if (Gdx.input.isKeyPressed(Keys.COMMA)) {
			cameraHelper.addZoom(camZoomSpeed);
		}
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) {
			cameraHelper.addZoom(-camZoomSpeed);
		}
		if (Gdx.input.isKeyPressed(Keys.SLASH)) {
			cameraHelper.setZoom(1);
		}
	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	private Pixmap createProceduralPixmap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);

		// Fill square with red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();

		// Draw a yellow-colored X shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);

		// Draw a cyan-colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}

	// Vuelve al men� principal.
	private void backToMenu() {
		game.setScreen(new MenuScreen(game));
	}

	private void onCollisionBunnyHeadWithRock(Rock rock) {
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y
				- (rock.position.y + rock.bounds.height));

		if (heightDifference > 0.25f) {
			boolean hitLeftEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitLeftEdge) {
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else {
				bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
			}
			return;
		}

		switch (bunnyHead.jumpState) {
		case GROUNDED:
			bunnyHead.jumpState = JUMP_STATE.JUMP_RISING;
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height
					+ bunnyHead.origin.y;
			if ((Gdx.input.isTouched(0)
					&& cJump.contains((float) Gdx.input.getX(0),
							(float) Gdx.input.getY(0)) || Gdx.input
					.isTouched(1)
					&& cJump.contains((float) Gdx.input.getX(1),
							(float) Gdx.input.getY(1)))
					|| !Gdx.input.isKeyPressed(Keys.SPACE)) {
				bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			}
			break;
		case JUMP_RISING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height
					+ bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		}
	}

	private void onCollisionBunnyHeadWithPlatform(Platform platform) {
		BunnyHead bunnyHead = level.bunnyHead;

		switch (bunnyHead.jumpState) {
		case GROUNDED:
			bunnyHead.jumpState = JUMP_STATE.JUMP_RISING;
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = platform.position.y + bunnyHead.origin.y;
			if ((Gdx.input.isTouched(0)
					&& cJump.contains((float) Gdx.input.getX(0),
							(float) Gdx.input.getY(0)) || Gdx.input
					.isTouched(1)
					&& cJump.contains((float) Gdx.input.getX(1),
							(float) Gdx.input.getY(1)))
					|| !Gdx.input.isKeyPressed(Keys.SPACE)) {
				bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			}
			break;
		case JUMP_RISING:
			bunnyHead.jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		}
	}

	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) {
		goldcoin.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
		score += goldcoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	}

	private void onCollisionBunnyWithFeather(Feather feather) {
		feather.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	}

	private void onCollisionBunnyWithCarrot(Carrot carrot) {
		carrot.collected = true;

		if (lives == 3) {
			score += carrot.getScore();
		} else {
			lives++;
		}

		Gdx.app.log(TAG, "Carrot collected");
	}

	private void onCollisionBunnyWithCheckpoint(Checkpoint checkpoint) {
		checkpoint.active = true;
		checkpointReached = true;
		Gdx.app.log(TAG, "Checkpoint reached");
	}

	private void onCollisionBunnyWithBox(Box box) {
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y
				- (box.position.y + box.bounds.height));

		if (heightDifference > 0.25f) {
			boolean hitLeftEdge = bunnyHead.position.x > (box.position.x + box.bounds.width / 2.0f);
			if (hitLeftEdge) {
				bunnyHead.position.x = box.position.x + box.bounds.width;
				if (isLeftPressed(0) || isLeftPressed(1)) {
					box.velocity.x = bunnyHead.velocity.x;
				} else {
					box.velocity.x = 0;
				}
			} else {
				bunnyHead.position.x = box.position.x - bunnyHead.bounds.width;
				if (isRightPressed(0) || isRightPressed(1)) {
					box.velocity.x = bunnyHead.velocity.x;
				} else {
					box.velocity.x = 0;
				}
			}
			return;
		}
		switch (bunnyHead.jumpState) {
		case GROUNDED:
			bunnyHead.jumpState = JUMP_STATE.JUMP_RISING;
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = box.position.y + bunnyHead.bounds.height / 2
					+ bunnyHead.origin.y;
			if ((Gdx.input.isTouched(0)
					&& cJump.contains((float) Gdx.input.getX(0),
							(float) Gdx.input.getY(0)) || Gdx.input
					.isTouched(1)
					&& cJump.contains((float) Gdx.input.getX(1),
							(float) Gdx.input.getY(1)))
					|| !Gdx.input.isKeyPressed(Keys.SPACE)) {
				bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			}
			break;
		case JUMP_RISING:
			bunnyHead.position.y = box.position.y + bunnyHead.bounds.height / 2
					+ bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.JUMP_FALLING;
			box.velocity.x = 0;
			break;
		}
	}

	/*
	 * private void onCollisionBoxWithBox(Box box1, Box box2) { float
	 * heightDifference = Math.abs(box1.position.y - (box2.position.y +
	 * box2.bounds.height)); if (heightDifference > 0.25f) { boolean hitLeftEdge
	 * = box1.position.x > (box2.position.x + box2.bounds.width / 2.0f); if
	 * (hitLeftEdge) { box1.position.x = box2.position.x + box2.bounds.width; if
	 * (isLeftPressed(0) || isLeftPressed(1)) box2.velocity.x = box1.velocity.x;
	 * else box2.velocity.x = 0; } else { box1.position.x = box2.position.x -
	 * box1.bounds.width; if (isRightPressed(0) || isRightPressed(1))
	 * box2.velocity.x = box1.velocity.x; else box2.velocity.x = 0; } return; }
	 * }
	 */

	private void onCollisionBunnyWithEnemy() {
		enemyHit = true;
	}

	private void onCollisionBunnyWithGoal() {
		goalReached = true;

		if (Constants.niveles.get(nivel + 1) == null) {
			finMundo = true;
		} else {
			finMundo = false;
		}

		timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
		Vector2 centerPosBunnyHead = new Vector2(level.bunnyHead.position);
		centerPosBunnyHead.x += level.bunnyHead.bounds.width;
	}

	private void testCollisions() {
		if (!enemyHitEffectOn) {
			r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y,
					level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);

			// Test collision: Bunny Head <-> Rocks
			for (Rock rock : level.rocks) {
				r2.set(rock.position.x, rock.position.y, rock.bounds.width,
						rock.bounds.height);
				if (!r1.overlaps(r2)) {
					continue;
				}

				onCollisionBunnyHeadWithRock(rock);
				// IMPORTANT: must do all collisions for valid
				// edge testing on rocks.
			}

			for (Platform platform : level.platforms) {
				r2.set(platform.position.x, platform.position.y,
						platform.bounds.width, platform.bounds.height);
				Rectangle r1Bottom = new Rectangle();
				// Rectangle r2Top = new Rectangle();
				r1Bottom.set(r1.x, r1.y, r1.width, 0.01f);
				// r2Top.set(r2.x,r2.y,r2.width,0.01f);

				if (!r1Bottom.overlaps(r2)) {
					// if (!r1.overlaps(r2))
					continue;
				}

				onCollisionBunnyHeadWithPlatform(platform);
				// IMPORTANT: must do all collisions for valid
				// edge testing on rocks.
			}
			
			for (MovingPlatform platform : level.movingPlatforms) {
				r2.set(platform.position.x, platform.position.y,
						platform.bounds.width, platform.bounds.height);
				Rectangle r1Bottom = new Rectangle();
				// Rectangle r2Top = new Rectangle();
				r1Bottom.set(r1.x, r1.y, r1.width, 0.01f);
				// r2Top.set(r2.x,r2.y,r2.width,0.01f);

				if (!r1Bottom.overlaps(r2)) {
					// if (!r1.overlaps(r2))
					continue;
				}

				onCollisionBunnyHeadWithPlatform(platform);
				// IMPORTANT: must do all collisions for valid
				// edge testing on rocks.
			}

			// Test collision: Bunny Head <-> Gold Coins
			for (GoldCoin goldcoin : level.goldcoins) {
				if (goldcoin.collected) {
					continue;
				}

				r2.set(goldcoin.position.x, goldcoin.position.y,
						goldcoin.bounds.width, goldcoin.bounds.height);

				if (!r1.overlaps(r2)) {
					continue;
				}
				onCollisionBunnyWithGoldCoin(goldcoin);
				break;
			}

			// Test collision: Bunny Head <-> Feathers
			for (Feather feather : level.feathers) {
				if (feather.collected) {
					continue;
				}

				r2.set(feather.position.x, feather.position.y,
						feather.bounds.width, feather.bounds.height);

				if (!r1.overlaps(r2)) {
					continue;
				}
				onCollisionBunnyWithFeather(feather);
				break;
			}

			// Test collision: Bunny Head <-> Carrots
			for (Carrot carrot : level.carrots) {
				if (carrot.collected) {
					continue;
				}

				r2.set(carrot.position.x, carrot.position.y,
						carrot.bounds.width, carrot.bounds.height);

				if (!r1.overlaps(r2)) {
					continue;
				}

				onCollisionBunnyWithCarrot(carrot);
				break;
			}

			// Test collision: Bunny Head <-> Checkpoint
			for (Checkpoint checkpoint : level.checkpoint) {
				if (checkpoint.active) {
					continue;
				}

				r2.set(checkpoint.position.x, checkpoint.position.y,
						checkpoint.bounds.width, checkpoint.bounds.height);

				if (!r1.overlaps(r2)) {
					continue;
				}

				onCollisionBunnyWithCheckpoint(checkpoint);
				break;
			}

			// Test collision: Bunny Head || Rock <-> Box
			for (Box box : level.boxes) {
				r2.set(box.position.x, box.position.y, box.bounds.width,
						box.bounds.height);
				if (r1.overlaps(r2)) {
					onCollisionBunnyWithBox(box);
				}

				/*
				 * for (Box box2 : level.boxes) { r2.set(box.position.x,
				 * box.position.y, box.bounds.width, box.bounds.height); if
				 * (r2.overlaps(r3)) onCollisionBoxWithBox(box,box2); }
				 */

				for (Rock rock : level.rocks) {
					r3.set(rock.position.x, rock.position.y, rock.bounds.width,
							rock.bounds.height);

					if (!r2.overlaps(r3)) {
						box.falling = true;
					} else {
						box.falling = false;
						break;
					}
				}
			}

			// Test collision: Bunny Head <-> Enemy
			for (Enemy enemy : level.enemies) {
				r2.set(enemy.position.x, enemy.position.y, enemy.bounds.width,
						enemy.bounds.height);

				if (!r1.overlaps(r2)) {
					continue;
				}

				onCollisionBunnyWithEnemy();
				break;
			}

			// Test collision: Bunny Head <-> Giant
			if (level.giant != null) {
				r2.set(level.giant.position.x, level.giant.position.y,
						level.giant.bounds.width, level.giant.bounds.height);

				if (goalReached) {
					level.giant.StopMoving();
				} else if (r1.overlaps(r2)) {
					onCollisionBunnyWithEnemy();
				}
			}

			// Test collision: Bunny Head <-> Goal
			if (!goalReached) {
				r2.set(level.goal.bounds);
				r2.x += level.goal.position.x;
				r2.y += level.goal.position.y;

				if (r1.overlaps(r2)) {
					onCollisionBunnyWithGoal();
				}
			}
		}
	}

	private void handleInputGame(float deltaTime) {
		if (cameraHelper.hasTarget(level.bunnyHead)) {
			for (int i = 0; i < 2; i++) {
				// Controlamos si el usuario pulsa alguno de los controles.
				if (isLeftPressed(i)) {
					level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
				} else if (isRightPressed(i)) {
					level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
				}

				// Si pulsa el bot�n de salto.
				if (isJumpPressed(i)) {
					level.bunnyHead.setJumping(true);
				}
			}
		} else {
			level.bunnyHead.setJumping(false);
		}
	}

	// Devuelve True si no quedan m�s vidas y False en caso contrario.
	public boolean isGameOver() {
		return lives < 0;
	}

	// Devuelve True si el personaje est� en el agua y False en caso contrario.
	public boolean isPlayerInWater() {
		return level.bunnyHead.position.y < -5;
	}

	public boolean isLeftPressed(int pointer) {
		if (Gdx.app.getType() == ApplicationType.Android
				|| Gdx.app.getType() == ApplicationType.iOS) {
			return Gdx.input.isTouched(pointer)
					&& cLeft.contains((float) Gdx.input.getX(pointer),
							(float) Gdx.input.getY(pointer));
		} else {
			return Gdx.input.isKeyPressed(Keys.LEFT);
		}
	}

	public boolean isRightPressed(int pointer) {
		if (Gdx.app.getType() == ApplicationType.Android
				|| Gdx.app.getType() == ApplicationType.iOS) {
			return Gdx.input.isTouched(pointer)
					&& cRight.contains((float) Gdx.input.getX(pointer),
							(float) Gdx.input.getY(pointer));
		} else {
			return Gdx.input.isKeyPressed(Keys.RIGHT);
		}
	}

	public boolean isJumpPressed(int pointer) {
		if (Gdx.app.getType() == ApplicationType.Android
				|| Gdx.app.getType() == ApplicationType.iOS) {
			return Gdx.input.isTouched(pointer)
					&& cJump.contains((float) Gdx.input.getX(pointer),
							(float) Gdx.input.getY(pointer));
		} else {
			return Gdx.input.isKeyPressed(Keys.SPACE);
		}
	}

	// Devuelve True si se ha alcanzado la meta y False en caso contrario.
	public boolean isGoalReached() {
		return goalReached;
	}

	// Devuelve True si el juego est� en pausa y False en caso contrario.
	public boolean isPaused() {
		return paused;
	}

	/*
	 * private void initPhysics() { if (b2world != null) b2world.dispose();
	 * b2world = new World(new Vector2(0, -9.81f), true); // Rocks Vector2
	 * origin = new Vector2(); for (Rock rock : level.rocks) { BodyDef bodyDef =
	 * new BodyDef(); bodyDef.type = BodyType.KinematicBody;
	 * bodyDef.position.set(rock.position); Body body =
	 * b2world.createBody(bodyDef); rock.body = body; PolygonShape polygonShape
	 * = new PolygonShape(); origin.x = rock.bounds.width / 2.0f; origin.y =
	 * rock.bounds.height / 2.0f; polygonShape.setAsBox(rock.bounds.width /
	 * 2.0f, rock.bounds.height / 2.0f, origin, 0); FixtureDef fixtureDef = new
	 * FixtureDef(); fixtureDef.shape = polygonShape;
	 * body.createFixture(fixtureDef); polygonShape.dispose(); } }
	 */

	@Override
	public void dispose() {
		/*
		 * if (b2world != null) b2world.dispose();
		 */
	}
}
