package com.metrocre.game.towers;

import static com.metrocre.game.MyGame.SCALE;

import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.world.EntityData;
import com.metrocre.game.world.EntityType;
import com.metrocre.game.world.WorldManager;

import java.io.Serializable;

public class TowerPlace implements Serializable {
    private int x;
    private int y;
    private boolean isOccupied;
    private final int type;

    public TowerPlace(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.isOccupied = false;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getType() {
        return type;
    }

    public int getY() {
        return y;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void placeTower(int playerId, int towerType, WorldManager worldManager) {
        EntityData.TowerData towerData = new EntityData.TowerData();
        towerData.x = x + SCALE / 2f;
        towerData.y = y + SCALE / 2f;
        towerData.type = towerType;
        towerData.playerId = playerId;
        isOccupied = true;
        WorldEvents.AddEntity addEntity = new WorldEvents.AddEntity();
        addEntity.type = EntityType.Tower;
        addEntity.data = towerData;
        worldManager.getMessageDispatcher().dispatchMessage(WorldEvents.AddEntity.ID, addEntity);
    }
}
