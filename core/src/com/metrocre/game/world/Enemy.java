package com.metrocre.game.world;

import static java.lang.Math.max;

import com.badlogic.gdx.graphics.Texture;
import com.metrocre.game.towers.Tower;

public class Enemy extends Entity {
    public static final float SIZE = 1f;
    private float health;
    private int reward;

    public Enemy(float x, float y, float health, int reward, WorldManager worldManager, Texture texture) {
        super(worldManager, texture, SIZE, SIZE);
        this.health = health;
        this.reward = reward;
        body = worldManager.createCircleBody(x, y, SIZE / 2, false, false, this);
    }

    public void takeDamage(float damage, Entity sender) {
        if (health == 0) {
            return;
        }
        health = max(0f, health - damage);
        if (health == 0) {
            destroy();
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else if (sender instanceof Tower) {
                player = ((Tower) sender).getPlayer();
            }
            if (player != null) {
                player.addMoney(reward);
            }
        }
    }
}
