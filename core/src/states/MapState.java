package states;

import com.metrocre.game.Map;
import com.metrocre.game.world.Player;

import java.io.Serializable;

public class MapState implements Serializable {
    private int mapID;
    public MapState(Map map) {
        mapID = map.getMapID();
    }

    public int getMapID() {
        return mapID;
    }
}
