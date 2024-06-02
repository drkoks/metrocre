package com.metrocre.game.towers;

import static com.metrocre.game.MyGame.SCALE;

import com.badlogic.gdx.graphics.Texture;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.WorldManager;

public abstract class Tower extends Entity {
    public static final float SIZE = SCALE;

    protected float health;
    protected Player player;

    public Tower(float x, float y, float health, WorldManager worldManager, Player player, Texture texture) {
        super(worldManager, texture);
        this.health = health;
        this.player = player;
        body = worldManager.createCircleBody(x, y, SIZE / 2, true, false, this);
    }

    public Player getPlayer() {
        return player;
    }
}
