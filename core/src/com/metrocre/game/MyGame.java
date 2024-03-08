package com.metrocre.game;

import com.badlogic.gdx.Game;
import com.metrocre.game.screens.MainMenuScreen;

public class MyGame extends Game {
    public static final int WIDTH = 800; // Set your preferred width
    public static final int HEIGHT = 480; // Set your preferred height

    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}