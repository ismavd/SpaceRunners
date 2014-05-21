package com.me.mygdxgame.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.game.Assets;

public class Piece extends AbstractGameObject {
	//private Array<TextureRegion> regPieces;
	private TextureRegion regPiece;
	public boolean collected;

	public Piece() {
		init();
	}

	private void init() {
		dimension.set(0.5f, 0.5f);
		//regGoldCoin = Assets.instance.goldCoin.goldCoin;
		//setAnimation(Assets.instance.piece.animPiece);
		//stateTime = MathUtils.random(0.0f, 1.0f);
		Array<TextureRegion> regPieces = new Array<TextureRegion>();
		regPieces.add(Assets.instance.piece.piece1);
		regPieces.add(Assets.instance.piece.piece2);
		regPieces.add(Assets.instance.piece.piece3);
		regPieces.add(Assets.instance.piece.piece4);
		regPieces.add(Assets.instance.piece.piece5);
		regPiece = regPieces.random();
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}

	public void render(SpriteBatch batch) {
		if (collected)
			return;
		TextureRegion reg = null;
		reg = regPiece;
		//reg = animation.getKeyFrame(stateTime, true);
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
	}

	public int getScore() {
		return 100;
	}

}
