package com.metrocre.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private float x;
	private float y;
	private OrthographicCamera camera;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("img1.png");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 1, 1, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(img, x, y);
		batch.end();
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			x = touchPos.x - img.getWidth() / 2.f;
			y = touchPos.y - img.getHeight() / 2.f;
		}
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}
