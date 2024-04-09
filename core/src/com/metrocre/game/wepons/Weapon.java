package com.metrocre.game.wepons;

import static java.lang.Math.max;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.MyGame;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.ProjectileManager;

public abstract class Weapon {
    protected float fireRate;
    protected float cooldown;
    protected Texture texture;
    protected Entity owner;
    protected ProjectileManager projectileManager;

    public Weapon(Entity holder, float fireRate, ProjectileManager projectileManager, Texture texture) {
        this.owner = holder;
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
                owner.getBody().getPosition().x - texture.getWidth() * MyGame.UNIT_SCALE / 2,
                owner.getBody().getPosition().y - texture.getHeight() * MyGame.UNIT_SCALE / 2,
                texture.getWidth() * MyGame.UNIT_SCALE,
                texture.getHeight() * MyGame.UNIT_SCALE);
    }
}
