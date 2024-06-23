package com.metrocre.game.towers;

import static com.metrocre.game.MyGame.SCALE;

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



    public void placeTower(int towerType, WorldManager worldManager) {
        Tower tower;
        if(towerType == 1) {
            tower = new GunTower(x , y , 5,
                    10 * SCALE, worldManager, worldManager.getPlayer(), "gunTower");
        } else if(towerType == 2) {
            tower = new HealTower(x , y , 5,
                    3 * SCALE, worldManager, worldManager.getPlayer(), "healTower");
        } else {
            return;
        }
        isOccupied = true;
        worldManager.addEntity(tower);
    }



}
