package com.metrocre.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Pistol extends Weapon {
    private float bulletSpeed = 7;

    public Pistol(Player player, ProjectileManager projectileManager, Texture texture) {
        super(player, 0.3f, projectileManager, texture);
    }

    @Override
    public boolean shoot(Vector2 direction) {
        if (cooldown > 0 || direction.len() == 0) {
            return false;
        }
        cooldown = fireRate;
        projectileManager.createBullet(player.getBody().getPosition(), direction, bulletSpeed);
        return true;
    }
}
