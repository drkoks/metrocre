package com.metrocre.game.world.enemies;

import static com.metrocre.game.MyGame.SCALE;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;

import com.badlogic.gdx.graphics.Texture;
import com.metrocre.game.towers.Tower;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.WorldManager;

public abstract class Enemy extends Entity {
    public static final float SIZE = SCALE;
    protected float health;
    protected int reward;
    protected float attackPower;
    protected float attackRange;
    protected float cooldown = 0;

    public Enemy(float x, float y, float health, int reward, float attackPower, float attackRange, WorldManager worldManager, String enemyKind) {
        super(worldManager, worldManager.getTexture(enemyKind), SIZE, SIZE);
        this.attackPower = attackPower;
        this.attackRange = attackRange;
        this.health = health;
        this.reward = reward;
        body = worldManager.createCircleBody(x, y, SIZE / 2, false, false, this);
    }



    public void update(float deltaTime) {
        Player player = worldManager.getPlayer();
        cooldown = max(cooldown - deltaTime, 0);
        if (player != null && isPlayerInRange(player)) {
            attackPlayer(player);
        }
    }

    public void takeDamage(float damage, Entity sender) {
        if (health == 0) {
            return;
        }
        health = (int) max(0f, health - damage);
        if (health == 0) {
            destroy();
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else if (sender instanceof Tower) {
                player = ((Tower) sender).getPlayer();
            }
            if (player != null) {
                player.addMoney(reward);
            }
        }
    }

    private boolean isPlayerInRange(Player player) {
        float distance = (float) sqrt((player.getX() - getX()) * (player.getX() - getX()) +
                (player.getY() - getY()) * (player.getY() - getY()));
        return distance <= attackRange;
    }

    protected abstract void attackPlayer(Player player);

    public float getAttack() {
        return attackPower;
    }


}
