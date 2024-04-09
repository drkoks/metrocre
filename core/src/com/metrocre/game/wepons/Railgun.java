package com.metrocre.game.wepons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.Player;
import com.metrocre.game.ProjectileManager;
import com.metrocre.game.wepons.Weapon;

public class Railgun extends Weapon {
    private final float range = 10;

    public Railgun(Player player, ProjectileManager projectileManager, Texture texture) {
        super(player, 0, projectileManager, texture);
    }

    @Override
    public boolean shoot(Vector2 direction) {
        if (cooldown > 0 || direction.len() == 0) {
            return false;
        }
        cooldown = fireRate;
        projectileManager.createRail(player.getBody().getPosition(), direction, range, 1f / 20, player);
        return true;
    }
}
