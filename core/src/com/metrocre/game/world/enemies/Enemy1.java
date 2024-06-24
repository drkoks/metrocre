package com.metrocre.game.world.enemies;

import static com.metrocre.game.MyGame.SCALE;

import com.metrocre.game.world.Player;
import com.metrocre.game.world.WorldManager;

public class Enemy1 extends Enemy {

    public Enemy1(float x, float y, WorldManager worldManager) {
        super(x, y, 3, 100, 10, SCALE, 8* SCALE, worldManager, "enemy1");
    }

    public Enemy1(float x, float y, float health, WorldManager worldManager) {
        super(x, y, health, 100, 10, SCALE, 8* SCALE, worldManager, "enemy1");
    }

    @Override
    protected void attackPlayer(Player player) {
        if (cooldown > 0) {
            return;
        }
        cooldown = 1f;
        player.takeDamage(1, id);
    }

    @Override
    public String getCoolName() {
        return "Phasmax";
    }
}
