package com.metrocre.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGame extends Game {
	static final long FPS = 60;
	static final long NANOS_PER_FRAME = 1_000_000_000 / FPS;

	private long prevRenderTime = 0;

	@Override
	public void create() {
		screen = new GameScreen();
	}

	@Override
	public void render() {
		long currentTime = TimeUtils.nanoTime();
		if (prevRenderTime == 0) {
			prevRenderTime = currentTime;
		}
		while (prevRenderTime <= currentTime) {
			screen.render(1f / FPS);
			prevRenderTime += NANOS_PER_FRAME;
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
