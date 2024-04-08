package com.metrocre.game;

import static java.lang.Math.max;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements Telegraph {
    public static final float SIZE = 1f;

    private Weapon weapon;
    private float shootCooldown = 0;
    private float reloadTime = 1f;
    private int speed;
    private PlayersProfile playersProfile;
    private int defence;
    private int attack;

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public Player(float x, float y, WorldManager worldManager, Texture texture, PlayersProfile playersProfile) {
        super(worldManager, texture, SIZE, SIZE);
        this.playersProfile = playersProfile;
        body = worldManager.createCircleBody(x, y, SIZE / 2, false, false, this);
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

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
