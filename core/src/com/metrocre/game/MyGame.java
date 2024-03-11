package com.metrocre.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MyGame extends Game {
	@Override
	public void create() {
		screen = new GameScreen();
	}

	@Override
	public void render() {
		screen.render(1f / 60);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
