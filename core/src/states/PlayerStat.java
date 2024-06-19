package states;

import java.util.HashMap;
import java.util.Map;

public class PlayerStat {
    Map<String, Integer> kills =new HashMap<>();

    public void addKill(String enemyType) {
        if (kills.containsKey(enemyType)) {
            kills.put(enemyType, kills.get(enemyType) + 1);
        } else {
            kills.put(enemyType, 1);
        }
    }

    public Map<String, Integer> getKills() {
        return kills;
    }
}
