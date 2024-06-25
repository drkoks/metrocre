package com.metrocre.game.event.world;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.Worm;
import com.metrocre.game.world.WorldManager;
import com.metrocre.game.world.enemies.Enemy;
import com.metrocre.game.weapons.Projectile;

public class ProjectileHitEventHandler implements Telegraph {
    private WorldManager worldManager;

    public ProjectileHitEventHandler(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        ProjectileHitEventData data = (ProjectileHitEventData) msg.extraInfo;
        Projectile projectile = (Projectile) worldManager.getEntity(data.projectileId);
        if (projectile.isDestroyed()) {
            return true;
        }
        if (data.hittedObject instanceof TiledMapTileLayer.Cell) {
            if (worldManager.getServer() != null) {
                projectile.destroy();
            }
        } else if (data.hittedObject instanceof Enemy) {
            Enemy enemy = (Enemy) data.hittedObject;
            if (enemy.isDestroyed()) {
                return true;
            }
            enemy.takeDamage(projectile.getDamage(), projectile.getSenderId());
            if (worldManager.getServer() != null) {
                projectile.destroy();
            }
        } else if (data.hittedObject instanceof Player) {
            Player player = (Player) data.hittedObject;
            if (player.isDestroyed()) {
                return true;
            }
            if (projectile.isHeal()) {
                player.heal(projectile.getDamage());
            } else {
                player.takeDamage(projectile.getDamage(), projectile.getSenderId());
            }
            if (worldManager.getServer() != null) {
                projectile.destroy();
            }
        } else if (data.hittedObject instanceof Worm) {
            Worm worm = (Worm) data.hittedObject;
            worm.takeDamage(projectile.getDamage(), projectile.getSenderId());
            if (worldManager.getServer() != null) {
                projectile.destroy();
            }
        }
        return true;
    }
}
