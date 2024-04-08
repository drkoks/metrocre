package com.metrocre.game;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Texture;

public class Enemy extends Entity {
    public static final float SIZE = 1f;

    public Enemy(float x, float y, WorldManager worldManager, Texture texture) {
        super(worldManager, texture, SIZE, SIZE);
        body = worldManager.createCircleBody(x, y, SIZE / 2, false, false, this);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == Messages.HIT) {
            //worldManager.getWorld().destroyBody(this.getBody());
            destroyed = true;
            //worldManager.getWorld().destroyBody(this.getBody());
            if (msg.sender instanceof Player) {
                Player player = (Player) msg.sender;
                player.addMoney(100);
            }
            return true;
        }
        return false;
    }
}
