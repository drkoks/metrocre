package com.metrocre.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));


        TextButton playButton = new TextButton("", skin, "play");
        TextButton exitButton = new TextButton("", skin, "exit");



        playButton.setSize(200, 200);
        playButton.setPosition(MyGame.WIDTH / 2 - 100, MyGame.HEIGHT / 2 - 50);
        exitButton.setSize(200, 200);
        exitButton.setPosition(MyGame.WIDTH / 2 - 100, MyGame.HEIGHT / 2 - 200 - 50);


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        Label titleLabel = new Label("Metrocre", labelStyle);
        titleLabel.setSize(100, 50);
        titleLabel.setPosition(MyGame.WIDTH / 2  -40, MyGame.HEIGHT - 50);


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


        stage.addActor(playButton);
        stage.addActor(exitButton);
        stage.addActor(titleLabel);


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


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
