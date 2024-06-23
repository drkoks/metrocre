package com.metrocre.game.weapons;

import static com.metrocre.game.MyGame.SCALE;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.ProjectileManager;

public class Pistol extends Weapon {
    private float bulletSpeed = 7*SCALE;

    public Pistol(Entity owner, ProjectileManager projectileManager, Texture texture) {
        super(owner, 0.3f, projectileManager, texture);
    }
    public Pistol(Entity player, ProjectileManager projectileManager, Texture texture, float width, float height, int level) {
        super(player, 0.3f, projectileManager, texture, width, height, level);
    }

    @Override
    public boolean shoot(Vector2 direction) {
        if (cooldown > 0 || direction.len() == 0) {
            return false;
        }
        cooldown = fireRate;
        projectileManager.createBullet(owner.getBody().getPosition(), direction, bulletSpeed, level, owner, false);
        return true;
    }
}
