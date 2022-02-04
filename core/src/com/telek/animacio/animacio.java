package com.telek.animacio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class animacio extends ApplicationAdapter {

	// Constant rows and columns of the sprite sheet
	private static final int FRAME_COLS = 10, FRAME_ROWS = 8;

	// Objects used
	Animation<TextureRegion> idleDownAnimation, idleLeftAnimation, idleUpAnimation, idleRightAnimation, walkDownAnimation, walkLeftAnimation, walkUpAnimation, walkRightAnimation; // Must declare frame type (TextureRegion)
	Texture walkSheet;
	SpriteBatch spriteBatch;

	// A variable for tracking elapsed time for the animation
	float stateTime;

	@Override
	public void create() {

		// Load the sprite sheet as a Texture
		walkSheet = new Texture(Gdx.files.internal("SpriteSheetLink.png"));

		// Use the split utility method to create a 2D array of TextureRegions. This is
		// possible because this sprite sheet contains frames of equal size and they are
		// all aligned.
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);

		// Place the regions into a 1D array in the correct order, starting from the top
		// left, going across first. The Animation constructor requires a 1D array.
		int index = 0;

		TextureRegion[] idleDownFrames = new TextureRegion[FRAME_COLS];
		for (int j = 0; j < FRAME_COLS; j++) {
			idleDownFrames[index++] = tmp[0][2];
		}

		TextureRegion[] idleLeftFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			idleLeftFrames[index++] = tmp[1][2];
		}

		TextureRegion[] idleUpFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			idleUpFrames[index++] = tmp[2][0];
		}

		TextureRegion[] idleRightFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			idleRightFrames[index++] = tmp[3][2];
		}

		TextureRegion[] walkDownFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			walkDownFrames[index++] = tmp[4][FRAME_COLS];
		}

		TextureRegion[] walkLeftFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			walkLeftFrames[index++] = tmp[5][FRAME_COLS];
		}

		TextureRegion[] walkUpFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			walkUpFrames[index++] = tmp[6][FRAME_COLS];
		}

		TextureRegion[] walkRightFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			walkRightFrames[index++] = tmp[7][j];
		}

		// Initialize the Animations with the frame interval and array of frames
		idleDownAnimation = new Animation<TextureRegion>(0.1f, idleDownFrames);
		idleLeftAnimation = new Animation<TextureRegion>(0.1f, idleLeftFrames);
		idleUpAnimation = new Animation<TextureRegion>(0.1f, idleUpFrames);
		idleRightAnimation = new Animation<TextureRegion>(0.1f, idleRightFrames);
		walkDownAnimation = new Animation<TextureRegion>(0.1f, walkDownFrames);
		walkLeftAnimation = new Animation<TextureRegion>(0.1f, walkLeftFrames);
		walkUpAnimation = new Animation<TextureRegion>(0.1f, walkUpFrames);
		walkRightAnimation = new Animation<TextureRegion>(0.1f, walkRightFrames);

		// Instantiate a SpriteBatch for drawing and reset the elapsed animation
		// time to 0
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

		// Get current frame of animation for the current stateTime
		TextureRegion currentFrame = idleDownAnimation.getKeyFrame(stateTime, true);
		spriteBatch.begin();
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) animacio.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
		// Keep bucket inside screen.
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;
		// Check if it's time to create new drops.
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
		// Move the raindrops.
		for (Iterator<Rectangle> i = raindrops.iterator(); i.hasNext(); ) {
			Rectangle raindrop = i.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) {
				//i.remove();
				rainMusic.dispose();
				// Game over screen
				game.setScreen(new GameOver(game, dropCount));
			}
			if(raindrop.overlaps(bucket) && raindrop.y > 80) {
				System.out.println(raindrop.y);
				dropCount += 1;
				dropSound.play();
				i.remove();
			}
		}
		spriteBatch.end();
	}

	@Override
	public void dispose() { // SpriteBatches and Textures must always be disposed
		spriteBatch.dispose();
		walkSheet.dispose();
	}
}