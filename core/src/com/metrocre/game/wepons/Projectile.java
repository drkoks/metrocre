package com.metrocre.game.wepons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.WorldManager;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Projectile extends Entity {
    public static final float SIZE = 1f / 8;

    private float damage;
    private Entity sender;

    public Projectile(Vector2 position, Vector2 direction, float damage, float speed, WorldManager worldManager, Texture texture, Entity sender) {
        super(worldManager, texture);
        this.damage = damage;
        this.sender = sender;
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

    public Entity getSender() {
        return sender;
    }
}
