package com.metrocre.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class HUD {
    private Player player;
    private Stage stage;
    private Label healthLabel;
    private Label coinsLabel;
    private Label trainHealthLabel;
    private ProgressBar bossHealthBar;
    private Skin skin;

    private int bossHealth = 0;
    private int trainHealth = 0;
    private int bossMaxHealth = 0;

    public HUD(Player player, Stage stage, Skin skin) {
        this.skin = skin;
        this.player = player;
        this.stage = stage;
        Label bossHealthLabel = null;
        if (player != null) {
            healthLabel = new Label("Health: " + player.getHealth(), skin);
            coinsLabel = new Label("Coins: " + player.getMoney(), skin);
            bossHealthBar = new ProgressBar(0, 200f, 1, false, skin);
            bossHealthBar.setValue(bossHealth);
                //bossHealthBar.setSize(50, 5);
            trainHealthLabel = new Label("Train Health: " + trainHealth, skin);
            bossHealthLabel = new Label("Boss Health: ", skin);
        }

        Table table = new Table();
        table.top();

        table.setSize(120, 50);
        table.setPosition(80, 130);
        table.add(trainHealthLabel).expandX().left();
        table.add(bossHealthLabel);
        table.row();
        table.add(healthLabel).expandX().left();
        table.add(bossHealthBar);
        table.row();
        table.add(coinsLabel).expandX().left();


        stage.addActor(table);
    }

    public void setBossHealth(int bossHealth) {
        this.bossHealth = bossHealth;
        bossHealthBar.setValue((float) bossHealth / bossMaxHealth * 200);
    }

    public void setBossMaxHealth(int bossMaxHealth) {
        this.bossMaxHealth = bossMaxHealth;
    }

    public void setTrainHealth(int trainHealth) {
        this.trainHealth = trainHealth;
    }

    public void update() {
        if (player != null) {
            healthLabel.setText("Health: " + player.getHealth());
            coinsLabel.setText("Coins: " + player.getMoney());
            if (bossMaxHealth > 0) {
                bossHealthBar.setValue((float) bossHealth / bossMaxHealth * 200);
            }
            trainHealthLabel.setText("Train Health: " + trainHealth);
        }
    }

    public void dispose() {
        stage.dispose();
    }
}
