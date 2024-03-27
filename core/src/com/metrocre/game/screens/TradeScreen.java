package com.metrocre.game.screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.metrocre.game.MyGame;

public class TradeScreen extends ScreenAdapter {
    private Stage stage;
    private MyGame game;

    public TradeScreen(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Skin skin =  new Skin(Gdx.files.internal("lib.json"));

        Label titleLabel = new Label("TradeCenter", skin);
        titleLabel.setSize(100, 50);
        titleLabel.setPosition(MyGame.WIDTH / 2  -40, MyGame.HEIGHT - 50);

        Label itemInfo = new Label("", skin);

        // Создание UI компонентов
        List<String> itemList = new List<>(skin);

        itemList.setItems("Speed", "Defence", "Attack");
        TextButton tradeButton = new TextButton("Trade", skin, "trade");
        tradeButton.setSize(100, 50);
        itemList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedItem = itemList.getSelected();
                itemInfo.setText("Информация о " + selectedItem + ": [описание, стоимость и т.д.]");
            }
        });

        tradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String selectedItem = itemList.getSelected();
                System.out.println(selectedItem + " куплен.");
            }
        });

        TextButton continueButton = new TextButton("Continue", skin, "continue");
        continueButton.setSize(100, 50);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(itemList).expandX().fillX();
        table.row();
        table.add(itemInfo).expandX().fillX();
        table.row();
        table.add(tradeButton).expandX();
        table.row();
        table.add(continueButton).expandX();
        table.pack();
        stage.addActor(table);


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}
