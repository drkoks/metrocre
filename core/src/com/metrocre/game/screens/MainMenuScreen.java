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
    private final MyGame game;

    public MainMenuScreen(final MyGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(MyGame.WIDTH, MyGame.HEIGHT));
        logo = new Texture(Gdx.files.internal("logo.png"));

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/MainMenuTheme.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        backgroundMusic.setVolume(game.getVolume());
        batch = new SpriteBatch();


        TextButton playButton = new TextButton("", skin, "play");
        TextButton exitButton = new TextButton("", skin, "exit");
        TextButton settingsButton = new TextButton("", skin, "settings");


        playButton.setSize(200, 200);
        playButton.setPosition(150, MyGame.HEIGHT / 2 - 200);
        settingsButton.setSize(200, 200);
        settingsButton.setPosition(MyGame.WIDTH - 200 - 150, MyGame.HEIGHT / 2 - 200);


        //Label.LabelStyle labelStyle = new Label.LabelStyle();
        //labelStyle.font = skin.getFont("default-font");
        //Label titleLabel = new Label("Metrocre", labelStyle);
        //titleLabel.setSize(100, 50);
        //titleLabel.setPosition(MyGame.WIDTH / 2  -40, MyGame.HEIGHT - 50);


        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backgroundMusic.stop();
                game.setScreen(new GameScreen(game));
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backgroundMusic.stop();
                game.setScreen(new SettingsScreen(game));
            }
        });


        stage.addActor(playButton);
        stage.addActor(settingsButton);
        //stage.addActor(titleLabel);


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
        batch.draw(logo, (Gdx.graphics.getWidth() - logoWidth) / 2, Gdx.graphics.getHeight() - logoHeight, logoWidth, logoHeight);
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
