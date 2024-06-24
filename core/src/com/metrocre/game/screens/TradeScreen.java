package com.metrocre.game.screens;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.metrocre.game.MyGame;
import com.metrocre.game.Upgrades;
import com.metrocre.game.network.Network;

public class TradeScreen extends ScreenAdapter {
    private Stage stage;
    private final MyGame game;
    private Label coinsLabel;
    private Texture backgroundTexture;
    private Label speedLevelLabel;
    private Label defenseLevelLabel;
    private Label attackLevelLabel;
    private final SpriteBatch batch;

    public TradeScreen(MyGame game) {
        this.game = game;
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("lib.json"));

        backgroundTexture = new Texture(Gdx.files.internal("data/list-background.png"));

        Label titleLabel = new Label("TradeCenter", skin);
        titleLabel.setSize(100, 50);
        titleLabel.setPosition((float) MyGame.WIDTH / 2 - 40, MyGame.HEIGHT - 50);
        coinsLabel = new Label("Coins: " + game.localPlayerProfile.getMoney(), skin);
        speedLevelLabel = new Label("Speed Level: " + game.localPlayerProfile.getSpeed(), skin);
        defenseLevelLabel = new Label("Defense Level: " + game.localPlayerProfile.getDefence(), skin);
        attackLevelLabel = new Label("Attack Level: " + game.localPlayerProfile.getAttack(), skin);

        Label itemInfo = new Label("", skin);


        List<String> itemList = new List<>(skin);

        itemList.setItems("Speed", "Defence", "Attack");
        TextButton tradeButton = new TextButton("Trade", skin, "trade");
        tradeButton.setSize(100, 50);
        itemList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Upgrades selectedItem = Upgrades.fromString(itemList.getSelected());
                itemInfo.setText("+ 20% to " + selectedItem.toString() + " cost " +
                        game.localPlayerProfile.getSelectedItemCost(selectedItem) + " coins");
            }
        });

        tradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Upgrades selectedItem = Upgrades.fromString(itemList.getSelected());
                if (game.localPlayerProfile.canBuyItem(selectedItem)) {
                    Network.Buy buy = new Network.Buy();
                    buy.upgrades = selectedItem;
                    game.getClient().packToSend(buy);
                    itemInfo.setText(selectedItem + " purchased.");
                    updateLabels();
                } else {
                    itemInfo.setText("Not enough coins.");
                }
            }
        });

        TextButton continueButton = new TextButton("Continue", skin, "continue");
        continueButton.setSize(100, 50);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getClient().packToSend(new Network.PlayerReady());
            }
        });
        Table table = new Table();
        table.setFillParent(true);

        table.add(titleLabel).colspan(2).center().padTop(10);
        table.row();

        table.add(coinsLabel).left().padTop(10).padLeft(10);
        table.row();
        table.add(speedLevelLabel).left().padTop(10).padLeft(10);
        table.row();
        table.add(defenseLevelLabel).left().padTop(10).padLeft(10);
        table.row();
        table.add(attackLevelLabel).left().padTop(10).padLeft(10);
        table.row();
        table.add(itemList).expandX().fillX().padTop(20).padLeft(10).padRight(10);
        table.row();
        table.add(itemInfo).expandX().fillX().padTop(10).padLeft(10).padRight(10);
        table.row();

        table.add(tradeButton).expandX().padTop(20).padLeft(10).padRight(10);
        table.row();

        table.add(continueButton).expandX().padTop(20).padLeft(10).padRight(10);
        table.row();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        while (true) {
            Object event = game.getClient().getRemoteEvent();
            if (event == null) {
                break;
            }
            if (event instanceof Network.NextLevel) {
                game.setScreen(new GameScreen(game));
                break;
            } else if (event instanceof Network.Buy) {
                Network.Buy buy = (Network.Buy) event;
                game.localPlayerProfile.buyItem(buy.upgrades);
            }
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        updateLabels();

        batch.draw(backgroundTexture, 0, 0, stage.getWidth(), stage.getHeight());
        batch.end();
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();

        game.getClient().sendAll();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        batch.dispose();
    }
    private void updateLabels() {
        coinsLabel.setText("Coins: " + game.localPlayerProfile.getMoney());
        speedLevelLabel.setText("Speed Level: " + game.localPlayerProfile.getSpeed());
        defenseLevelLabel.setText("Defense Level: " + game.localPlayerProfile.getDefence());
        attackLevelLabel.setText("Attack Level: " + game.localPlayerProfile.getAttack());
    }
}
