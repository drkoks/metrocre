package com.metrocre.game;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class WorldManager {
    private final World world;
    private final MessageDispatcher messageDispatcher;

    public WorldManager(World world) {
        this.world = world;
        messageDispatcher = new MessageDispatcher();
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object data1 = contact.getFixtureA().getBody().getUserData();
                System.out.println(data1);
                Object data2 = contact.getFixtureB().getBody().getUserData();
                System.out.println(data2);
                if (data1 == null || data2 == null) {
                    return;
                }
                System.out.println(data1.getClass() + " " + data2.getClass());
                if (data2 instanceof Bullet) {
                    Object tmp = data1;
                    data1 = data2;
                    data2 = tmp;
                }
                if (data1 instanceof Bullet) {
                    Bullet bullet = (Bullet) data1;
                    if (data2 instanceof Enemy) {
                        Enemy enemy = (Enemy) data2;
                        messageDispatcher.dispatchMessage(null, enemy, Messages.HIT);
                        messageDispatcher.dispatchMessage(null, bullet, Messages.HIT);
                    } else if (data2 instanceof TiledMapTileLayer.Cell) {
                        messageDispatcher.dispatchMessage(null, bullet, Messages.HIT);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public World getWorld() {
        return world;
    }

    public Body createCircleBody(float x, float y, float radius, boolean isSensor, boolean isBullet, Object userData) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.fixedRotation = true;
        bd.position.set(new Vector2(x, y));
        bd.bullet = isBullet;
        Body body = world.createBody(bd);
        body.setUserData(userData);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.friction = 0;
        fd.isSensor = isSensor;

        Fixture fixture = body.createFixture(fd);
        fixture.setUserData(userData);

        shape.dispose();

        return body;
    }

    public Body createRectangleBody(float x, float y, float width, float height, Object userData) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.fixedRotation = true;
        bd.position.set(new Vector2(x, y));
        Body body = world.createBody(bd);
        body.setUserData(userData);

        PolygonShape shape = new PolygonShape();
        shape.set(new float[]{0, 0, 0, height, width, height, width, 0});

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.friction = 0;

        Fixture fixture = body.createFixture(fd);

        shape.dispose();

        return body;
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    public void dispose() {
        world.dispose();
    }


}
