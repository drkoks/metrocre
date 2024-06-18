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
import com.metrocre.game.event.trade.BuyEventData;
import com.metrocre.game.event.trade.TradeEvents;

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
        coinsLabel = new Label("Coins: " + game.playersProfile.getMoney(), skin);
        speedLevelLabel = new Label("Speed Level: " + game.playersProfile.getSpeed(), skin);
        defenseLevelLabel = new Label("Defense Level: " + game.playersProfile.getDefence(), skin);
        attackLevelLabel = new Label("Attack Level: " + game.playersProfile.getAttack(), skin);

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
                game.setScreen(new GameScreen(game));
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, stage.getWidth(), stage.getHeight());
        batch.end();
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
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
        coinsLabel.setText("Coins: " + game.playersProfile.getMoney());
        speedLevelLabel.setText("Speed Level: " + game.playersProfile.getSpeed());
        defenseLevelLabel.setText("Defense Level: " + game.playersProfile.getDefence());
        attackLevelLabel.setText("Attack Level: " + game.playersProfile.getAttack());
    }
}
