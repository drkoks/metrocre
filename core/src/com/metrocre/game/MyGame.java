package com.metrocre.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.TimeUtils;
import com.metrocre.game.screens.MainMenuScreen;

public class MyGame extends Game {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final float UNIT_SCALE = 1 / 32f;
    static final long FPS = 60;
    static final long NANOS_PER_FRAME = 1_000_000_000 / FPS;
    public PlayersProfile playersProfile = new PlayersProfile("Player", 1, 0, 0, 1, 1, 1);
    private float volume = 1.0f;
    private long prevRenderTime = 0;

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
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
