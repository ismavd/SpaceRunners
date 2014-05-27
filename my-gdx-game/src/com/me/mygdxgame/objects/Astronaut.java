package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;
import com.me.mygdxgame.utils.Constants;
import com.me.mygdxgame.utils.GamePreferences;
import com.me.mygdxgame.utils.CharacterSkin;
import com.badlogic.gdx.math.MathUtils;
import com.me.mygdxgame.utils.AudioManager;

public class Astronaut extends AbstractGameObject {
	public static final String TAG = Astronaut.class.getName();
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

	public enum VIEW_DIRECTION {
		LEFT, RIGHT
	}

	public enum JUMP_STATE {
		GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
	}

	private TextureRegion regAstronaut;
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasBarPowerup;
	public boolean dustOn;
	public boolean viewDirectionOn;
	public float timeLeftBarPowerup;

	public boolean shooting;

	public ParticleEffect dustParticles = new ParticleEffect();

	private Animation animStanding;
	private Animation animMoving;
	private Animation animJumping;
	private Animation animFlying;

	public Astronaut() {
		init();
	}

	public void init() {
		dimension.set(0.5f, 1f);
		
		animStanding = Assets.instance.astronaut.animStanding;
		animMoving = Assets.instance.astronaut.animMoving;
		animJumping = Assets.instance.astronaut.animJumping;
		animFlying = Assets.instance.astronaut.animFlying;
		setAnimation(animStanding);

		// Centro de la imagen
		origin.set(dimension.x / 2, dimension.y / 2);
		// Recuadro para la detección de colisiones entre objetos
		bounds.set(0, 0, dimension.x, dimension.y);
		// Físicas
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		// Dirección de vista del personaje
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Estado del salto
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		// Power-ups
		hasBarPowerup = false;
		timeLeftBarPowerup = 0;

		shooting = false;

		// Pârtículas de polvo
		dustParticles.load(Gdx.files.internal("particles/dust.pfx"),
				Gdx.files.internal("particles"));
		dustOn = true;
		viewDirectionOn = true;
	}

	public void setJumping(boolean jumpKeyPressed) {
		switch (jumpState) {
		case GROUNDED: // El personaje permanece en una plataforma
			if (jumpKeyPressed) {
				AudioManager.instance.play(Assets.instance.sounds.jump);
				// Start counting jump time from the beginning
				timeJumping = 0;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		case JUMP_RISING: // Elevación en el aire
			if (!jumpKeyPressed)
				jumpState = JUMP_STATE.JUMP_FALLING;
			else if (jumpKeyPressed && hasBarPowerup) {
				AudioManager.instance.play(
						Assets.instance.sounds.jumpWithFlyPower, 1,
						MathUtils.random(1.0f, 1.1f));
				timeJumping = JUMP_TIME_OFFSET_FLYING;
			}
			break;
		case FALLING:// Caída
		case JUMP_FALLING: // Caída después de saltar
			break;
		}
	}

	public void setBarPowerup(boolean pickedUp) {
		hasBarPowerup = pickedUp;
		if (pickedUp) {
			timeLeftBarPowerup = Constants.ITEM_FEATHER_POWERUP_DURATION;
			terminalVelocity.set(5.0f, 4.0f);
		} else {
			terminalVelocity.set(3.0f, 4.0f);
		}
	}

	public boolean hasBarPowerup() {
		return hasBarPowerup && timeLeftBarPowerup > 0;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (velocity.x != 0 && viewDirectionOn) {
			
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT
					: VIEW_DIRECTION.RIGHT;
			if (animation == animStanding) {
				dimension.set(1f, 1f);
				setAnimation(animMoving);
			}
		} 
		if (jumpState == JUMP_STATE.FALLING) {
			if (velocity.x != 0 && animation == animJumping) {
				dimension.set(1f, 1f);
				setAnimation(animMoving);
			} else if (velocity.x == 0 && animation != animStanding){
				dimension.set(0.5f, 1f);
				setAnimation(animStanding);
			}
		}
		if (jumpState != JUMP_STATE.FALLING) {
			dimension.set(1f, 1f);
			setAnimation(animJumping);
		}
		if (!viewDirectionOn)
			viewDirectionOn = true;
		if (timeLeftBarPowerup > 0) {
			timeLeftBarPowerup -= deltaTime;
			if (timeLeftBarPowerup < 0) {
				// Inhabilitar power-up
				timeLeftBarPowerup = 0;
				setBarPowerup(false);
			}
		}
		dustParticles.update(deltaTime);
	}

	@Override
	protected void updateMotionY(float deltaTime) {
		switch (jumpState) {
		case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			if (velocity.x != 0 && dustOn) {
				dustParticles.setPosition(position.x + dimension.x / 2,
						position.y);
				dustParticles.start();
			}
			break;
		case JUMP_RISING:
			// Comprobación del tiempo de salto
			timeJumping += deltaTime;
			// ¿Queda tiempo de salto?
			if (timeJumping <= JUMP_TIME_MAX) {
				// Saltando
				velocity.y = terminalVelocity.y;
			}
			break;
		case FALLING:
			break;
		case JUMP_FALLING:
			// Comprobación del tiempo de salto
			timeJumping += deltaTime;
			if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
				// Saltando
				velocity.y = terminalVelocity.y;
			}
		}
		if (jumpState != JUMP_STATE.GROUNDED) {
			if (dustOn)
				dustParticles.allowCompletion();
			dustOn = true;
			super.updateMotionY(deltaTime);
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;

		// Dibuja partículas de polvo
		dustParticles.draw(batch);

		batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin]
				.getColor());

		reg = regAstronaut;
		if (hasBarPowerup)
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		//System.out.println(animation.);
		if (animation != null) {
			reg = animation.getKeyFrame(stateTime, true);
		} else
			reg = Assets.instance.astronaut.astronaut;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), 
				animation == animJumping || animation == animFlying ?
						viewDirection == VIEW_DIRECTION.RIGHT : viewDirection == VIEW_DIRECTION.LEFT,
				false);
		batch.setColor(1, 1, 1, 1);
	}
}
