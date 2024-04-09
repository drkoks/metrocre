package com.metrocre.game;

import static java.lang.Math.max;

import com.badlogic.gdx.graphics.Texture;

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
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.addMoney(reward);
            }
        }
    }
}
