package com.me.mygdxgame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;
import com.me.mygdxgame.utils.Constants;
import com.me.mygdxgame.utils.GamePreferences;
import com.me.mygdxgame.utils.CharacterSkin;
import com.badlogic.gdx.math.MathUtils;
import com.me.mygdxgame.utils.AudioManager;

public class BunnyHead extends AbstractGameObject {
	public static final String TAG = BunnyHead.class.getName();
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

	public enum VIEW_DIRECTION {
		LEFT, RIGHT
	}

	public enum JUMP_STATE {
		GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
	}

	private TextureRegion regHead;
	private TextureRegion regPower;
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasFeatherPowerup;
	public float timeLeftFeatherPowerup;

	public BunnyHead() {
		init();
	}

	public void init() 
	{
		dimension.set(1, 1);
		regHead = Assets.instance.bunny.head;
		regPower = Assets.instance.bunnyPower.head;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		// Set physics values
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		// Power-ups
		hasFeatherPowerup = false;
		timeLeftFeatherPowerup = 0;
	}

	public void setJumping(boolean jumpKeyPressed) {
		switch (jumpState) {
		case GROUNDED: // Character is standing on a platform
			if (jumpKeyPressed) {
				AudioManager.instance.play(Assets.instance.sounds.jump);
				// Start counting jump time from the beginning
				timeJumping = 0;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		case JUMP_RISING: // Rising in the air
			if (!jumpKeyPressed)
				jumpState = JUMP_STATE.JUMP_FALLING;
			else if (jumpKeyPressed && hasFeatherPowerup) {
				AudioManager.instance.play(Assets.instance.sounds.jumpWithFeather, 1, MathUtils.random(1.0f, 1.1f));
				timeJumping = JUMP_TIME_OFFSET_FLYING;
			}
			break;
		case FALLING:// Falling down
		case JUMP_FALLING: // Falling down after jump
			break;
		}
	}

	public void setFeatherPowerup(boolean pickedUp) {
		hasFeatherPowerup = pickedUp;
		if (pickedUp) {
			timeLeftFeatherPowerup = Constants.ITEM_FEATHER_POWERUP_DURATION;
			terminalVelocity.set(5.0f, 4.0f);
		}
		else {
			terminalVelocity.set(3.0f, 4.0f);
		}			
	}

	public boolean hasFeatherPowerup() {
		return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
	}

	@Override
	public void update(float deltaTime) 
	{
		super.update(deltaTime);
		if (velocity.x != 0) 
		{
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT
					: VIEW_DIRECTION.RIGHT;
		}
		if (timeLeftFeatherPowerup > 0) 
		{
			timeLeftFeatherPowerup -= deltaTime;
			if (timeLeftFeatherPowerup < 0) 
			{
				// disable power-up
				timeLeftFeatherPowerup = 0;
				setFeatherPowerup(false);
			}
		}
	}

	@Override
	protected void updateMotionY(float deltaTime) 
	{
		switch (jumpState) 
		{
		case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			break;
		case JUMP_RISING:
			// Keep track of jump time
			timeJumping += deltaTime;
			// Jump time left?
			if (timeJumping <= JUMP_TIME_MAX) 
			{
				// Still jumping
				velocity.y = terminalVelocity.y;
			}
			break;
		case FALLING:
			break;
		case JUMP_FALLING:
			// Add delta times to track jump time
			timeJumping += deltaTime;
			// Jump to minimal height if jump key was pressed too short
			if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) 
			{
				// Still jumping
				velocity.y = terminalVelocity.y;
			}
		}
		if (jumpState != JUMP_STATE.GROUNDED)
			super.updateMotionY(deltaTime);
	}

	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		
		// Apply Skin Color
		batch.setColor(
		CharacterSkin.values()[GamePreferences.instance.charSkin]
		.getColor());
		
		// Set special color when game object has a feather power-up
		if (hasFeatherPowerup)
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		reg = regHead;
		
		// Código para cambiar de imagen al obtener una pluma (la imagen debe llamarse bunny_power.png)
		/*if (hasFeatherPowerup)
			reg = regPower;
		else
			reg = regHead;*/
		
		// Draw image
		
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT,
				false);
		// Reset color to white
		batch.setColor(1, 1, 1, 1);
	}
}
