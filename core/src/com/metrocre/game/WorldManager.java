package com.metrocre.game;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class WorldManager {
    private World world;
    private MessageDispatcher messageDispatcher;

    public WorldManager(World world) {
        this.world = world;
        messageDispatcher = new MessageDispatcher();
    }

    public World getWorld() {
        return world;
    }

    public Body createCircleBody(float x, float y, float radius, Object userData) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.fixedRotation = true;
        bd.position.set(new Vector2(x, y));
        Body body = world.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.friction = 0;

        Fixture fixture = body.createFixture(fd);
        fixture.setUserData(userData);

        shape.dispose();

        return body;
    }

    public Body createRectBody(float x, float y, float width, float height, Object userData) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.fixedRotation = true;
        bd.position.set(new Vector2(x, y));
        Body body = world.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.set(new float[]{0, 0, 0, height, width, height, width, 0});

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.friction = 0;

        Fixture fixture = body.createFixture(fd);
        fixture.setUserData(userData);

        shape.dispose();

        return body;
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }
}
