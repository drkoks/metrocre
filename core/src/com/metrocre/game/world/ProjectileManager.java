package com.metrocre.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.metrocre.game.MyGame;
import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.event.world.RailHitEventData;
import com.metrocre.game.towers.Tower;
import com.metrocre.game.weapons.Projectile;
import com.metrocre.game.weapons.Rail;
import com.metrocre.game.world.enemies.Enemy;

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
    private final Texture healBulletTexture = new Texture("healBullet.png");

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

    public void createRail(Vector2 position, Vector2 direction, float len, float damage, Entity owner) {
        RayCastResult rayCastResult = worldManager.castRay(position.cpy(), position.cpy().add(direction.scl(len)));
        Rail rail = new Rail(position.cpy(), rayCastResult.hitPoint, damage, owner);
        rails.add(rail);
        for (int i = 0; i < rayCastResult.fractions.size(); i++) {
            if (rayCastResult.fractions.get(i) < rayCastResult.hitPointFraction) {
                RailHitEventData railHitEventData = new RailHitEventData();
                railHitEventData.rail = rail;
                railHitEventData.hittedObject = rayCastResult.hitted.get(i);
                worldManager.getMessageDispatcher().dispatchMessage(WorldEvents.RAIL_HIT, railHitEventData);
            }
        }
    }

    public void createBullet(Vector2 position, Vector2 direction, float speed, float damage, Entity owner, boolean isHeal) {
        Projectile bullet = new Projectile(position, direction, damage, speed, worldManager,
                isHeal ? healBulletTexture : bulletTexture, owner, isHeal);

        worldManager.addEntity(bullet);
    }

    public void dispose() {
        texture.dispose();
    }
}
