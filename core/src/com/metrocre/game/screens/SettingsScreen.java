package com.metrocre.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.metrocre.game.MyGame;

import java.util.HashMap;
import java.util.Map;

import states.PlayerStat;

public class SettingsScreen implements Screen {
    private final Stage stage;

    public SettingsScreen(final MyGame game) {
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("lib.json"));

        Label volumeLabel = new Label("Volume", skin);
        volumeLabel.setSize(100, 30);
        volumeLabel.setPosition(50, Gdx.graphics.getHeight() - 50);

        Slider volumeSlider = new Slider(0, 100, 1, false, skin);
        volumeSlider.setSize(200, 30);
        volumeSlider.setPosition(50, Gdx.graphics.getHeight() - 100);

        volumeSlider.setValue(100 * game.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = volumeSlider.getValue() / 100f;
                game.setVolume(volume);
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.setSize(100, 30);
        backButton.setPosition(Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 50);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        PlayerStat stats  = game.localPlayerProfile.getStatistics();
        int y = Gdx.graphics.getHeight() - 200;
        Map<String, Integer> kills = new HashMap<>(stats.getAllKills());
        Label label = new Label("You defeted " + kills.size() + " different enemies all-time, here is list:", skin);
        label.setPosition(50, y);
        stage.addActor(label);
        y -= 20;
        for (Map.Entry<String, Integer> entry : kills.entrySet()) {
            label = new Label(entry.getKey() + ": " + entry.getValue(), skin);
            label.setPosition(50, y);
            stage.addActor(label);
            y -= 20;
        }

        stage.addActor(volumeLabel);
        stage.addActor(volumeSlider);
        stage.addActor(backButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0.31f, 0.49f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}