package com.metrocre.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.metrocre.game.MyGame;


public class MainMenuScreen implements Screen {

    private Stage stage;
    private MyGame game;

    public MainMenuScreen(final MyGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(MyGame.WIDTH, MyGame.HEIGHT));

        // Create a skin
        Skin skin = new Skin();

        // Load texture regions for button states
        TextureRegion playButtonUp = new TextureRegion(new Texture(Gdx.files.internal("play.png")));
        TextureRegion playButtonDown = new TextureRegion(new Texture(Gdx.files.internal("play.png")));
        TextureRegion exitButtonUp = new TextureRegion(new Texture(Gdx.files.internal("exit.png")));
        TextureRegion exitButtonDown = new TextureRegion(new Texture(Gdx.files.internal("exit.png")));

        // Register texture regions with the skin
        skin.add("play-button-up", playButtonUp);
        skin.add("play-button-down", playButtonDown);
        skin.add("exit-button-up", exitButtonUp);
        skin.add("exit-button-down", exitButtonDown);

        // Load the default font
        BitmapFont defaultFont = new BitmapFont(Gdx.files.internal("default.fnt"));

        // Register the default font with the skin
        skin.add("default-font", defaultFont);

        // Load uiskin.json and register the styles
        skin.load(Gdx.files.internal("uiskin.json"));

        // Create buttons
        TextButton playButton = new TextButton("Play", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Set button positions and sizes
        playButton.setSize(200, 50);
        playButton.setPosition(MyGame.WIDTH / 2 - 100, MyGame.HEIGHT / 2);
        exitButton.setSize(200, 50);
        exitButton.setPosition(MyGame.WIDTH / 2 - 100, MyGame.HEIGHT / 2 - 100);

        // Add listeners to buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game)); // Switch to the game screen
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the application
            }
        });

        // Add buttons to the stage
        stage.addActor(playButton);
        stage.addActor(exitButton);

        // Make the stage handle inputs
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
