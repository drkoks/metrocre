package com.metrocre.game.world.enemies;

import com.metrocre.game.world.WorldManager;

public class EnemySpawner {
    private final WorldManager worldManager;
    private int x;
    private int y;
    private int enemyType;
    private float spawnTimer = 0;
    private boolean isDestroyed = false;
    private int spawnAmount = 0;

    public EnemySpawner(WorldManager worldManager, int x, int y, int enemyType, int spawnAmount, float spawnTimer) {
        this.worldManager = worldManager;
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.spawnAmount = spawnAmount;
        this.spawnTimer = spawnTimer;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void update(float deltaTime) {
        if (spawnAmount == 0) {
            isDestroyed = true;
        }
        spawnTimer -= deltaTime;
        if (spawnTimer < 0) {
            spawnEnemy();
            spawnTimer = 5f;
        }
    }

    private void spawnEnemy() {
        switch (enemyType) {
            case 1:
                worldManager.addEntity(new Enemy1(x, y, worldManager));
                break;
            case 2:
                worldManager.addEntity(new Enemy2(x, y, worldManager));
                break;
        }
        spawnAmount--;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getSpawnAmount() {
        return spawnAmount;
    }

    public float getSpawnTimer() {
        return spawnTimer;
    }
}
