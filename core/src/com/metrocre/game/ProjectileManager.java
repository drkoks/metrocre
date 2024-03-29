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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class ProjectileManager {
    private List<Rail> rails = new LinkedList<>();
    private List<Bullet> bullets = new LinkedList<>();
    private WorldManager worldManager;
    private Texture texture;
    private TextureRegion region;
    private Texture bulletTexture = new Texture("bullet.png");

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
        for (Iterator<Bullet> it = bullets.iterator(); it.hasNext(); ) {
            Bullet bullet = it.next();
            if (bullet.isDestroyed()) {
                worldManager.getWorld().destroyBody(bullet.getBody());
                it.remove();
            } else {
                bullet.update(delta);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        ShapeDrawer shapeDrawer = new ShapeDrawer(batch, region);
        shapeDrawer.setDefaultLineWidth(MyGame.UNIT_SCALE);
        for (Rail rail : rails) {
            rail.draw(shapeDrawer);
        }
        for (Bullet bullet : bullets) {
            bullet.draw(shapeDrawer);
        }
    }

    public void createRail(Vector2 position, Vector2 direction, float len) {
        class MyRayCastCallback implements RayCastCallback {
            List<Enemy> hitted = new ArrayList<>();
            List<Float> fractions = new ArrayList<>();
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
        Rail rail = new Rail(position.cpy(), rayCastCallback.hitPoint);
        rails.add(rail);
        for (int i = 0; i < rayCastCallback.fractions.size(); i++) {
            if (rayCastCallback.fractions.get(i) < rayCastCallback.hitPointFraction) {
                worldManager.getMessageDispatcher().dispatchMessage(null, rayCastCallback.hitted.get(i), Messages.HIT);
            }
        }
    }

    public void createBullet(Vector2 position, Vector2 direction, float speed) {
        Bullet bullet = new Bullet(position, direction, speed, worldManager, bulletTexture);
        bullets.add(bullet);
    }

    public void dispose() {
        texture.dispose();
    }
}
