package com.metrocre.game.world;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class HUD {
    private Player player;
    private Stage stage;
    private Label healthLabel;
    private Label coinsLabel;

    public HUD(Player player, Stage stage, Skin skin) {
        this.player = player;
        this.stage = stage;
        healthLabel = new Label("Health: " + player.getHealth(), skin);
        coinsLabel = new Label("Coins: " + player.getMoney(), skin);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.setSize(25, 10);
        table.setPosition(0, 5);
        table.add(healthLabel).expandX().left();
        table.row();
        table.add(coinsLabel).expandX().left();

        stage.addActor(table);
    }

    public void update() {
        healthLabel.setText("Health: " + player.getHealth());
        coinsLabel.setText("Coins: " + player.getMoney());
    }

    public void dispose() {
        stage.dispose();
    }
}
