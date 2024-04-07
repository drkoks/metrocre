package com.metrocre.game;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Bullet extends Entity {
    public static final float SIZE = 1f / 8;

    public Bullet(Vector2 position, Vector2 direction, float speed, WorldManager worldManager, Texture texture) {
        super(worldManager, texture);
        body = worldManager.createCircleBody(position.x, position.y, SIZE / 2, true, true, this);
        body.setLinearVelocity(direction.scl(speed));
    }

    public void update(float delta) {

    }

    public void draw(ShapeDrawer shapeDrawer) {
        shapeDrawer.filledCircle(body.getPosition().x, body.getPosition().y, SIZE, new Color(1, 1, 0, 1));
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case Messages.HIT:
                //worldManager.getWorld().destroyBody(body);
                destroyed = true;
                return true;
        }
        return false;
    }
}
