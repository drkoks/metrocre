package com.metrocre.game.world;

import static com.metrocre.game.MyGame.SCALE;

import com.badlogic.gdx.graphics.Texture;
import com.metrocre.game.network.Network;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.WorldManager;

public class Train extends Entity {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private int health;

    public Train(float x, float y, int health, WorldManager worldManager, Texture texture, float width, float height) {
        super(worldManager, texture, width, height);
        this.x = x;
        this.y = y;
        this.health = health;
        this.width = width;
        this.height = height;
        body = worldManager.createRectangleBody(x, y, width, height, this);
    }

    public boolean isPlayerOnTrain(Player player) {
        float playerX = player.getX();
        float playerY = player.getY();
        return playerX <= x + width + SCALE && playerX >= x - width -SCALE && playerY >= y && playerY <= y + height;
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (worldManager.getServer() != null) {
            Network.TakeDamage takeDamage = new Network.TakeDamage();
            takeDamage.senderId = -1;
            takeDamage.receiverId = id;
            takeDamage.damage = damage;
            worldManager.getServer().packToSend(takeDamage);
        }
    }

    public int getHealth() {
        return health;
    }
}