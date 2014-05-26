package com.me.mygdxgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.me.mygdxgame.objects.AbstractGameObject;
import com.me.mygdxgame.objects.Box;
import com.me.mygdxgame.objects.Checkpoint;
import com.me.mygdxgame.objects.Clouds;
import com.me.mygdxgame.objects.Enemy;
import com.me.mygdxgame.objects.EnemyForward;
import com.me.mygdxgame.objects.FallingPlatform;
import com.me.mygdxgame.objects.ForwardPlatform;
import com.me.mygdxgame.objects.BouncingPlatform;
import com.me.mygdxgame.objects.Giant;
import com.me.mygdxgame.objects.Laser;
import com.me.mygdxgame.objects.Mountains;
import com.me.mygdxgame.objects.MovingPlatform;
import com.me.mygdxgame.objects.Platform;
import com.me.mygdxgame.objects.Rock;
import com.me.mygdxgame.objects.Wall;
import com.me.mygdxgame.objects.PoisonOverlay;
import com.me.mygdxgame.objects.Astronaut;
import com.me.mygdxgame.objects.Piece;
import com.me.mygdxgame.objects.FlyPower;
import com.me.mygdxgame.objects.ExtraLife;
import com.me.mygdxgame.objects.Goal;
import com.me.mygdxgame.utils.AudioManager;

