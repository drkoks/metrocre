package com.metrocre.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import java.util.ArrayList;
import java.util.List;

public class Railgun extends Weapon {
    private float range = 10;

    public Railgun(Player player, ProjectileManager projectileManager, Texture texture) {
        super(player, 0.1f, projectileManager, texture);
    }

    @Override
    public boolean shoot(Vector2 direction) {
        if (cooldown > 0 || direction.len() == 0) {
            return false;
        }
        cooldown = fireRate;
        projectileManager.createRail(player.getBody().getPosition(), direction, range);
        return true;
    }
}
