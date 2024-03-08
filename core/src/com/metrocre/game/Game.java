package com.metrocre.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture texture;
	private float x, y; // Position of the sprite

	@Override
	public void create () {
		batch = new SpriteBatch();
		texture = new Texture("badlogic.jpg"); // Load a texture (you need to put the image in the assets folder)
		x = Gdx.graphics.getWidth() / 2 - texture.getWidth() / 2; // Center the sprite horizontally
		y = Gdx.graphics.getHeight() / 2 - texture.getHeight() / 2; // Center the sprite vertically
	}

	@Override
	public void render () {
		// Clear the screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Draw the sprite
		batch.begin();
		batch.draw(texture, x, y);
		batch.end();
	}

	@Override
	public void dispose () {
		// Dispose of resources when they are no longer needed
		batch.dispose();
		texture.dispose();
	}
}
