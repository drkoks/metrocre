package com.metrocre.game.world;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.Texture;
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
import com.metrocre.game.event.world.ProjectileHitEventData;
import com.metrocre.game.event.world.ProjectileHitEventHandler;
import com.metrocre.game.event.world.RailHitEventHandler;
import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.towers.Tower;
import com.metrocre.game.weapons.Projectile;
import com.metrocre.game.world.enemies.Enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WorldManager {
    private final World world;
    private final MessageDispatcher messageDispatcher = new MessageDispatcher();
    private Player player;
    private IntMap<Entity> entities = new IntMap<>();
    private Map<String, Texture> textures = new HashMap<>();
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
                    if (bullet.getSender() instanceof Enemy && data2 instanceof Enemy) {
                        return;
                    }
                    if ((bullet.getSender() instanceof Tower || bullet.getSender() instanceof Player) && data2 instanceof Player) {
                        return;
                    }
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
        for (Iterator<IntMap.Entry<Entity>> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next().value;
            if (entity.isDestroyed()) {
                world.destroyBody(entity.getBody());
                it.remove();
            }
            entity.update(delta);
        }
    }

    public void addEntity(Entity entity) {
        if (entity instanceof Player) {
            this.player = (Player) entity;
        }
        entities.put(entity.getId(), entity);
    }

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        List<Enemy> enemies = new ArrayList<>();
        for (Entity entity : entities.values()) {
            if (entity instanceof Enemy) {
                enemies.add((Enemy) entity);
            }
        }
        return enemies;
    }

    public List<Projectile> getProjectiles() {
        List<Projectile> projectiles = new ArrayList<>();
        for (Entity entity : entities.values()) {
            if (entity instanceof Projectile) {
                projectiles.add((Projectile) entity);
            }
        }
        return projectiles;
    }

    public void drawWorld(SpriteBatch batch) {
        for (IntMap.Entry<Entity> entityEntry : entities) {
            Entity entity = entityEntry.value;
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


    public void addTexture(Texture texture, String name) {
        textures.put(name, texture);
    }

    public Texture getTexture(String name) {
        return textures.get(name);
    }
}
