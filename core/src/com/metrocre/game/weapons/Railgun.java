package com.metrocre.game.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.ProjectileManager;

public class Railgun extends Weapon {
    private final float range = 10;

    public Railgun(Entity owner, ProjectileManager projectileManager, Texture texture) {
        super(owner, 0, projectileManager, texture);
    }

    public Railgun(Entity owner, ProjectileManager projectileManager, Texture texture, float width, float height) {
        super(owner, 0, projectileManager, texture, width, height);
    }

    @Override
    public boolean shoot(Vector2 direction) {
        if (cooldown > 0 || direction.len() == 0) {
            return false;
        }
        cooldown = fireRate;
        projectileManager.createRail(owner.getBody().getPosition(), direction, range, 1f / 20, owner);
        return true;
    }
}
