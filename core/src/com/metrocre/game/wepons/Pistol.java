package com.metrocre.game.wepons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.ProjectileManager;

public class Pistol extends Weapon {
    private float bulletSpeed = 7;

    public Pistol(Entity owner, ProjectileManager projectileManager, Texture texture) {
        super(owner, 0.3f, projectileManager, texture);
    }

    @Override
    public boolean shoot(Vector2 direction) {
        if (cooldown > 0 || direction.len() == 0) {
            return false;
        }
        cooldown = fireRate;
        projectileManager.createBullet(owner.getBody().getPosition(), direction, bulletSpeed, 1, owner);
        return true;
    }
}
