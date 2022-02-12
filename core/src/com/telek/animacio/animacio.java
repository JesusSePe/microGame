package com.telek.animacio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class animacio extends ApplicationAdapter {

	// Constant rows and columns of the sprite sheet
	private static final int FRAME_COLS = 10, FRAME_ROWS = 8, FRAME_IDLE = 3, FRAME_1 = 1;

	// Objects used
	Animation<TextureRegion> idleDownAnimation, idleLeftAnimation, idleUpAnimation, idleRightAnimation, walkDownAnimation, walkLeftAnimation, walkUpAnimation, walkRightAnimation; // Must declare frame type (TextureRegion)
	Texture walkSheet;
	SpriteBatch spriteBatch;
	Texture back;
	int posY, posX = 0;

	// A variable for tracking elapsed time for the animation
	float stateTime;

	@Override
	public void create() {

		// Load the sprite sheet as a Texture
		walkSheet = new Texture(Gdx.files.internal("SpriteSheetLink.png"));

		// Load background
		back = new Texture(Gdx.files.internal("maze.png"));

		// Use the split utility method to create a 2D array of TextureRegions. This is
		// possible because this sprite sheet contains frames of equal size and they are
		// all aligned.
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);

		// Place the regions into a 1D array in the correct order, starting from the top
		// left, going across first. The Animation constructor requires a 1D array.
		int index = 0;

		TextureRegion[] idleDownFrames = new TextureRegion[FRAME_IDLE];
		for (int j = 0; j < FRAME_IDLE; j++) {
			idleDownFrames[index++] = tmp[0][j];
		}

		TextureRegion[] idleLeftFrames = new TextureRegion[FRAME_IDLE];
		index = 0;
		for (int j = 0; j < FRAME_IDLE; j++) {
			idleLeftFrames[index++] = tmp[1][j];
		}

		TextureRegion[] idleUpFrames = new TextureRegion[FRAME_1];
		index = 0;
		for (int j = 0; j < FRAME_1; j++) {
			idleUpFrames[index++] = tmp[2][j];
		}

		TextureRegion[] idleRightFrames = new TextureRegion[FRAME_IDLE];
		index = 0;
		for (int j = 0; j < FRAME_IDLE; j++) {
			idleRightFrames[index++] = tmp[3][j];
		}

		TextureRegion[] walkDownFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			walkDownFrames[index++] = tmp[4][j];
		}

		TextureRegion[] walkLeftFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			walkLeftFrames[index++] = tmp[7][j];
		}

		TextureRegion[] walkUpFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			walkUpFrames[index++] = tmp[6][j];
		}

		TextureRegion[] walkRightFrames = new TextureRegion[FRAME_COLS];
		index = 0;
		for (int j = 0; j < FRAME_COLS; j++) {
			walkRightFrames[index++] = tmp[5][j];
		}

		// Initialize the Animations with the frame interval and array of frames
		idleDownAnimation = new Animation<>(0.3f, idleDownFrames);
		idleLeftAnimation = new Animation<>(0.3f, idleLeftFrames);
		idleUpAnimation = new Animation<>(0.3f, idleUpFrames);
		idleRightAnimation = new Animation<>(0.3f, idleRightFrames);
		walkDownAnimation = new Animation<>(0.1f, walkDownFrames);
		walkLeftAnimation = new Animation<>(0.1f, walkLeftFrames);
		walkUpAnimation = new Animation<>(0.1f, walkUpFrames);
		walkRightAnimation = new Animation<>(0.1f, walkRightFrames);

		// Instantiate a SpriteBatch for drawing and reset the elapsed animation
		// time to 0
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

		//TextureRegion currentFrame = idleDownAnimation.getKeyFrame(stateTime, true);
		spriteBatch.begin();

		// Draw background
		spriteBatch.draw(back, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		TextureRegion currentFrame;

		// Moving
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && posX < Gdx.graphics.getWidth() - 50){
			currentFrame = walkLeftAnimation.getKeyFrame(stateTime, true);
			posX += 200 * Gdx.graphics.getDeltaTime();

		} else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && posX > 0){
			currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
			posX -= 200 * Gdx.graphics.getDeltaTime();

		} else if(Gdx.input.isKeyPressed(Input.Keys.UP) && posY < Gdx.graphics.getHeight()){
			currentFrame = walkUpAnimation.getKeyFrame(stateTime, true);
			posY += 200 * Gdx.graphics.getDeltaTime();

		} else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && posY > 0){
			currentFrame = walkDownAnimation.getKeyFrame(stateTime, true);
			posY -= 200 * Gdx.graphics.getDeltaTime();

		} else {
			// Idle
			currentFrame = idleDownAnimation.getKeyFrame(stateTime, true);
		}
		spriteBatch.draw(currentFrame, posX, posY, 50, 50);


		spriteBatch.end();
	}

	@Override
	public void dispose() { // SpriteBatches and Textures must always be disposed
		spriteBatch.dispose();
		walkSheet.dispose();
	}
}