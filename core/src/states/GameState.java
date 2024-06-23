
package states;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.Map;
import com.metrocre.game.towers.TowerPlace;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.WorldManager;
import com.metrocre.game.world.enemies.Enemy;
import com.metrocre.game.weapons.Projectile;
import com.metrocre.game.world.enemies.EnemySpawner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    private MapState mapState;
    private PlayerState playerState;
    private List<EnemyState> enemies;
    private List<SpawnerState> spawners;

    private List<TowerPlace> HealTowerPlaces;
    private List<TowerPlace> GunTowerPlaces;
    private List<Vector2> bulletPositions; // #TODO change to bulletStates

    public GameState(Player player, WorldManager worldManager, Map map) {
        this.mapState = new MapState(map);
        this.playerState = new PlayerState(player);
        this.enemies = new ArrayList<>();
        for (Enemy enemy : worldManager.getEnemies()) {
            if (enemy.isDestroyed()) {
                continue;
            }
            enemies.add(new EnemyState(enemy));
        }
        this.spawners = new ArrayList<>();
        for (EnemySpawner spawner : worldManager.getSpawners()) {
            if (spawner.isDestroyed()) {
                continue;
            }
            spawners.add(new SpawnerState(spawner));
        }
        this.HealTowerPlaces = worldManager.getHealTowerPlaces();
        this.GunTowerPlaces = worldManager.getGunTowerPlaces();

        this.bulletPositions = new ArrayList<>();
        for (Projectile bullet : worldManager.getProjectiles()) {
            this.bulletPositions.add(new Vector2(bullet.getX(), bullet.getY()));
        }
    }

    public PlayerState getPlayerState() {
        return playerState;
    }



    public List<Vector2> getBulletPositions() {
        return bulletPositions;
    }

    public MapState getMapState() {
        return mapState;
    }

    public List<EnemyState> getEnemyStates() {
        return enemies;
    }
    public List<SpawnerState> getSpawnersStates() {
        return spawners;
    }

    public List<TowerPlace> getHealTowerPlaces() {
        return HealTowerPlaces;
    }
    public List<TowerPlace> getGunTowerPlaces() {
        return GunTowerPlaces;
    }
}