package com.metrocre.game;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Texture;

public class Train extends Entity {
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public Train(float x, float y, WorldManager worldManager, Texture texture, float width, float height) {
        super(worldManager, texture, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        body = worldManager.createRectangleBody(x, y, width, height, this);
    }

    public boolean isPlayerOnTrain(Player player) {
        float playerX = player.getX();
        float playerY = player.getY();
        return playerX <= x + width + 1 && playerX >= x - width -1 && playerY >= y && playerY <= y + height;
    }
}