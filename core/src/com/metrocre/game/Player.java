package com.metrocre.game;

import static java.lang.Math.max;

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
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements Telegraph {
    public static final float SIZE = 1f;

    private float shootCooldown = 0;
    private float reloadTime = 1f;

    public Player(float x, float y, WorldManager worldManager, Texture texture) {
        super(SIZE, SIZE, worldManager, texture);
        body = worldManager.createCircleBody(x, y, SIZE / 2, this);
    }

    public void shoot(Vector2 direction) {
        if (shootCooldown > 0 || direction.len() == 0) {
            return;
        }
        shootCooldown = reloadTime;
        class MyRayCastCallback implements RayCastCallback {
            Enemy hitTarget = null;

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                Object fixtureUserData = fixture.getUserData();
                if (fixtureUserData instanceof Enemy) {
                    hitTarget = (Enemy) fixtureUserData;
                    return fraction;
                }
                return -1;
            }
        }
        MyRayCastCallback rayCastCallback = new MyRayCastCallback();
        worldManager.getWorld().rayCast(
            rayCastCallback,
            body.getPosition(),
            body.getPosition().cpy().add(direction.scl(10))
        );
        if (rayCastCallback.hitTarget != null) {
            worldManager.getMessageDispatcher().dispatchMessage(this, rayCastCallback.hitTarget, Messages.HIT);
        }
    }

    @Override
    public void update(float delta) {
        shootCooldown -= delta;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
