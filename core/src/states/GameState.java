
package states;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.Map;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.WorldManager;
import com.metrocre.game.world.enemies.Enemy;
import com.metrocre.game.weapons.Projectile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    private MapState mapState;
    private PlayerState playerState;
    private List<Vector2> enemyPositions; // #TODO change to enemyStates
    private List<Vector2> bulletPositions; // #TODO change to bulletStates

    public GameState(Player player, WorldManager worldManager, Map map) {
        this.mapState = new MapState(map);
        this.playerState = new PlayerState(player);
        this.enemyPositions = new ArrayList<>();
        for (Enemy enemy : worldManager.getEnemies()) {
            if (!enemy.isDestroyed()) {
                this.enemyPositions.add(new Vector2(enemy.getX(), enemy.getY()));
            }
        }

        this.bulletPositions = new ArrayList<>();
        for (Projectile bullet : worldManager.getProjectiles()) {
            this.bulletPositions.add(new Vector2(bullet.getX(), bullet.getY()));
        }
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public List<Vector2> getEnemyPositions() {
        return enemyPositions;
    }

    public List<Vector2> getBulletPositions() {
        return bulletPositions;
    }

    public MapState getMapState() {
        return mapState;
    }
}