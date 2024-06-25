package com.metrocre.game.towers;

import static com.metrocre.game.MyGame.SCALE;

import com.badlogic.gdx.graphics.Texture;
import com.metrocre.game.world.Worm;
import com.metrocre.game.world.enemies.Enemy;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.RayCastResult;
import com.metrocre.game.world.WorldManager;
import com.metrocre.game.weapons.Pistol;

import java.util.List;

public class GunTower extends Tower {
    private Pistol gun;
    private float detectRadius;

    public GunTower(float x, float y, float health, float detectRadius, WorldManager worldManager, Player player, String texture) {
        super(x, y, health, worldManager, player, worldManager.getTexture(texture));
        gun = new Pistol(this, worldManager.getProjectileManager(), null);
        this.detectRadius = detectRadius;
    }

    public void update(float delta) {
        Entity target = getTarget();
        if (target == null) {
            return;
        }
        gun.update(delta);
        gun.shoot(target.getBody().getPosition().cpy().sub(body.getPosition()).setLength(1));
    }

    private Entity getTarget() {
        List<Entity> entitiesInRadius = worldManager.getEntitiesInRadius(body.getPosition(), detectRadius);
        Entity target = null;
        float distance = detectRadius;
        for (Entity entity : entitiesInRadius) {
            float thisDistance = entity.getBody().getPosition().cpy().sub(body.getPosition()).len();
            if (!(entity instanceof Enemy || entity instanceof Worm) || thisDistance > distance) {
                continue;
            }
            RayCastResult rayCastResult = worldManager.castRay(body.getPosition(), entity.getBody().getPosition());
            if (rayCastResult.hitPointFraction == 1) {
                target = entity;
                distance = thisDistance;
            }
        }
        return target;
    }
}
