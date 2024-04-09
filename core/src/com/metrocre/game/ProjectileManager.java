package com.metrocre.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.event.world.RailHitEventData;
import com.metrocre.game.wepons.Projectile;
import com.metrocre.game.wepons.Rail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class ProjectileManager {
    private final List<Rail> rails = new LinkedList<>();
    private final WorldManager worldManager;
    private final Texture texture;
    private final TextureRegion region;
    private final Texture bulletTexture = new Texture("bullet.png");

    public ProjectileManager(WorldManager worldManager) {
        this.worldManager = worldManager;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        texture = new Texture(pixmap);
        pixmap.dispose();
        region = new TextureRegion(texture, 0, 0, 1, 1);
    }

    public void update(float delta) {
        for (Iterator<Rail> it = rails.iterator(); it.hasNext(); ) {
            Rail rail = it.next();
            if (rail.isExpired()) {
                it.remove();
            } else {
                rail.update(delta);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        ShapeDrawer shapeDrawer = new ShapeDrawer(batch, region);
        shapeDrawer.setDefaultLineWidth(MyGame.UNIT_SCALE);
        for (Rail rail : rails) {
            rail.draw(shapeDrawer);
        }
    }

    public void createRail(Vector2 position, Vector2 direction, float len, float damage, Player player) {
        class MyRayCastCallback implements RayCastCallback {
            final List<Enemy> hitted = new ArrayList<>();
            final List<Float> fractions = new ArrayList<>();
            Vector2 hitPoint = position.cpy().add(direction.scl(len));
            float hitPointFraction = 1;

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                Object fixtureUserData = fixture.getBody().getUserData();
                if (fixtureUserData instanceof Enemy) {
                    hitted.add((Enemy) fixtureUserData);
                    fractions.add(fraction);
                    return 1;
                } else if (fixtureUserData instanceof TiledMapTileLayer.Cell) {
                    hitPoint = point;
                    hitPointFraction = fraction;
                    return fraction;
                }
                return -1;
            }
        }
        MyRayCastCallback rayCastCallback = new MyRayCastCallback();
        worldManager.getWorld().rayCast(rayCastCallback, position.cpy(), position.cpy().add(direction.scl(len)));
        Rail rail = new Rail(position.cpy(), rayCastCallback.hitPoint, damage, player);
        rails.add(rail);
        for (int i = 0; i < rayCastCallback.fractions.size(); i++) {
            if (rayCastCallback.fractions.get(i) < rayCastCallback.hitPointFraction) {
                RailHitEventData railHitEventData = new RailHitEventData();
                railHitEventData.rail = rail;
                railHitEventData.hittedObject = rayCastCallback.hitted.get(i);
                worldManager.getMessageDispatcher().dispatchMessage(WorldEvents.RAIL_HIT, railHitEventData);
            }
        }
    }

    public void createBullet(Vector2 position, Vector2 direction, float speed, float damage, Player player) {
        Projectile bullet = new Projectile(position, direction, damage, speed, worldManager, bulletTexture, player);
        worldManager.addEntity(bullet);
    }

    public void dispose() {
        texture.dispose();
    }
}
