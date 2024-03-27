package com.metrocre.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Entity {
    private static int id_counter = 0;

    protected int id;
    protected Texture texture;
    protected Body body;
    protected float width;
    protected float height;
    protected WorldManager worldManager;

    protected Entity(float width, float height, WorldManager worldManager, Texture texture) {
        id = id_counter++;
        this.width = width;
        this.height = height;
        this.worldManager = worldManager;
        this.texture = texture;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x - width / 2, body.getPosition().y - height / 2, width, height);
    }

    public Body getBody() {
        return body;
    }

    public void setVelocity(Vector2 velocity) {
        body.setLinearVelocity(velocity);
    }

    public void update(float delta) {

    }
}
