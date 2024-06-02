package com.metrocre.game.world.enemies;

import static com.metrocre.game.MyGame.SCALE;

import com.metrocre.game.world.Player;
import com.metrocre.game.world.WorldManager;

public class Enemy1 extends Enemy{

    public Enemy1(float x, float y, WorldManager worldManager) {
        super(x, y, 3, 100, 10, SCALE, worldManager, "enemy1");
    }

}
