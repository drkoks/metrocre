package com.metrocre.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.PlayersProfile;
import com.metrocre.game.wepons.Weapon;

public class Player extends Entity {
    public static final float SIZE = 1f;

    private Weapon weapon;
    private int speed;
    private final PlayersProfile playersProfile;
    private int defence;
    private int attack;

    public Player(float x, float y, WorldManager worldManager, Texture texture, PlayersProfile playersProfile) {
        super(worldManager, texture, SIZE, SIZE);
        this.playersProfile = playersProfile;
        speed = playersProfile.getSpeed();
        defence = playersProfile.getDefence();
        attack = playersProfile.getAttack();
        body = worldManager.createCircleBody(x, y, SIZE / 2, false, false, this);
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public PlayersProfile getPlayersProfile() {
        return playersProfile;
    }

    public void move(Vector2 direction) {
        body.setLinearVelocity(direction.scl(5 + (speed - 1) * 2));
    }

    public void addMoney(int money) {
        playersProfile.setMoney(playersProfile.getMoney() + money);
    }

    public void shoot(Vector2 direction) {
        if (weapon == null) {
            return;
        }
        weapon.shoot(direction);
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public void update(float delta) {
        weapon.update(delta);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        weapon.draw(batch);
    }
}
