package com.metrocre.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.metrocre.game.MyGame;

import java.util.HashMap;
import java.util.Map;

import states.PlayerStat;

public class GameOverScreen implements Screen {
    private final Stage stage;
    private Image img;
    private boolean isWin;

    public GameOverScreen(final MyGame game, boolean isWin) {
        this.isWin = isWin;
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("lib.json"));
        if (isWin) {
            img = new Image(new Texture("victory.png"));
            img.setSize(150, 150);
        } else {
            img = new Image(new Texture("game-over.png"));
            img.setSize(300, 200);
        }

        img.setPosition((float) (Gdx.graphics.getWidth() - img.getWidth()) / 2, Gdx.graphics.getHeight()- img.getHeight());

        PlayerStat stats  = game.playersProfile.getStatistics();
        int y = Gdx.graphics.getHeight() - 200;
        Map<String, Integer> kills = new HashMap<>(stats.getKills());
        Label label = new Label("You defeted " + kills.size() + " different enemies, here is list:", skin);
        label.setPosition(50, y);
        stage.addActor(label);
        y -= 20; // move down for the next label
        for (Map.Entry<String, Integer> entry : kills.entrySet()) {
            label = new Label(entry.getKey() + ": " + entry.getValue(), skin);
            label.setPosition(50, y);
            stage.addActor(label);
            y -= 20;
        }
        TextButton backButton = new TextButton("Back to Main menu", skin);
        backButton.setSize(200, 30);
        backButton.setPosition((float) (Gdx.graphics.getWidth()-backButton.getWidth()) /2, 50);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(img);
        stage.addActor(backButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (isWin) {
            Gdx.gl.glClearColor(1, 0.2f, 0.5f, 1);
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
        }
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