package com.metrocre.game.world;

import static com.metrocre.game.MyGame.SCALE;

import com.badlogic.gdx.graphics.Texture;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.WorldManager;

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
        return playerX <= x + width + SCALE && playerX >= x - width -SCALE && playerY >= y && playerY <= y + height;
    }
}