public class Level {
	public static final String TAG = Level.class.getName();

	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), // black
		ROCK(0, 255, 0), // green
		PLATFORM(128, 128, 128), // grey
		MOVING_PLATFORM(64, 64, 4), // dark yellow
		FORWARD_PLATFORM(112, 146, 190), // blue
		FALLING_PLATFORM(64, 128, 128), // blue
		BOUNCING_PLATFORM(128, 128, 64), // dark beige
		WALL(64, 0, 0), // dark red
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		ITEM_POWERBAR(255, 0, 255), // purple
		ITEM_PIECE(255, 255, 0), // yellow
		GOAL(255, 0, 0), // red
		ITEM_EXTRALIFE(0, 0, 255), // blue
		CHECKPOINT(255, 69, 0), // orange
		ENEMY(63, 72, 204), // indigo
		ENEMY_FORWARD(163, 73, 164), // purple
		GIANT(128, 64, 0), // brown
		BOX(255, 128, 128); // pink

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

	// Elementos que forman el recorrido del nivel.
	public Array<Rock> rocks;
	public Array<Platform> platforms;
	public Array<MovingPlatform> movingPlatforms;
	public Array<ForwardPlatform> fwdPlatforms;
	public Array<FallingPlatform> fallPlatforms;
	public Array<BouncingPlatform> bouncingPlatforms;
	public Array<Wall> walls;

	// Elementos decorativos
	public Clouds clouds;
	public Mountains mountains;
	public PoisonOverlay poisonOverlay;

	// Personajes del juego.
	public Astronaut astronaut;
	public Array<Enemy> enemies;
	public Array<EnemyForward> enemiesFwd;
	public Giant giant;

	// Elementos
	public Array<Piece> pieces;
	public Array<FlyPower> flyPowers;
	public Array<ExtraLife> extraLifes;
	public Array<Checkpoint> checkpoint;
	public Array<Box> boxes;

	// Laser de disparo
	public Laser laser;

	// Elemento que constituye la meta del nivel.
	public Goal goal;
	
	private int level; // Alacenar el nivel es necesario para renderizar una meta u otra.

	// Constructor
	public Level(String filename, boolean checkpointReached, int level) {
		init(filename, checkpointReached, level);
		// AudioManager.instance.play(Assets.instance.music.song02);
	}

	private void init(String filename, boolean checkpointReached, int level) {
		this.level = level;
		
		// Personaje del jugador.
		astronaut = null;

		// Enemigos
		enemies = new Array<Enemy>();
		enemiesFwd = new Array<EnemyForward>();
		giant = null; // Gigante en el nivel de correr.

		// Otros objetos del nivel.
		rocks = new Array<Rock>();
		platforms = new Array<Platform>();
		movingPlatforms = new Array<MovingPlatform>();
		fallPlatforms = new Array<FallingPlatform>();
		fwdPlatforms = new Array<ForwardPlatform>();
		bouncingPlatforms = new Array<BouncingPlatform>();
		walls = new Array<Wall>();

		pieces = new Array<Piece>();
		flyPowers = new Array<FlyPower>();
		extraLifes = new Array<ExtraLife>();
		checkpoint = new Array<Checkpoint>();
		boxes = new Array<Box>();

		laser = null;

		// Carga un mapa de pixeles a partir de la imagen del nivel.
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));

		// Escaneamos los pixeles en vertical de arriba-izquierda a
		// abajo-derecha.

		int lastPixel = -1;
		for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
			for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
				AbstractGameObject obj = null;
				float offsetHeight = 0;

				// height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;

				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				if (BLOCK_TYPE.WALL.sameColor(currentPixel)) // Pared
				{
					if (lastPixel != currentPixel) {
						obj = new Wall();
						float heightIncreaseFactor = 0.7f;
						offsetHeight = -3.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y
								* heightIncreaseFactor + offsetHeight);
						walls.add((Wall) obj);
					} else {
						walls.get(walls.size - 1).increaseLength(1);
					}
				}
				lastPixel = currentPixel;
			}
		}

		// Escaneamos los pixeles de la imagen de arriba-izquierda a
		// abajo-derecha.
		lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;
				float offsetHeight = 0;

				// height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;

				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);

				// Comparamos el color de cada pixel de la imagen y si
				// corresponde con alguno de los
				// asignados a los objetos del nivel, creamos un objeto de ese
				// tipo en esa coordenada o pixel x e y.

				// Pixel vacío
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					// No hacemos nada.
				} else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) // Roca
				{
					if (lastPixel != currentPixel) {
						obj = new Rock();
						float heightIncreaseFactor = 0.7f;
						offsetHeight = 0.0f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y
								* heightIncreaseFactor + offsetHeight);
						rocks.add((Rock) obj);
					} else {
						rocks.get(rocks.size - 1).increaseLength(1);
					}
				} else if (BLOCK_TYPE.PLATFORM.sameColor(currentPixel)) // Plataforma
																		// flotante
				{
					if (lastPixel != currentPixel) {
						obj = new Platform();
						offsetHeight = 1.0f;
						obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
								+ offsetHeight);
						platforms.add((Platform) obj);
					} else {
						platforms.get(platforms.size - 1).increaseLength(1);
					}
				} else if (BLOCK_TYPE.MOVING_PLATFORM.sameColor(currentPixel)) // Plataforma
																				// flotante
																				// móvil
				{
					if (lastPixel != currentPixel) {
						obj = new MovingPlatform();
						offsetHeight = 1.0f;
						obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
								+ offsetHeight);
						movingPlatforms.add((MovingPlatform) obj);
						((MovingPlatform) obj).initMove(obj.position.y);
					} else {
						movingPlatforms.get(movingPlatforms.size - 1)
								.increaseLength(1);
					}
				} else if (BLOCK_TYPE.FORWARD_PLATFORM.sameColor(currentPixel)) // Plataforma
																				// móvil
																				// horizontal
				{
					if (lastPixel != currentPixel) {
						obj = new ForwardPlatform();
						offsetHeight = 1.0f;
						obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
								+ offsetHeight);
						fwdPlatforms.add((ForwardPlatform) obj);
						((ForwardPlatform) obj).initMove(obj.position.x);
					} else {
						fwdPlatforms.get(fwdPlatforms.size - 1).increaseLength(
								1);
					}
				} else if (BLOCK_TYPE.FALLING_PLATFORM.sameColor(currentPixel)) // Plataforma
																				// móvil
																				// horizontal
				{
					obj = new FallingPlatform();
					offsetHeight = 1.0f;
					obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
							+ offsetHeight);
					fallPlatforms.add((FallingPlatform) obj);
				} else if (BLOCK_TYPE.BOUNCING_PLATFORM.sameColor(currentPixel)) // Elemento
																		// que
																		// impulsa
																		// al
																		// jugador
																		// hacia
																		// arriba
				{
					obj = new BouncingPlatform();
					offsetHeight = 1.0f;
					obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
							+ offsetHeight);
					bouncingPlatforms.add((BouncingPlatform) obj);

				} else if (BLOCK_TYPE.WALL.sameColor(currentPixel)) {
				} else if (!checkpointReached
						&& BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) // Punto
																					// aparición
																					// del
																					// jugador
				{
					obj = new Astronaut();
					offsetHeight = 1.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					astronaut = (Astronaut) obj;
				} else if (BLOCK_TYPE.ITEM_POWERBAR.sameColor(currentPixel)) // Pluma
																			// (Barra
																			// energética)
				{
					obj = new FlyPower();
					offsetHeight = 1.0f;
					obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
							+ offsetHeight);
					flyPowers.add((FlyPower) obj);
				} else if (BLOCK_TYPE.ITEM_PIECE.sameColor(currentPixel)) // Moneda
																				// de
																				// oro
																				// (Tuerca)
				{
					obj = new Piece();
					offsetHeight = 1.0f;
					obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
							+ offsetHeight);
					pieces.add((Piece) obj);
				} else if (BLOCK_TYPE.ITEM_EXTRALIFE.sameColor(currentPixel)) // Zanahoria
				{
					obj = new ExtraLife();
					offsetHeight = 1.0f;
					obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
							+ offsetHeight);
					extraLifes.add((ExtraLife) obj);
				} else if (BLOCK_TYPE.CHECKPOINT.sameColor(currentPixel)) // Checkpoint
				{
					obj = new Checkpoint();
					offsetHeight = 0.5f;
					obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
							+ offsetHeight);
					checkpoint.add((Checkpoint) obj);

					if (checkpointReached) {
						obj = new Astronaut();
						offsetHeight = 0f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y
								+ offsetHeight);
						astronaut = (Astronaut) obj;
					}
				} else if (BLOCK_TYPE.BOX.sameColor(currentPixel)) // Caja
																	// metálica
				{
					obj = new Box();
					offsetHeight = 1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					boxes.add((Box) obj);
				} else if (BLOCK_TYPE.ENEMY.sameColor(currentPixel)) // Enemigo
				{
					obj = new Enemy();
					offsetHeight = 1f;
					obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
							+ offsetHeight);
					((Enemy) obj).initMove(obj.position.y);
					enemies.add((Enemy) obj);
				} else if (BLOCK_TYPE.ENEMY_FORWARD.sameColor(currentPixel)) // Enemigo
																				// 2
				{
					obj = new EnemyForward();
					offsetHeight = 1f;
					obj.position.set(pixelX, baseHeight /** obj.dimension.y*/
							+ offsetHeight);
					enemiesFwd.add((EnemyForward) obj);
				} else if (BLOCK_TYPE.GIANT.sameColor(currentPixel)) // Gigante
				{
					obj = new Giant();
					offsetHeight = -12.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					giant = (Giant) obj;
				} else if (BLOCK_TYPE.GOAL.sameColor(currentPixel)) // Punto de
																	// meta
				{
					obj = new Goal(level);
					//offsetHeight = -0.15f;
					//obj.position.set(pixelX, baseHeight + offsetHeight);
					goal = (Goal) obj;
					goal.setPosition(pixelX, baseHeight);
				} else // Color de pixel u objeto desconocido
				{
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

		// Creamos y colocamos los objetos decorativos del nivel (Nubes y
		// montañas)
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
		mountains = new Mountains(pixmap.getWidth());
		//mountains.position.set(-1, -1);
		mountains.position.set(-1, 2);
		poisonOverlay = new PoisonOverlay(pixmap.getWidth());
		//poisonOverlay.position.set(0, -3.5f);
		poisonOverlay.position.set(0, -1);

		// Liberamos la memoria utilizada por el mapa de pixeles.
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}

	// Los objetos se renderizan por capas: los primeros en el método se renderizan antes
	public void render(SpriteBatch batch) {

		mountains.render(batch); // Colocamos las montañas en el nivel.
		
		goal.render(batch); // Dibujamos la meta.

		// Generamos las rocas.
		for (Rock rock : rocks) {
			rock.render(batch);
		}

		// Colocamos las plataformas flotantes.
		for (Platform platform : platforms) {
			platform.render(batch);
		}

		// Colocamos las plataformas flotantes que se mueven verticalmente.
		for (MovingPlatform platform : movingPlatforms) {
			platform.render(batch);
		}

		// Colocamos las plataformas flotantes que se mueven horizontalmente.
		for (ForwardPlatform platform : fwdPlatforms) {
			platform.render(batch);
		}

		// Colocamos las plataformas que caen cuando el jugador se mantiene un
		// tiempo en ellas
		for (FallingPlatform platform : fallPlatforms) {
			platform.render(batch);
		}

		// Colocamos los géisers
		for (BouncingPlatform bouncingPlatform : bouncingPlatforms) {
			bouncingPlatform.render(batch);
		}

		// Colocamos los géisers
		for (Wall wall : walls) {
			wall.render(batch);
		}

		// Renderizamos las monedas.
		for (Piece piece : pieces) {
			piece.render(batch);
		}

		// Colocamos las plumas.
		for (FlyPower flyPower : flyPowers) {
			flyPower.render(batch);
		}

		// Renderizamos las zanahorias.
		for (ExtraLife extraLife : extraLifes) {
			extraLife.render(batch);
		}

		// Colocamos los enemigos.
		for (Enemy enemy : enemies) {
			if (enemy.alive)
				enemy.render(batch);
			else
				enemy = null;
		}

		// Colocamos los enemigos que te atacan en línea recta.
		for (EnemyForward enemy : enemiesFwd) {
			if (enemy.alive)
				enemy.render(batch);
			else
				enemy = null;
		}

		// Si el nivel contiene un gigante, lo colocamos.
		if (giant != null && giant.hp > 0) {
			giant.render(batch);
		} else {
			giant = null;
		}

		// Colocamos el checkpoint.
		for (Checkpoint cp : checkpoint) {
			cp.render(batch);
		}

		// Renderizar las cajas metálicas
		for (Box box : boxes) {
			box.render(batch);
		}
		if (astronaut.shooting) {
			Laser l = new Laser(astronaut.viewDirection);
			if (laser == null) {
				if (astronaut.viewDirection == astronaut.viewDirection.RIGHT)
					l.position.set(astronaut.position.x
							+ astronaut.bounds.width, astronaut.position.y
							+ astronaut.bounds.height / 2);
				else
					l.position.set(astronaut.position.x
							- astronaut.bounds.width, astronaut.position.y
							+ astronaut.bounds.height / 2);
				laser = l;
			}
			if (laser.duracion < laser.maxDuracion)
				laser.render(batch);
			else {
				astronaut.shooting = false;
				laser = null;
			}
		} else
			laser = null;

		astronaut.render(batch); // Renderizar el personaje.
		poisonOverlay.render(batch); // Dibujar la capa de agua.
		clouds.render(batch); // Dibujar las nubes.
	}

	public void update(float deltaTime) {
		astronaut.update(deltaTime);

		for (Rock rock : rocks) {
			rock.update(deltaTime);
		}

		for (Platform platform : platforms) {
			platform.update(deltaTime);
		}

		for (MovingPlatform platform : movingPlatforms) {
			platform.update(deltaTime);
		}

		for (ForwardPlatform platform : fwdPlatforms) {
			platform.update(deltaTime);
		}

		for (FallingPlatform platform : fallPlatforms) {
			platform.update(deltaTime);
		}

		for (BouncingPlatform bouncingPlatform : bouncingPlatforms) {
			bouncingPlatform.update(deltaTime);
		}

		for (Wall wall : walls) {
			wall.update(deltaTime);
		}

		for (Piece piece : pieces) {
			piece.update(deltaTime);
		}

		for (FlyPower flyPower : flyPowers) {
			flyPower.update(deltaTime);
		}

		for (ExtraLife extraLife : extraLifes) {
			extraLife.update(deltaTime);
		}

		for (Checkpoint cp : checkpoint) {
			cp.update(deltaTime);
		}

		for (Box box : boxes) {
			box.update(deltaTime);
		}

		for (Enemy enemy : enemies) {
			if (enemy.alive && enemy != null)
				enemy.update(deltaTime);
			if (enemy.dying) {
				astronaut.shooting = false;
				enemy.dying = false;
			}
		}

		for (EnemyForward enemy : enemiesFwd) {
			if (enemy.alive && enemy != null)
				enemy.update(deltaTime);
			if (enemy.dying) {
				astronaut.shooting = false;
				enemy.dying = false;
			}
		}

		if (astronaut.shooting && laser != null) {
			laser.update(deltaTime);
		}

		if (giant != null && giant.hp > 0) {
			giant.update(deltaTime);
		}

		clouds.update(deltaTime);
	}
}
