package com.metrocre.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.metrocre.game.MyGame;
import com.metrocre.game.network.Network;

public abstract class Entity {
    protected int id;
    protected Texture texture;
    protected Body body;
    protected float width = -1;
    protected float height = -1;
    protected WorldManager worldManager;
    protected boolean destroyed = false;

    protected Entity(WorldManager worldManager, Texture texture) {
        id = worldManager.nextEntityCnt();
        this.worldManager = worldManager;
        this.texture = texture;
    }

    protected Entity(WorldManager worldManager, Texture texture, float width, float height) {
        this(worldManager, texture);
        this.width = width;
        this.height = height;
    }

    public void draw(SpriteBatch batch) {
        if (width != -1 && height != -1) {
            batch.draw(texture, body.getPosition().x - width / 2, body.getPosition().y - height / 4, width, height);
        } else {
            batch.draw(texture, body.getPosition().x - texture.getWidth() * MyGame.UNIT_SCALE / 2,
                    body.getPosition().y - texture.getHeight() * MyGame.UNIT_SCALE / 2, texture.getWidth() * MyGame.UNIT_SCALE, texture.getHeight() * MyGame.UNIT_SCALE);
        }
    }


    public Body getBody() {
        return body;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public void setVelocity(Vector2 velocity) {
        body.setLinearVelocity(velocity);
    }

    public void update(float delta) {

    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
        if (worldManager.getServer() != null) {
            Network.DestroyEntity destroyEntity = new Network.DestroyEntity();
            destroyEntity.id = id;
            worldManager.getServer().packToSend(destroyEntity);
        }
    }
}
