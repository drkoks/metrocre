package states;

import java.util.HashMap;
import java.util.Map;

public class PlayerStat {
    Map<String, Integer> kills = new HashMap<>();
    Map<String, Integer> newKills = new HashMap<>();

    public void addKill(String enemyType) {
        if (newKills.containsKey(enemyType)) {
            newKills.put(enemyType, newKills.get(enemyType) + 1);
        } else {
            newKills.put(enemyType, 1);
        }
    }


    public Map<String, Integer> getKills() {
        Map<String, Integer> buf = new HashMap<>(newKills);
        kills.putAll(newKills);
        newKills.clear();
        return buf;
    }

    public Map<String, Integer> getAllKills() {
        return kills;
    }
}
