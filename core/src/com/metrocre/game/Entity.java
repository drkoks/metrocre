package com.metrocre.game;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Entity implements Telegraph {
    private static int id_counter = 0;

    protected int id;
    protected Texture texture;
    protected Body body;
    protected WorldManager worldManager;
    protected boolean destroyed = false;

    protected Entity(WorldManager worldManager, Texture texture) {
        id = id_counter++;
        this.worldManager = worldManager;
        this.texture = texture;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x - texture.getWidth() * MyGame.UNIT_SCALE / 2, body.getPosition().y - texture.getHeight() * MyGame.UNIT_SCALE / 2, texture.getWidth() * MyGame.UNIT_SCALE, texture.getHeight() * MyGame.UNIT_SCALE);
    }

    public Body getBody() {
        return body;
    }

    public void setVelocity(Vector2 velocity) {
        body.setLinearVelocity(velocity);
    }

    public void update(float delta) {

    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
