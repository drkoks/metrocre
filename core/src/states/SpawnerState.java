package states;

import com.metrocre.game.world.WorldManager;

import com.metrocre.game.world.enemies.EnemySpawner;

import java.io.Serializable;

public class SpawnerState implements Serializable {
    private int x;
    private int y;
    private int enemyType;
    private float spawnTimer;
    private int spawnAmount;

    public SpawnerState(EnemySpawner spawner) {
        this.x = spawner.getX();
        this.y = spawner.getY();
        this.enemyType = spawner.getEnemyType();
        this.spawnAmount = spawner.getSpawnAmount();
        this.spawnTimer = spawner.getSpawnTimer();

    }

    public EnemySpawner getSpawnerFromState(WorldManager worldManager) {
        return new EnemySpawner(worldManager, x, y, enemyType, spawnAmount, spawnTimer);
    }
}
