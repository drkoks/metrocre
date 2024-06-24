package com.metrocre.game.weapons;

import static com.metrocre.game.MyGame.SCALE;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.ProjectileManager;

public class Railgun extends Weapon {
    private final float range = 10 * SCALE;

    public Railgun(Entity owner, ProjectileManager projectileManager, Texture texture, float width, float height, int level) {
        super(owner, 1, projectileManager, texture, width, height, level);
    }

    @Override
    public boolean shoot(Vector2 direction) {
        if (cooldown > 0 || direction.len() == 0) {
            return false;
        }
        cooldown = fireRate;
        projectileManager.createRail(owner.getBody().getPosition(), direction, range, 3, owner);
        return true;
    }
}
