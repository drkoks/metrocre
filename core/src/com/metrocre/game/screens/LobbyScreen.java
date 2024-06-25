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


public class LobbyScreen extends ScreenAdapter {
    private Label inGameCounter;
    private Label readyCounter;

    private final Stage stage;
    private final MyGame game;
    private boolean isReady = false;

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
                isReady = !isReady;
                //TODO: send ready signal to server
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
        Gdx.gl.glClearColor(1, 0.31f, 0.49f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        updateLabels();
        stage.draw();
    }

    private void updateLabels() {
        int playersReady = 0; //TODO: get number of players ready
        if (isReady) {
            playersReady++;
        }
        int playersInLobby = 1; //TODO: get number of players in lobby
        if (playersReady == playersInLobby) {
            game.setScreen(new GameScreen(game)); //TODO: start game
        }
        readyCounter.setText("Players ready: " + 0);
        inGameCounter.setText("Players in lobby: " + 1);
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
