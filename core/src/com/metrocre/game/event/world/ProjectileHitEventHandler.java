package com.metrocre.game.event.world;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.enemies.Enemy;
import com.metrocre.game.weapons.Projectile;

public class ProjectileHitEventHandler implements Telegraph {

    @Override
    public boolean handleMessage(Telegram msg) {
        ProjectileHitEventData data = (ProjectileHitEventData) msg.extraInfo;
        Projectile projectile = data.projectile;
        if (data.hittedObject instanceof Enemy) {
            Enemy enemy = (Enemy) data.hittedObject;
            enemy.takeDamage(projectile.getDamage(), projectile.getSender());
            projectile.destroy();
        } else if (data.hittedObject instanceof TiledMapTileLayer.Cell) {
            projectile.destroy();
        } else if (data.hittedObject instanceof Player) {
            Player player = (Player) data.hittedObject;
            player.takeDamage(projectile.getDamage(), projectile.getSender());
            projectile.destroy();
        }
        return true;
    }
}
