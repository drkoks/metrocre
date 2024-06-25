package com.metrocre.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.metrocre.game.MyGame;


public class MainMenuScreen implements Screen {

    private final Stage stage;
    private final Music backgroundMusic;
    private final SpriteBatch batch;
    private final Texture logo;

    public MainMenuScreen(final MyGame game) {
        stage = new Stage(new FitViewport(MyGame.WIDTH, MyGame.HEIGHT));
        logo = new Texture(Gdx.files.internal("logo.png"));

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/MainMenuTheme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        backgroundMusic.setVolume(game.getVolume());
        batch = new SpriteBatch();

        TextButton serverPlayButton = new TextButton("", skin, "play");
        TextButton joinPlayButton = new TextButton("", skin, "join");
        TextButton settingsButton = new TextButton("", skin, "settings");

        serverPlayButton.setSize(200, 200);
        float delta = ((float) MyGame.WIDTH /3 - serverPlayButton.getWidth()) / 2;
        serverPlayButton.setPosition(delta, 0);
        joinPlayButton.setSize(200, 200);
        joinPlayButton.setPosition((float) MyGame.WIDTH /3+ delta, 0);
        settingsButton.setSize(200, 200);
        settingsButton.setPosition((float) 2* MyGame.WIDTH /3 + delta, 0);

        joinPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backgroundMusic.stop();
                game.setScreen(new LobbyScreen(game)); //TODO: change to join screen
            }
        });
        serverPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backgroundMusic.stop();
                game.setScreen(new LobbyScreen(game)); //TODO: change to server screen
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backgroundMusic.stop();
                game.setScreen(new SettingsScreen(game));
            }
        });

        stage.addActor(serverPlayButton);
        stage.addActor(settingsButton);
        stage.addActor(joinPlayButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0.31f, 0.49f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        float logoWidth = 400;
        float logoHeight = 200;
        batch.draw(logo, (Gdx.graphics.getWidth() - logoWidth) / 2, MyGame.HEIGHT - logoHeight, logoWidth, logoHeight);
        batch.end();

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
