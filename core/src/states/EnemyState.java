package states;

import com.metrocre.game.world.WorldManager;
import com.metrocre.game.world.enemies.Enemy;
import com.metrocre.game.world.enemies.Enemy1;
import com.metrocre.game.world.enemies.Enemy2;

import java.io.Serializable;

public class EnemyState implements Serializable {
    private float enemyX;
    private float enemyY;
    private int enemyType;
    private float health;


    public EnemyState(Enemy enemy) {

        this.enemyX = enemy.getX();
        this.enemyY = enemy.getY();
        this.enemyType = enemy.getClass() == Enemy1.class ? 1 : 2;
        this.health = enemy.getHealth();
    }

    public float getEnemyX() {
        return enemyX;
    }

    public float getEnemyY() {
        return enemyY;
    }


    public float getHealth() {
        return health;
    }


    public Enemy getEnemyFromState(WorldManager worldManager) {
        if (enemyType == 1) {
            return new Enemy1(enemyX, enemyY, health, worldManager);
        } else {
            return new Enemy2(enemyX, enemyY, health, worldManager);
        }
    }
}
