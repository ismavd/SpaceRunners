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
import com.me.mygdxgame.objects.Giant;
import com.me.mygdxgame.objects.Laser;
import com.me.mygdxgame.objects.Mountains;
import com.me.mygdxgame.objects.MovingPlatform;
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
		MOVING_PLATFORM(64, 64, 4), // dark yellow
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		ITEM_FEATHER(255, 0, 255), // purple
		ITEM_GOLD_COIN(255, 255, 0), // yellow
		GOAL(255, 0, 0), // red
		ITEM_CARROT(0, 0, 255), // blue
		CHECKPOINT(255, 69, 0), // orange
		ENEMY(63, 72, 204), // indigo
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

	// Elementos decorativos
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;

	// Personajes del juego.
	public BunnyHead bunnyHead;
	public Array<Enemy> enemies;
	public Giant giant;

	// Elementos
	public Array<GoldCoin> goldcoins;
	public Array<Feather> feathers;
	public Array<Carrot> carrots;
	public Array<Checkpoint> checkpoint;
	public Array<Box> boxes;

	// Laser de disparo
	public Laser laser;

	// Elemento que constituye la meta del nivel.
	public Goal goal;

	// Constructor
	public Level(String filename, boolean checkpointReached) {
		init(filename, checkpointReached);
		// AudioManager.instance.play(Assets.instance.music.song02);
	}

	private void init(String filename, boolean checkpointReached) {
		// Personaje del jugador.
		bunnyHead = null;

		// Enemigos
		enemies = new Array<Enemy>();
		giant = null; // Gigante en el nivel de correr.

		// Otros objetos del nivel.
		rocks = new Array<Rock>();
		platforms = new Array<Platform>();
		movingPlatforms = new Array<MovingPlatform>();
		goldcoins = new Array<GoldCoin>();
		feathers = new Array<Feather>();
		carrots = new Array<Carrot>();
		checkpoint = new Array<Checkpoint>();
		boxes = new Array<Box>();

		laser = null;

		// Carga un mapa de pixeles a partir de la imagen del nivel.
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));

		// Escaneamos los pixeles de la imagen de arriba-izquierda a
		// abajo-derecha.
		int lastPixel = -1;
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
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
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
						offsetHeight = -1.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y
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
						offsetHeight = -1.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y
								+ offsetHeight);
						movingPlatforms.add((MovingPlatform) obj);
						((MovingPlatform) obj).initMove(obj.position.y);
					} else {
						movingPlatforms.get(movingPlatforms.size - 1)
								.increaseLength(1);
					}
				} else if (!checkpointReached
						&& BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) // Punto
																					// aparición
																					// del
																					// jugador
				{
					obj = new BunnyHead();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					bunnyHead = (BunnyHead) obj;
				} else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) // Pluma
																			// (Barra
																			// energética)
				{
					obj = new Feather();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					feathers.add((Feather) obj);
				} else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) // Moneda
																				// de
																				// oro
																				// (Tuerca)
				{
					obj = new GoldCoin();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					goldcoins.add((GoldCoin) obj);
				} else if (BLOCK_TYPE.ITEM_CARROT.sameColor(currentPixel)) // Zanahoria
				{
					obj = new Carrot();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					carrots.add((Carrot) obj);
				} else if (BLOCK_TYPE.CHECKPOINT.sameColor(currentPixel)) // Checkpoint
				{
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
				} else if (BLOCK_TYPE.BOX.sameColor(currentPixel)) // Caja
																	// metálica
				{
					obj = new Box();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					boxes.add((Box) obj);
				} else if (BLOCK_TYPE.ENEMY.sameColor(currentPixel)) // Enemigo
				{
					obj = new Enemy();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					((Enemy) obj).initMove(obj.position.y);
					enemies.add((Enemy) obj);
				} else if (BLOCK_TYPE.GIANT.sameColor(currentPixel)) // Gigante
				{
					obj = new Giant();
					offsetHeight = -9f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					giant = (Giant) obj;
				} else if (BLOCK_TYPE.GOAL.sameColor(currentPixel)) // Punto de
																	// meta
				{
					obj = new Goal();
					offsetHeight = -7.0f;
					obj.position.set(pixelX, baseHeight + offsetHeight);
					goal = (Goal) obj;
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
		mountains.position.set(-1, -1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0, -3.75f);

		// Liberamos la memoria utilizada por el mapa de pixeles.
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}

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

		// Colocamos las plataformas flotantes que se mueven.
		for (MovingPlatform platform : movingPlatforms) {
			platform.render(batch);
		}

		// Renderizamos las monedas.
		for (GoldCoin goldCoin : goldcoins) {
			goldCoin.render(batch);
		}

		// Colocamos las plumas.
		for (Feather feather : feathers) {
			feather.render(batch);
		}

		// Renderizamos las zanahorias.
		for (Carrot carrot : carrots) {
			carrot.render(batch);
		}

		// Colocamos los enemigos.
		for (Enemy enemy : enemies) {
			if (enemy.alive)
				enemy.render(batch);
			else
				enemy = null;
		}

		// Si el nivel contiene un gigante, lo colocamos.
		if (giant != null) {
			giant.render(batch);
		}

		// Colocamos el checkpoint.
		for (Checkpoint cp : checkpoint) {
			cp.render(batch);
		}

		// Renderizar las cajas metálicas
		for (Box box : boxes) {
			box.render(batch);
		}

		if (bunnyHead.shooting) {
			Laser l = new Laser(bunnyHead.viewDirection);
			if (laser == null) {
				if (bunnyHead.viewDirection == bunnyHead.viewDirection.RIGHT)
					l.position.set(bunnyHead.position.x + bunnyHead.bounds.width,
							bunnyHead.position.y + bunnyHead.bounds.height / 2);
				else
					l.position.set(bunnyHead.position.x - bunnyHead.bounds.width,
							bunnyHead.position.y + bunnyHead.bounds.height / 2);
				laser = l;
			}
			if (laser.duracion < laser.maxDuracion)
				laser.render(batch);
			else {
				bunnyHead.shooting = false;
				laser = null;
			}
		}
		else
			laser = null;

		bunnyHead.render(batch); // Renderizar el personaje.
		waterOverlay.render(batch); // Dibujar la capa de agua.
		clouds.render(batch); // Dibujar las nubes.
	}

	public void update(float deltaTime) {
		bunnyHead.update(deltaTime);

		for (Rock rock : rocks) {
			rock.update(deltaTime);
		}

		for (Platform platform : platforms) {
			platform.update(deltaTime);
		}

		for (MovingPlatform platform : movingPlatforms) {
			platform.update(deltaTime);
		}

		for (GoldCoin goldCoin : goldcoins) {
			goldCoin.update(deltaTime);
		}

		for (Feather feather : feathers) {
			feather.update(deltaTime);
		}

		for (Carrot carrot : carrots) {
			carrot.update(deltaTime);
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
		}

		if (bunnyHead.shooting && laser != null) {
			laser.update(deltaTime);
		}

		if (giant != null) {
			giant.update(deltaTime);
		}

		clouds.update(deltaTime);
	}
}
