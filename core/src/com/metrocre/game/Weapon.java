package com.metrocre.game;

import static java.lang.Math.max;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Weapon {
    protected float fireRate;
    protected float cooldown;
    protected Texture texture;
    protected Player player;
    protected ProjectileManager projectileManager;

    public Weapon(Player player, float fireRate, ProjectileManager projectileManager, Texture texture) {
        this.player = player;
        this.fireRate = fireRate;
        this.projectileManager = projectileManager;
        this.texture = texture;
    }

    public abstract boolean shoot(Vector2 direction);

    public void update(float delta) {
        cooldown = max(cooldown - delta, 0);
    }

    public void draw(Batch batch) {
        batch.draw(texture,
                player.getBody().getPosition().x - texture.getWidth() * MyGame.UNIT_SCALE / 2,
                player.getBody().getPosition().y - texture.getHeight() * MyGame.UNIT_SCALE / 2,
                texture.getWidth() * MyGame.UNIT_SCALE,
                texture.getHeight() * MyGame.UNIT_SCALE);
    }
}
