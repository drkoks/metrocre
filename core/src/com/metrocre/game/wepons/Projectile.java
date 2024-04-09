package com.metrocre.game.wepons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.metrocre.game.Entity;
import com.metrocre.game.Player;
import com.metrocre.game.WorldManager;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Projectile extends Entity {
    public static final float SIZE = 1f / 8;

    private float damage;
    private Player player;

    public Projectile(Vector2 position, Vector2 direction, float damage, float speed, WorldManager worldManager, Texture texture, Player player) {
        super(worldManager, texture);
        this.damage = damage;
        this.player = player;
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

    public float getDamage() {
        return damage;
    }

    public Player getPlayer() {
        return player;
    }
}
