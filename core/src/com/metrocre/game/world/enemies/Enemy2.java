package com.metrocre.game.world.enemies;

import static com.metrocre.game.MyGame.SCALE;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.weapons.Pistol;
import com.metrocre.game.weapons.Weapon;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.RayCastResult;
import com.metrocre.game.world.WorldManager;

import java.util.List;

public class Enemy2 extends Enemy {
    private Weapon weapon;
    float detectRadius = SCALE*5;

    public Enemy2(float x, float y, WorldManager worldManager) {
        super(x, y, 3, 100, 10, SCALE, 8*SCALE,  worldManager, "enemy2");
        setWeapon(new Pistol(this, worldManager.getProjectileManager(), new Texture("pistol.png"), 0.6F * SCALE, 0.4F * SCALE, 1));
    }
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void shoot(Vector2 direction) {
        if (weapon == null) {
            return;
        }
        weapon.shoot(direction);
    }
    private Entity getTarget() {

        List<Entity> entitiesInRadius = worldManager.getEntitiesInRadius(body.getPosition(), detectRadius);
        Player target = null;
        float distance = detectRadius;
        for (Entity entity : entitiesInRadius) {
            float thisDistance = entity.getBody().getPosition().cpy().sub(body.getPosition()).len();
            if (!(entity instanceof Player) || thisDistance > distance) {
                continue;
            }
            RayCastResult rayCastResult = worldManager.castRay(body.getPosition(), entity.getBody().getPosition());
            if (rayCastResult.hitPointFraction == 1) {
                target = (Player) entity;
                distance = thisDistance;
            }
        }
        return target;
    }
    @Override
    public void update(float delta) {
        super.update(delta);
        Entity target = getTarget();
        if (target == null) {
            return;
        }
        weapon.update(delta);
        weapon.shoot(target.getBody().getPosition().cpy().sub(body.getPosition()).setLength(1));
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    protected void attackPlayer(Player player) {
    }
    @Override
    public String getCoolName() {
        return "Chompzilla";
    }

}