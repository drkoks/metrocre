package com.metrocre.game.world;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.IntMap;
import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.event.world.ProjectileHitEventData;
import com.metrocre.game.event.world.ProjectileHitEventHandler;
import com.metrocre.game.event.world.RailHitEventHandler;
import com.metrocre.game.wepons.Projectile;
import com.metrocre.game.world.Enemy;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.ProjectileManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorldManager {
    private final World world;
    private final MessageDispatcher messageDispatcher = new MessageDispatcher();
    private IntMap<Entity> entities = new IntMap<>();
    private ProjectileManager projectileManager = new ProjectileManager(this);
    private ProjectileHitEventHandler projectileHitEventHandler = new ProjectileHitEventHandler();
    private RailHitEventHandler railHitEventHandler = new RailHitEventHandler();

    public WorldManager(World world) {
        this.world = world;
        messageDispatcher.addListener(projectileHitEventHandler, WorldEvents.PROJECTILE_HIT);
        messageDispatcher.addListener(railHitEventHandler, WorldEvents.RAIL_HIT);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object data1 = contact.getFixtureA().getBody().getUserData();
                Object data2 = contact.getFixtureB().getBody().getUserData();
                if (data1 == null || data2 == null) {
                    return;
                }
                if (data2 instanceof Projectile) {
                    Object tmp = data1;
                    data1 = data2;
                    data2 = tmp;
                }
                if (data1 instanceof Projectile) {
                    Projectile bullet = (Projectile) data1;
                    ProjectileHitEventData projectileHitEventData = new ProjectileHitEventData();
                    projectileHitEventData.projectile = bullet;
                    projectileHitEventData.hittedObject = data2;
                    messageDispatcher.dispatchMessage(WorldEvents.PROJECTILE_HIT, projectileHitEventData);
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

    public ProjectileManager getProjectileManager() {
        return projectileManager;
    }

    public void update(float delta) {
        projectileManager.update(delta);
        for (Iterator<IntMap.Entry<Entity>> it = entities.iterator(); it.hasNext();) {
            Entity entity = it.next().value;
            if (entity.isDestroyed()) {
                world.destroyBody(entity.getBody());
                it.remove();
            }
            entity.update(delta);
        }
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getId(), entity);
    }

    public void drawWorld(SpriteBatch batch) {
        for (Iterator<IntMap.Entry<Entity>> it = entities.iterator(); it.hasNext();) {
            Entity entity = it.next().value;
            entity.draw(batch);
        }
        projectileManager.draw(batch);
    }

    public RayCastResult castRay(Vector2 pos1, Vector2 pos2) {
        class MyRayCastCallback implements RayCastCallback {
            RayCastResult rayCastResult = new RayCastResult(pos2.cpy());

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                Object fixtureUserData = fixture.getBody().getUserData();
                if (fixtureUserData instanceof Entity) {
                    rayCastResult.hitted.add((Entity) fixtureUserData);
                    rayCastResult.fractions.add(fraction);
                    return 1;
                } else if (fixtureUserData instanceof TiledMapTileLayer.Cell) {
                    rayCastResult.hitPoint = point.cpy();
                    rayCastResult.hitPointFraction = fraction;
                    return fraction;
                }
                return -1;
            }
        }

        MyRayCastCallback rayCastCallback = new MyRayCastCallback();
        world.rayCast(rayCastCallback, pos1.cpy(), pos2.cpy());
        return rayCastCallback.rayCastResult;
    }

    public List<Entity> getEntitiesInRadius(Vector2 position, float radius) {
        List<Entity> result = new ArrayList<>();
        world.QueryAABB(fixture -> {
            if (fixture.getBody().getPosition().cpy().sub(position).len() <= radius
                && fixture.getBody().getUserData() instanceof Entity) {
                result.add((Entity) fixture.getBody().getUserData());
            }
            return true;
        }, position.x - radius, position.y - radius, position.x + radius, position.y + radius);
        return result;
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
        projectileManager.dispose();
    }


}