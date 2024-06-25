package com.metrocre.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.metrocre.game.MyGame;
import com.metrocre.game.network.Network;

import java.util.Queue;


public class LobbyScreen extends ScreenAdapter {
    private Label inGameCounter;
    private Label readyCounter;
    private int joined = 0;
    private int ready = 0;

    private final Stage stage;
    private final MyGame game;

    public LobbyScreen(final MyGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("lib.json"));
        readyCounter = new Label("Players ready: " + 0, skin);
        inGameCounter = new Label("Players in lobby: " + 1, skin);
        readyCounter.setSize(200, 50);
        readyCounter.setPosition((float) (Gdx.graphics.getWidth() - readyCounter.getWidth()) /2, Gdx.graphics.getHeight() - 100);
        inGameCounter.setSize(200, 50);
        inGameCounter.setPosition((float) (Gdx.graphics.getWidth() - inGameCounter.getWidth()) /2, Gdx.graphics.getHeight() - 150);

        TextButton readyButton = new TextButton("Ready", skin);
        readyButton.setSize(400, 100);
        readyButton.setPosition((float) (Gdx.graphics.getWidth() - readyButton.getWidth()) /2, readyButton.getHeight());

        readyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getClient().packToSend(new Network.PlayerReady());
            }
        });

        stage.addActor(readyButton);
        stage.addActor(readyCounter);
        stage.addActor(inGameCounter);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        while (true) {
            Object event = game.getClient().getRemoteEvent();
            if (event == null) {
                break;
            } else if (event instanceof Network.PlayerReady) {
                ready = ((Network.PlayerReady) event).cnt;
            } else if (event instanceof Network.PlayerJoined) {
                joined = ((Network.PlayerJoined) event).cnt;
            } else if (event instanceof Network.NextLevel) {
                game.setScreen(new GameScreen(game));
                break;
            }
        }

        Gdx.gl.glClearColor(1, 0.31f, 0.49f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        updateLabels();
        stage.draw();

        game.getClient().sendAll();
    }

    private void updateLabels() {
        readyCounter.setText("Players ready: " + ready);
        inGameCounter.setText("Players in lobby: " + joined);
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
