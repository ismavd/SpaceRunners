package com.me.mygdxgame.game;

import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.objects.Box;
import com.me.mygdxgame.objects.Astronaut;
import com.me.mygdxgame.objects.Astronaut.JUMP_STATE;
import com.me.mygdxgame.objects.Astronaut.VIEW_DIRECTION;
import com.me.mygdxgame.objects.ExtraLife;
import com.me.mygdxgame.objects.Checkpoint;
import com.me.mygdxgame.objects.Enemy;
import com.me.mygdxgame.objects.EnemyForward;
import com.me.mygdxgame.objects.FallingPlatform;
import com.me.mygdxgame.objects.FlyPower;
import com.me.mygdxgame.objects.ForwardPlatform;
import com.me.mygdxgame.objects.BouncingPlatform;
import com.me.mygdxgame.objects.Giant;
import com.me.mygdxgame.objects.Piece;
import com.me.mygdxgame.objects.MovingPlatform;
import com.me.mygdxgame.objects.Platform;
import com.me.mygdxgame.objects.Rock;
import com.me.mygdxgame.objects.Wall;
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
import com.me.mygdxgame.screens.LevelScreen;
import com.me.mygdxgame.screens.MenuScreen;
import com.me.mygdxgame.screens.DirectedGame;
import com.me.mygdxgame.screens.ScreenTransition;
import com.me.mygdxgame.screens.ScreenTransitionSlice;
import com.me.mygdxgame.utils.CameraHelper;
import com.me.mygdxgame.utils.Constants;
import com.me.mygdxgame.utils.GamePreferences;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
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

	private DirectedGame game;

	// Rectángulos de detección de colisiones entre objetos
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private Rectangle r3 = new Rectangle();

	// Botones para los controles en la versión para móvil
	public Circle cLeft = new Circle();
	public Circle cRight = new Circle();
	public Circle cJump = new Circle();
	public Circle cShoot = new Circle();

	// Botones del menú de pausa
	public Circle cPlay = new Circle();
	public Circle cRestart = new Circle();
	public Circle cLevels = new Circle();
	public Circle cHome = new Circle();
	public Circle cSound = new Circle();

	public CameraHelper cameraHelper;
	public Level level;
	public int lives;

	public int pieces;
	public int piecesNeeded;
	public int piecesSave;

	public int score;
	public int lastScore;
	public int time;
	private int startTime;

	public boolean checkpointReached;

	private boolean paused;

	private float timeLeftGameOverDelay;

	private boolean goalReached;
	private boolean enemyHit;
	private boolean enemyHitEffectOn;
	private int hitStopCounter;
	private boolean soundButtonTouched;

	private int nivel;
	private boolean finMundo = false;

	public WorldController(DirectedGame game) {
		this.game = game;
		init();
	}

	public WorldController(DirectedGame game, int level, int pieces, int score,
			int time) {
		this.game = game;
		this.nivel = level;
		this.score = score;
		this.lastScore = score;
		this.piecesNeeded = pieces;
		this.startTime = time * 60;
		init();
	}

	private void init() {
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		enemyHitEffectOn = false;
		soundButtonTouched = false;
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
		cShoot.set((float) 0.8203125 * Gdx.graphics.getWidth(), (float) 0.9028
				* Gdx.graphics.getHeight(),
				(float) 0.058594 * Gdx.graphics.getWidth());

		// Dimensiones de la pantalla en versión Desktop: Width = 1280; Height =
		// 720
		// Botones del menú de pausa
		cPlay.set((float) 0.1515625 * Gdx.graphics.getWidth(),
				(float) 0.2833333333333333 * Gdx.graphics.getHeight(),
				(float) 0.058594 * Gdx.graphics.getWidth());
		cRestart.set((float) 0.1515625 * Gdx.graphics.getWidth(),
				(float) 0.5041666666666667 * Gdx.graphics.getHeight(),
				(float) 0.058594 * Gdx.graphics.getWidth());
		cLevels.set((float) 0.1515625 * Gdx.graphics.getWidth(),
				(float) 0.6944444444444444 * Gdx.graphics.getHeight(),
				(float) 0.058594 * Gdx.graphics.getWidth());
		cHome.set((float) 0.078125 * Gdx.graphics.getWidth(),
				(float) 0.9097222222222222 * Gdx.graphics.getHeight(),
				(float) 0.058594 * Gdx.graphics.getWidth());
		cSound.set((float) 0.24609375 * Gdx.graphics.getWidth(),
				(float) 0.9097222222222222 * Gdx.graphics.getHeight(),
				(float) 0.058594 * Gdx.graphics.getWidth());
	}

	private void initLevel() {
		pieces = 0;
		score = lastScore;
		goalReached = false;
		enemyHit = false;
		level = new Level(getLevel(nivel), checkpointReached, nivel);
		cameraHelper.setTarget(level.astronaut);
		paused = false;
		time = startTime;
		if (checkpointReached)
			pieces = piecesSave;
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
		// Reseteo del mundo
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		// Fijar cámara en el personaje jugador
		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null
					: level.astronaut);
			Gdx.app.debug(TAG,
					"Camera follow enabled: " + cameraHelper.hasTarget());
		}
		// Reinicio del nivel
		else if (keycode == Keys.BACKSPACE) {
			initLevel();
		}
		// Menú de pausa
		else if (keycode == Keys.MENU || keycode == Keys.BACK
				|| keycode == Keys.ESCAPE) {
			if (!paused) {
				game.getScreen().pause();
				moveCamera(-1f, 0);
				paused = true;
			} else {
				game.getScreen().resume();
				moveCamera(1f, 0);
				paused = false;
			}
		}
		return false;
	}

	public void update(float deltaTime) {
		if (time >= 0)
			time--;
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
		} else if (enemyHitEffectOn || time == 0) {
			timeLeftGameOverDelay -= deltaTime;
			level.astronaut.position.y -= 0.01f;
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
			level.astronaut.position.y += 0.25f;
			hitStopCounter = 10;
		}
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
		if (level.giant != null)
			level.giant.position.y = level.astronaut.position.y;
	}

	private void nextLevel() {
		ScreenTransition transition = ScreenTransitionSlice.init(2,
				ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
		if (nivel != 5)
			game.setScreen(new GameScreen(game, nivel + 1, 20, score, 151),
					transition);
		else
			game.setScreen(new GameScreen(game, nivel + 1, 20, score, -1),
					transition);
	}

	// En este método vamos a poner las acciones a realizar en el menú de pausa
	public void updatePaused(float deltaTime) {
		if (!soundButtonTouched) {
			// Continuar
			if (Gdx.input.isTouched()
					&& cPlay.contains((float) Gdx.input.getX(),
							(float) Gdx.input.getY())) {
				game.getScreen().resume();
				moveCamera(1.5f, 0);
				paused = false;
			}
			// Reiniciar
			if (Gdx.input.isTouched()
					&& cRestart.contains((float) Gdx.input.getX(),
							(float) Gdx.input.getY())) {
				game.getScreen().resume();
				moveCamera(1.5f, 0);
				paused = false;
				initLevel();
			}
			// Selección de nivel
			if (Gdx.input.isTouched()
					&& cLevels.contains((float) Gdx.input.getX(),
							(float) Gdx.input.getY())) {
				game.getScreen().resume();
				paused = false;
				game.setScreen(new LevelScreen(game));
			}
			// Menu principal
			if (Gdx.input.isTouched()
					&& cHome.contains((float) Gdx.input.getX(),
							(float) Gdx.input.getY())) {
				game.getScreen().resume();
				paused = false;
				backToMenu();
			}
			// Sonido
			if (Gdx.input.isTouched()
					&& cSound.contains((float) Gdx.input.getX(),
							(float) Gdx.input.getY())) {
				soundButtonTouched = true;
				GamePreferences prefs = GamePreferences.instance;
				if (!prefs.sound && !prefs.music) {
					prefs.sound = true;
					prefs.music = true;
				} else {
					prefs.sound = false;
					prefs.music = false;
				}
				prefs.save();
				AudioManager.instance.onSettingsUpdated();
			}
		} else {
			soundButtonTouched = Gdx.input.isTouched()
					&& cSound.contains((float) Gdx.input.getX(),
							(float) Gdx.input.getY());
		}
	}

	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) {
			return;
		}

		if (!cameraHelper.hasTarget(level.astronaut)) {
			// Movimiento manual de cámara en versión de escritorio
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

		// Zoom
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

	// Vuelve al menú principal.
	private void backToMenu() {
		game.setScreen(new MenuScreen(game));
	}

	private void onCollisionAstronautWithRock(Rock rock) {
		Astronaut astronaut = level.astronaut;
		float heightDifference = Math.abs(astronaut.position.y
				- (rock.position.y + rock.bounds.height));

		if (heightDifference > 0.25f) {
			boolean hitLeftEdge = astronaut.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitLeftEdge) {
				astronaut.position.x = rock.position.x + rock.bounds.width;
			} else {
				astronaut.position.x = rock.position.x - astronaut.bounds.width;
			}
			return;
		}

		switch (astronaut.jumpState) {
		case GROUNDED:
			astronaut.jumpState = JUMP_STATE.JUMP_RISING;
			break;
		case FALLING:
		case JUMP_FALLING:
			astronaut.position.y = rock.position.y + astronaut.bounds.height
					+ astronaut.origin.y;
			if ((Gdx.input.isTouched(0)
					&& cJump.contains((float) Gdx.input.getX(0),
							(float) Gdx.input.getY(0)) || Gdx.input
					.isTouched(1)
					&& cJump.contains((float) Gdx.input.getX(1),
							(float) Gdx.input.getY(1)))
					|| !Gdx.input.isKeyPressed(Keys.SPACE)) {
				astronaut.jumpState = JUMP_STATE.GROUNDED;
			}
			break;
		case JUMP_RISING:
			astronaut.position.y = rock.position.y + astronaut.bounds.height
					+ astronaut.origin.y;
			astronaut.jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		}
	}

	private void onCollisionAstronautWithPlatform(Platform platform) {
		Astronaut astronaut = level.astronaut;

		switch (astronaut.jumpState) {
		case GROUNDED:
			astronaut.jumpState = JUMP_STATE.JUMP_RISING;
			break;
		case FALLING:
		case JUMP_FALLING:
			astronaut.position.y = platform.position.y + astronaut.origin.y;
			if (!((Gdx.input.isTouched(0) && cJump.contains(
					(float) Gdx.input.getX(0), (float) Gdx.input.getY(0)))
					|| Gdx.input.isTouched(1)
					&& cJump.contains((float) Gdx.input.getX(1),
							(float) Gdx.input.getY(1)) || Gdx.input
						.isKeyPressed(Keys.SPACE))) {
				astronaut.jumpState = JUMP_STATE.GROUNDED;
			}
			break;
		case JUMP_RISING:
			astronaut.jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		}
	}

	private void onCollisionAstronautWithForwardPlatform(Platform platform) {
		Astronaut astronaut = level.astronaut;
		switch (astronaut.jumpState) {
		case GROUNDED:
			astronaut.jumpState = JUMP_STATE.JUMP_RISING;
			break;
		case FALLING:
		case JUMP_FALLING:
			astronaut.position.y = platform.position.y + astronaut.origin.y;
			if ((Gdx.input.isTouched(0)
					&& cJump.contains((float) Gdx.input.getX(0),
							(float) Gdx.input.getY(0)) || Gdx.input
					.isTouched(1)
					&& cJump.contains((float) Gdx.input.getX(1),
							(float) Gdx.input.getY(1)))
					|| Gdx.input.isKeyPressed(Keys.SPACE)) {
				((ForwardPlatform) platform).playerPosition = astronaut.position.x
						- platform.position.x;
			} else {
				if (astronaut.jumpState == JUMP_STATE.GROUNDED)
					((ForwardPlatform) platform).playerPosition = astronaut.position.x
							- platform.position.x;
				astronaut.jumpState = JUMP_STATE.GROUNDED;
			}

			if (isLeftPressed(0) || isLeftPressed(1)) {
				((ForwardPlatform) platform).playerPosition = astronaut.position.x
						- platform.position.x;
				astronaut.velocity.x = -level.astronaut.terminalVelocity.x
						- platform.velocity.x;
			} else if (isRightPressed(0) || isRightPressed(1)) {
				((ForwardPlatform) platform).playerPosition = astronaut.position.x
						- platform.position.x;
				astronaut.velocity.x = level.astronaut.terminalVelocity.x
						+ platform.velocity.x;
			} else {
				astronaut.dustOn = false;
				astronaut.viewDirectionOn = false;
				astronaut.velocity.x = platform.velocity.x;
			}
			break;
		case JUMP_RISING:
			astronaut.jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		}
	}

	private void onCollisionAstronautWithBouncingPlatform(
			BouncingPlatform bouncingPlatform) {
		if (!bouncingPlatform.active)
			bouncingPlatform.active = true;
		else
			level.astronaut.velocity.y = 10;
	}

	private void onCollisionAstronautWithWall(Wall wall) {
		Astronaut astronaut = level.astronaut;
		float heightDifference = Math.abs(astronaut.position.y
				- (wall.position.y + wall.bounds.height));

		if (heightDifference > 0.25f) {
			boolean hitLeftEdge = astronaut.position.x > (wall.position.x + wall.bounds.width / 2.0f);
			if (hitLeftEdge) {
				astronaut.position.x = wall.position.x + wall.bounds.width;
			} else {
				astronaut.position.x = wall.position.x - astronaut.bounds.width;
			}
			if (Gdx.input.isTouched(0)
					&& cJump.contains((float) Gdx.input.getX(0),
							(float) Gdx.input.getY(0))
					|| Gdx.input.isTouched(1)
					&& cJump.contains((float) Gdx.input.getX(1),
							(float) Gdx.input.getY(1))
					|| Gdx.input.isKeyPressed(Keys.SPACE)) {
			}
			return;
		}

		switch (astronaut.jumpState) {
		case GROUNDED:
			astronaut.jumpState = JUMP_STATE.JUMP_RISING;
			break;
		case FALLING:
		case JUMP_FALLING:
			astronaut.position.y = wall.position.y + astronaut.bounds.height
					+ astronaut.origin.y;
			if ((Gdx.input.isTouched(0)
					&& cJump.contains((float) Gdx.input.getX(0),
							(float) Gdx.input.getY(0)) || Gdx.input
					.isTouched(1)
					&& cJump.contains((float) Gdx.input.getX(1),
							(float) Gdx.input.getY(1)))
					|| !Gdx.input.isKeyPressed(Keys.SPACE)) {
				astronaut.jumpState = JUMP_STATE.GROUNDED;
			}
			break;
		case JUMP_RISING:
			astronaut.position.y = wall.position.y + astronaut.bounds.height
					+ astronaut.origin.y;
			astronaut.jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		}
	}

	private void onCollisionAstronautWithPiece(Piece piece) {
		piece.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
		pieces += 1;
		score += piece.getScore();
		Gdx.app.log(TAG, "Piece of the space ship collected");
	}

	private void onCollisionAstronautWithFeather(FlyPower flyPower) {
		flyPower.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFlyPower);
		score += flyPower.getScore();
		level.astronaut.setBarPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	}

	private void onCollisionAstronautWithExtraLife(ExtraLife extraLife) {
		extraLife.collected = true;

		if (lives == 3) {
			score += extraLife.getScore();
		} else {
			lives++;
		}

		Gdx.app.log(TAG, "Carrot collected");
	}

	private void onCollisionAstronautWithCheckpoint(Checkpoint checkpoint) {
		if (!checkpoint.active)
			piecesSave = pieces;
		checkpoint.active = true;
		checkpointReached = true;
		if (time > 75) {
			startTime = time;
		} else
			startTime = 75;
		Gdx.app.log(TAG, "Checkpoint reached");
	}

	private void onCollisionAstronautWithBox(Box box) {
		Astronaut astronaut = level.astronaut;
		float heightDifference = Math.abs(astronaut.position.y
				- (box.position.y + box.bounds.height));
		if (heightDifference > 0.25f) {
			boolean hitLeftEdge = astronaut.position.x > (box.position.x + box.bounds.width / 2.0f);
			if (hitLeftEdge) {
				astronaut.position.x = box.position.x + box.bounds.width;
				if ((isLeftPressed(0) || isLeftPressed(1))
						&& astronaut.jumpState == JUMP_STATE.GROUNDED) {
					box.velocity.x = astronaut.velocity.x;
				} else {
					box.velocity.x = 0;
				}
			} else {
				astronaut.position.x = box.position.x - astronaut.bounds.width;
				if ((isRightPressed(0) || isRightPressed(1))
						&& astronaut.jumpState == JUMP_STATE.GROUNDED) {
					box.velocity.x = astronaut.velocity.x;
				} else {
					box.velocity.x = 0;
				}
			}
			return;
		}
		switch (astronaut.jumpState) {
		case GROUNDED:
			astronaut.jumpState = JUMP_STATE.JUMP_RISING;
			break;
		case FALLING:
		case JUMP_FALLING:
			astronaut.position.y = box.position.y + astronaut.bounds.height / 2
					+ astronaut.origin.y;
			if (isJumpPressed(0) || isJumpPressed(1)) {
				astronaut.jumpState = JUMP_STATE.GROUNDED;
			}
			break;
		case JUMP_RISING:
			astronaut.position.y = box.position.y + astronaut.bounds.height / 2
					+ astronaut.origin.y;
			astronaut.jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		}
	}

	private void onCollisionAstronautWithEnemy() {
		enemyHit = true;
	}

	private void onCollisionAstronautWithGoal() {
		if (pieces >= piecesNeeded)
			goalReached = true;

		if (Constants.niveles.get(nivel + 1) == null) {
			finMundo = true;
		} else {
			finMundo = false;
		}

		timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
		Vector2 centerPosBunnyHead = new Vector2(level.astronaut.position);
		centerPosBunnyHead.x += level.astronaut.bounds.width;
	}

	private void onCollisionLaserWithEnemy(Enemy enemy) {
		if (enemy.alive) {
			score += enemy.getScore();
			level.astronaut.shooting = false;
			enemy.alive = false;
			enemy.dying = true;
		}
	}

	private void onCollisionLaserWithEnemy(EnemyForward enemy) {
		if (enemy.alive) {
			score += enemy.getScore();
			level.astronaut.shooting = false;
			enemy.alive = false;
			enemy.dying = true;
		}
	}

	private void onCollisionLaserWithGiant(Giant giant) {
		/*
		 * level.bunnyHead.shooting = false; if (giant.hp <= 0) score +=
		 * giant.getScore(); else giant.hp--;
		 */
	}

	private void testCollisions() {
		if (!enemyHitEffectOn) {
			r1.set(level.astronaut.position.x, level.astronaut.position.y,
					level.astronaut.bounds.width, level.astronaut.bounds.height);

			// Colisión Astronauta <-> Suelo
			for (Rock rock : level.rocks) {
				r2.set(rock.position.x, rock.position.y, rock.bounds.width,
						rock.bounds.height);
				if (!r1.overlaps(r2)) {
					continue;
				}
				onCollisionAstronautWithRock(rock);
			}
			// Colisión Astronauta <-> Plataforma fija
			for (Platform platform : level.platforms) {
				r2.set(platform.position.x, platform.position.y,
						platform.bounds.width, platform.bounds.height);
				Rectangle r1Bottom = new Rectangle();
				r1Bottom.set(r1.x, r1.y, r1.width, 0.01f);

				if (!r1Bottom.overlaps(r2)) {
					continue;
				}

				onCollisionAstronautWithPlatform(platform);
			}
			// Colisión Astronauta <-> Plataforma de mov. vertical
			for (MovingPlatform platform : level.movingPlatforms) {
				r2.set(platform.position.x, platform.position.y,
						platform.bounds.width, platform.bounds.height);
				Rectangle r1Bottom = new Rectangle();
				r1Bottom.set(r1.x, r1.y, r1.width, 0.01f);
				if (!r1Bottom.overlaps(r2)) {
					continue;
				}
				onCollisionAstronautWithPlatform(platform);
			}
			// Colisión Astronauta <-> Plataforma de mov. horizontal
			for (ForwardPlatform platform : level.fwdPlatforms) {
				r2.set(platform.position.x, platform.position.y,
						platform.bounds.width, platform.bounds.height);
				Rectangle r1Bottom = new Rectangle();
				r1Bottom.set(r1.x, r1.y, r1.width, 0.01f);
				if (!r1Bottom.overlaps(r2)) {
					continue;
				}
				onCollisionAstronautWithForwardPlatform(platform);
			}
			// Colisión Astronauta <-> Plataforma de caída
			for (FallingPlatform platform : level.fallPlatforms) {
				r2.set(platform.position.x, platform.position.y,
						platform.bounds.width, platform.bounds.height);
				Rectangle r1Bottom = new Rectangle();
				r1Bottom.set(r1.x, r1.y, r1.width, 0.01f);

				if (!r1Bottom.overlaps(r2)) {
					continue;
				}
				((FallingPlatform) platform).active = true;
				onCollisionAstronautWithPlatform(platform);
			}
			// Colisión Astronauta <-> Plataforma elástica
			for (BouncingPlatform bouncingPlatform : level.bouncingPlatforms) {
				r2.set(bouncingPlatform.position.x,
						bouncingPlatform.position.y,
						bouncingPlatform.bounds.width,
						bouncingPlatform.bounds.height);
				Rectangle r1Bottom = new Rectangle();
				r1Bottom.set(r1.x, r1.y, r1.width, 0.01f);

				if (!r1Bottom.overlaps(r2) && !bouncingPlatform.active) {
					continue;
				}
				if (!bouncingPlatform.active
						&& (isJumpPressed(0) || isJumpPressed(1))) {
					bouncingPlatform.time = 50;
				}
				onCollisionAstronautWithBouncingPlatform(bouncingPlatform);
			}

			// Colisión Astronauta <-> Muro
			for (Wall wall : level.walls) {
				r2.set(wall.position.x, wall.position.y, wall.bounds.width,
						wall.bounds.height);
				if (!r1.overlaps(r2)) {
					continue;
				}
				onCollisionAstronautWithWall(wall);
			}

			// Colisión Astronauta <-> Pieza
			for (Piece piece : level.pieces) {
				if (piece.collected) {
					continue;
				}
				r2.set(piece.position.x, piece.position.y, piece.bounds.width,
						piece.bounds.height);
				if (!r1.overlaps(r2)) {
					continue;
				}
				onCollisionAstronautWithPiece(piece);
				break;
			}

			// // Colisión Astronauta <-> Barra energética
			for (FlyPower flyPower : level.flyPowers) {
				if (flyPower.collected) {
					continue;
				}
				r2.set(flyPower.position.x, flyPower.position.y,
						flyPower.bounds.width, flyPower.bounds.height);
				if (!r1.overlaps(r2)) {
					continue;
				}
				onCollisionAstronautWithFeather(flyPower);
				break;
			}

			// Colisión Astronauta <-> Vida extra
			for (ExtraLife extraLife : level.extraLifes) {
				if (extraLife.collected) {
					continue;
				}
				r2.set(extraLife.position.x, extraLife.position.y,
						extraLife.bounds.width, extraLife.bounds.height);
				if (!r1.overlaps(r2)) {
					continue;
				}
				onCollisionAstronautWithExtraLife(extraLife);
				break;
			}

			// Colisión Astronauta <-> Checkpoint
			for (Checkpoint checkpoint : level.checkpoint) {
				if (checkpoint.active) {
					continue;
				}
				r2.set(checkpoint.position.x, checkpoint.position.y,
						checkpoint.bounds.width, checkpoint.bounds.height);
				if (!r1.overlaps(r2)) {
					continue;
				}
				onCollisionAstronautWithCheckpoint(checkpoint);
				break;
			}

			// Colisión Astronauta <-> Caja || Suelo <-> Caja
			for (Box box : level.boxes) {
				r2.set(box.position.x, box.position.y, box.bounds.width,
						box.bounds.height);
				if (r1.overlaps(r2)) {
					onCollisionAstronautWithBox(box);
				}
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
				if (box.position.y <= -1.5299072) {
					box.position.y = -1.5299072f;
					box.falling = false;
				}
			}

			// Colisión Astronauta <-> Enemigo vertical || Laser <-> Enemigo vertical
			for (Enemy enemy : level.enemies) {
				r2.set(enemy.position.x, enemy.position.y, enemy.bounds.width,
						enemy.bounds.height);
				if (level.laser != null) {
					r3.set(level.laser.position.x, level.laser.position.y,
							level.laser.bounds.width, level.laser.bounds.height);
					if (r2.overlaps(r3)) {
						onCollisionLaserWithEnemy(enemy);
					}
				}
				if (!r1.overlaps(r2)) {
					continue;
				}
				if (enemy.alive) {
					onCollisionAstronautWithEnemy();
				}
				break;
			}

			// Colisión Astronauta <-> Babosa alienígena
			for (EnemyForward enemy : level.enemiesFwd) {
				r2.set(enemy.position.x, enemy.position.y, enemy.bounds.width,
						enemy.bounds.height);
				if (level.laser != null) {
					r3.set(level.laser.position.x, level.laser.position.y,
							level.laser.bounds.width, level.laser.bounds.height);
					if (r2.overlaps(r3)) {
						onCollisionLaserWithEnemy(enemy);
					}
				}
				// Si te acercas al enemigo este se comenzará a mover hacia la
				// izquierda
				if (r2.x - r1.x < 4)
					enemy.moving = true;
				if (isGoalReached())
					enemy.moving = false;
				if (!r1.overlaps(r2)) {
					continue;
				}
				if (enemy.alive) {
					onCollisionAstronautWithEnemy();
				}
				break;
			}

			// Colisión Astronauta <-> Gigante
			if (level.giant != null) {
				r2.set(level.giant.position.x, level.giant.position.y,
						level.giant.bounds.width, level.giant.bounds.height);
				if (level.laser != null) {
					r3.set(level.laser.position.x, level.laser.position.y,
							level.laser.bounds.width, level.laser.bounds.height);
					if (r2.overlaps(r3)) {
						onCollisionLaserWithGiant(level.giant);
					}
				}
				if (goalReached) {
					level.giant.StopMoving();
				} else if (r1.overlaps(r2)) {
					onCollisionAstronautWithEnemy();
				}
			}

			// Colisión Astronauta <-> Meta
			if (!goalReached) {
				r2.set(level.goal.bounds);
				r2.x += level.goal.position.x;
				r2.y += level.goal.position.y;

				int x1 = new Float(r1.x).intValue();
				int x2 = new Float(r2.x).intValue();

				int y1 = new Float(r1.y).intValue();
				int y2 = new Float(r2.y).intValue();

				if ((x1 == x2 || x1 == x2 - 1) && (y1 == y2 || y1 == y2 + 1)) {
					onCollisionAstronautWithGoal();
				}
			}
		}
	}

	private void handleInputGame(float deltaTime) {
		int j;
		if (cameraHelper.hasTarget(level.astronaut)) {
			if (isLeftPressed(0) || isLeftPressed(1) /* && !isRightPressed(1) */) {
				level.astronaut.velocity.x = -level.astronaut.terminalVelocity.x;
			} else if (isRightPressed(0) || isRightPressed(1)) {
				level.astronaut.velocity.x = level.astronaut.terminalVelocity.x;
			}
			for (int i = 0; i < 2; i++) {
				j = i == 0 ? 1 : 0;
				// Controlamos si el usuario pulsa alguno de los controles.

				// Si pulsa el botón de salto.
				if (isJumpPressed(i)) {
					level.astronaut.setJumping(true);
				}
				// Si pulsa el botón de disparo (si está habilitado)
				else if (isShootPressed(i) && !level.astronaut.shooting) {
					level.astronaut.shooting = true;
				}
			}
		} else {
			level.astronaut.setJumping(false);
		}
	}

	// Devuelve True si no quedan más vidas y False en caso contrario.
	public boolean isGameOver() {
		return lives < 0;
	}

	// Devuelve True si el personaje está en el agua y False en caso contrario.
	public boolean isPlayerInWater() {
		return level.astronaut.position.y < -1;
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

	public boolean isShootPressed(int pointer) { // Código comentado para conservarlo para un futuro
		/*
		 * if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType()
		 * == ApplicationType.iOS) { return Gdx.input.isTouched(pointer) &&
		 * cShoot.contains((float) Gdx.input.getX(pointer), (float)
		 * Gdx.input.getY(pointer)); } else { return
		 * Gdx.input.isKeyPressed(Keys.ALT_LEFT); }
		 */
		return Gdx.input.isKeyPressed(Keys.ALT_LEFT);
	}

	// Devuelve True si se ha alcanzado la meta y False en caso contrario.
	public boolean isGoalReached() {
		return goalReached;
	}

	// Devuelve True si el juego está en pausa y False en caso contrario.
	public boolean isPaused() {
		return paused;
	}

	public int getNivel() {
		return nivel;
	}

	@Override
	public void dispose() {
		/*
		 * if (b2world != null) b2world.dispose();
		 */
	}
}
