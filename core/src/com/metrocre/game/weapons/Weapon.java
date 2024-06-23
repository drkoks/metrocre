package com.metrocre.game.weapons;

import static java.lang.Math.max;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.MyGame;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.ProjectileManager;

public abstract class Weapon {
    protected float fireRate;
    protected float cooldown;
    protected Texture texture;
    protected Entity owner;
    protected float width = -1;
    protected float height = -1;
    protected ProjectileManager projectileManager;

    public Weapon(Entity holder, float fireRate, ProjectileManager projectileManager, Texture texture) {
        this.owner = holder;
        this.fireRate = fireRate;
        this.projectileManager = projectileManager;
        this.texture = texture;
    }
    public Weapon(Entity holder, float fireRate, ProjectileManager projectileManager, Texture texture, float width, float height) {
        this(holder, fireRate, projectileManager, texture);
        this.width = width;
        this.height = height;
    }

    public abstract boolean shoot(Vector2 direction);

    public void update(float delta) {
        cooldown = max(cooldown - delta, 0);
    }

    public void draw(Batch batch) {
        if (width != -1 && height != -1) {
            batch.draw(texture,
                    owner.getBody().getPosition().x - width / 2,
                    owner.getBody().getPosition().y - height / 2,
                    width,
                    height);
        } else {
            batch.draw(texture,
                    owner.getBody().getPosition().x - texture.getWidth() * MyGame.UNIT_SCALE / 2,
                    owner.getBody().getPosition().y - texture.getHeight() * MyGame.UNIT_SCALE / 2,
                    texture.getWidth() * MyGame.UNIT_SCALE,
                    texture.getHeight() * MyGame.UNIT_SCALE);
        }
    }
}
