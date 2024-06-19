package com.metrocre.game.towers;

import com.metrocre.game.weapons.HealGun;
import com.metrocre.game.world.enemies.Enemy;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.RayCastResult;
import com.metrocre.game.world.WorldManager;
import com.metrocre.game.weapons.Pistol;

import java.util.List;

public class HealTower extends Tower {
    private final float detectRadius;
    private HealGun gun;

    public HealTower(float x, float y, float health, float detectRadius, WorldManager worldManager, Player player, String texture) {
        super(x, y, health, worldManager, player, worldManager.getTexture(texture));
        gun = new HealGun(this, worldManager.getProjectileManager(), null);
        this.detectRadius = detectRadius;
    }

    public void update(float delta) {
        Player target = getTarget();
        if (target == null) {
            return;
        }
        gun.update(delta);
        gun.shoot(target.getBody().getPosition().cpy().sub(body.getPosition()).setLength(1));
    }

    private Player getTarget() {
        List<Entity> entitiesInRadius = worldManager.getEntitiesInRadius(body.getPosition(), detectRadius);
        Player target = null;
        float distance = detectRadius;
        for (Entity entity : entitiesInRadius) {
            float thisDistance = entity.getBody().getPosition().cpy().sub(body.getPosition()).len();
            if (!(entity instanceof Player) || thisDistance > distance) {
                continue;
            }
            RayCastResult rayCastResult = worldManager.castRay(body.getPosition(), entity.getBody().getPosition());
            if (rayCastResult.hitPointFraction == 1) {
                target = (Player) entity;
                distance = thisDistance;
            }
        }
        return target;
    }
}
