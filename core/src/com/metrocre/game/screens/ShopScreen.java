package com.metrocre.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.metrocre.game.MyGame;
import com.metrocre.game.Upgrades;
import com.metrocre.game.event.trade.BuyEventData;
import com.metrocre.game.event.trade.TradeEvents;

public class ShopScreen extends ScreenAdapter {
    private final Stage stage;
    private final MyGame game;
    private final SpriteBatch batch;
    private Texture backgroundTexture;


    public ShopScreen(final MyGame game) {
        this.game = game;
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());

    }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("lib.json"));

        backgroundTexture = new Texture(Gdx.files.internal("data/list-background.png"));
        Label titleLabel = new Label("Shop", skin);
        titleLabel.setSize(100, 50);
        titleLabel.setPosition((float) MyGame.WIDTH / 2 - 40, MyGame.HEIGHT - 50);
        TextButton backButton = new TextButton("Back", skin);
        backButton.setSize(100, 30);
        backButton.setPosition(Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 50);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GameScreen.loadGameState(game));
            }
        });
        List<String> itemList = new List<>(skin);
        Label itemInfo = new Label("", skin);
        itemList.setItems("Railgun", "Pistol");
        TextButton tradeButton = new TextButton("Trade", skin, "trade");
        tradeButton.setSize(100, 50);
        itemList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Upgrades selectedItem = Upgrades.fromString(itemList.getSelected());
                itemInfo.setText("NEW weapon: " + selectedItem.toString() + " cost " +
                        game.playersProfile.getSelectedItemCost(selectedItem) + " coins");
            }
        });

        tradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Upgrades selectedItem = Upgrades.fromString(itemList.getSelected());
                if (game.playersProfile.canBuyItem(selectedItem)) {
                    BuyEventData buyEventData = new BuyEventData();
                    buyEventData.upgrade = selectedItem;
                    game.getMessageDispatcher().dispatchMessage(TradeEvents.BUY, buyEventData);
                    itemInfo.setText(selectedItem + " purchased.");
                } else {
                    itemInfo.setText("Not enough coins.");
                }
            }
        });


        Table table = new Table();
        table.setFillParent(true);

        table.add(titleLabel).colspan(2).center().padTop(10);
        table.row();
        table.add(itemList).expandX().fillX().padTop(20).padLeft(10).padRight(10);
        table.row();
        table.add(itemInfo).expandX().fillX().padTop(20).padLeft(10).padRight(10);
        table.row();
        table.add(tradeButton).expandX().padTop(20).padLeft(10).padRight(10);
        table.row();

        table.add(backButton).left().padTop(10).padLeft(10);

        table.row();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0.31f, 0.49f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, stage.getWidth(), stage.getHeight());
        batch.end();
        stage.act(Math.min(delta, 1 / 30f));
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
        backgroundTexture.dispose();
        batch.dispose();
    }
}