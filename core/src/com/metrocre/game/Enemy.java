package com.metrocre.game;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends Entity {
    public static final float SIZE = 1f;

    public Enemy(float x, float y, WorldManager worldManager, Texture texture) {
        super(worldManager, texture, SIZE, SIZE);
        body = worldManager.createCircleBody(x, y, SIZE / 2, false, false, this);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case Messages.HIT:
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
