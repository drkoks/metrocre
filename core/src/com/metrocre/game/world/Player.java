package com.metrocre.game.world;

import static com.metrocre.game.MyGame.SCALE;
import static java.lang.Math.max;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.PlayersProfile;
import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.weapons.Pistol;
import com.metrocre.game.weapons.Railgun;
import com.metrocre.game.weapons.Weapon;

public class Player extends Entity {
    public static final float SIZE = SCALE;

    private Weapon weapon = null;
    private int speed;
    private final PlayersProfile playersProfile;
    private int health;
    private int attack;
    private boolean isDamaged;
    private float damageTime;
    private static final float DAMAGE_DISPLAY_DURATION = 0.5f;
    private Sprite sprite;

    private int healthFromDefence(){
        int health = 100;
        for (int i = 1; i < playersProfile.getDefence(); i++) {
            health *= 1.2;
        }
        return health;
    }

    public Player(float x, float y, WorldManager worldManager, PlayersProfile playersProfile, Texture texture) {
        super(worldManager, texture, SIZE, SIZE);
        this.playersProfile = playersProfile;
        speed = playersProfile.getSpeed()*10;
        health = healthFromDefence();
        attack = playersProfile.getAttack();
        this.sprite = new Sprite(texture);
        sprite.setSize(SIZE, SIZE);
        sprite.setPosition(x, y);
        body = worldManager.createCircleBody(x, y, SIZE / 2, false, false, this);
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
    public void takeDamage(float damage, Entity sender) {
        if (health == 0) {
            return;
        }
        health = (int) max(0f, health - damage);
        if (health == 0) {
            onDeath();
        } else {
            isDamaged = true;
            damageTime = DAMAGE_DISPLAY_DURATION;
            sprite.setColor(Color.RED);
        }
    }
    private void onDeath() {

    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public void update(float delta) {
        if (isDamaged) {
            damageTime -= delta;
            if (damageTime <= 0) {
                isDamaged = false;
                sprite.setColor(Color.WHITE);
            }
        }
        if (weapon != null) {
            weapon.update(delta);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        if (weapon != null) {
            weapon.draw(batch);
        }
    }

    public int getHealth() {
        return health;
    }

    public int getMoney() {
        return playersProfile.getMoney();
    }

    public void equipWeapon(int weaponId) {
        if (weaponId == 1) {
            setWeapon(new Pistol(this, worldManager.getProjectileManager(), new Texture("pistol.png"),
                    0.6F * SCALE, 0.4F * SCALE));
        } else if (weaponId == 2) {
            setWeapon(new Railgun(this, worldManager.getProjectileManager(), new Texture("railgun.png"),
                    0.6F * SCALE, 0.4F * SCALE));
        }
        if (worldManager.getServer() != null) {
            WorldEvents.EquipWeapon equipWeapon = new WorldEvents.EquipWeapon();
            equipWeapon.playerId = id;
            equipWeapon.weaponId = weaponId;
            worldManager.getServer().packToSend(equipWeapon);
        }
    }
}
