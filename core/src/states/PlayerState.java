package states;

import com.metrocre.game.world.Player;

import java.io.Serializable;

public class PlayerState implements Serializable {
    private float playerX;
    private float playerY;
    private int health;
    private boolean isDamaged;
    private float damageTime;
    public PlayerState(Player player) {
        playerX = player.getX();
        playerY = player.getY();
        health = player.getHealth();
        isDamaged = player.isDamaged();
        damageTime = player.getDamageTime();
    }

    public float getPlayerX() {
        return playerX;
    }
    public float getPlayerY() {
        return playerY;
    }

    public int getHealth() {
        return health;
    }

    public boolean isDamaged() {
        return isDamaged;
    }
    public float getDamageTime() {
        return damageTime;
    }

}
