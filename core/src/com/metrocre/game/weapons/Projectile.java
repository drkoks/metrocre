package com.metrocre.game.weapons;

import static com.metrocre.game.MyGame.SCALE;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.WorldManager;
import com.metrocre.game.world.enemies.Enemy;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Projectile extends Entity {
    private static final Texture friedlyBulletTexture = new Texture("bullet.png");
    private static final Texture enemyBulletTexture = new Texture("bulletEnemy.png");
    private static final Texture healBulletTexture = new Texture("healBullet.png");
    public static final float SIZE = (float) SCALE / 8;

    private float damage;
    private int senderId;
    private boolean isHeal = false;

    public Projectile(Vector2 position, Vector2 direction, float damage, float speed, WorldManager worldManager, int senderId, boolean isHeal) {
        super(worldManager, isHeal ? healBulletTexture : worldManager.getEntity(senderId) instanceof Enemy ? enemyBulletTexture : friedlyBulletTexture);
        this.isHeal = isHeal;
        this.damage = damage;
        this.senderId = senderId;
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

    public boolean isHeal() {
        return isHeal;
    }

    public float getDamage() {
        return damage;
    }

    public int getSenderId() {
        return senderId;
    }
}
