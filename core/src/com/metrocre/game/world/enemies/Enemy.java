package com.metrocre.game.world.enemies;

import static com.metrocre.game.MyGame.SCALE;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;

import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.network.Network;
import com.metrocre.game.towers.Tower;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.RayCastResult;
import com.metrocre.game.world.WorldManager;

import java.util.List;

import states.EnemyState;

public abstract class Enemy extends Entity {
    public static final float SIZE = SCALE;
    protected float health;
    protected int reward;
    protected float attackPower;
    protected float attackRange;
    protected float detectionRange;
    protected float cooldown = 0;
    protected Player targetPlayer;
    float speed = 20.0f;
    private Vector2 position;

    public Enemy(float x, float y, float health, int reward, float attackPower, float attackRange, float detectionRange, WorldManager worldManager, String enemyKind) {
        super(worldManager, worldManager.getTexture(enemyKind), SIZE, SIZE);
        this.attackPower = attackPower;
        this.detectionRange = detectionRange;
        this.attackRange = attackRange;
        this.health = health;
        this.reward = reward;
        this.position = new Vector2();
        body = worldManager.createCircleBody(x, y, SIZE / 2, false, false, this);
    }


    public void update(float deltaTime) {
        targetPlayer = getTarget();
        if (targetPlayer == null) {
            patrol();
            return;
        }
        cooldown = max(cooldown - deltaTime, 0);
        moveTowardsPlayer();
        if (isPlayerInRange(targetPlayer)) {
            attackPlayer(targetPlayer);
        }
    }

    public float getHealth() {
        return health;
    }

    private Player getTarget() {
        List<Entity> entitiesInRadius = worldManager.getEntitiesInRadius(body.getPosition(), detectionRange);
        Player target = null;
        float distance = detectionRange;
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

    private void patrol() {
        body.setLinearVelocity(Vector2.Zero);
    }

    private void moveTowardsPlayer() {
        Vector2 playerPosition = targetPlayer.getPosition();
        Vector2 direction = new Vector2(playerPosition).sub(this.body.getPosition()).nor();

        body.setLinearVelocity(direction.scl(5 + (speed - 1) * 2));
    }

    public void takeDamage(float damage, int senderId) {
        if (health == 0) {
            return;
        }
        health = (int) max(0f, health - damage);
        if (health == 0) {
            if (worldManager.getServer() != null) {
                destroy();
            }
            Player player = null;
            Entity sender = worldManager.getEntity(senderId);
            if (sender instanceof Player) {
                player = (Player) sender;
            } else if (sender instanceof Tower) {
                player = ((Tower) sender).getPlayer();
            }
            if (player != null) {
                player.getPlayersProfile().reportKill(this);
                player.addMoney(reward);
            }
        }
        if (worldManager.getServer() != null) {
            Network.TakeDamage takeDamage = new Network.TakeDamage();
            takeDamage.senderId = senderId;
            takeDamage.receiverId = id;
            takeDamage.damage = damage;
            worldManager.getServer().packToSend(takeDamage);
        }
    }

    private boolean isPlayerInRange(Player player) {
        float distance = (float) sqrt((player.getX() - getX()) * (player.getX() - getX()) +
                (player.getY() - getY()) * (player.getY() - getY()));
        return distance <= attackRange;
    }



    public float getAttack() {
        return attackPower;
    }

    protected abstract void attackPlayer(Player player);

    abstract public String getCoolName();
}
