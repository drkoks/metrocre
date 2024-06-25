package com.metrocre.game.world;

import static com.metrocre.game.MyGame.SCALE;
import static java.lang.Math.max;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.PlayersProfile;
import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.network.Network;
import com.metrocre.game.weapons.Pistol;
import com.metrocre.game.weapons.Railgun;
import com.metrocre.game.weapons.Weapon;
import com.metrocre.game.screens.MainMenuScreen;
import com.metrocre.game.weapons.Pistol;
import com.metrocre.game.weapons.Railgun;
import com.metrocre.game.weapons.Weapon;
import com.metrocre.game.world.enemies.Enemy;

import states.PlayerState;

public class Player extends Entity {
    public static final float SIZE = SCALE;

    private Weapon weapon = null;
    private int speed;
    private final PlayersProfile playersProfile;
    private int health;
    private Sprite sprite;
    private int attack;
    private boolean isDamaged;
    private float damageTime;
    private static final float DAMAGE_DISPLAY_DURATION = 0.5f;

    private int healthFromDefence(){
        int health = 100;
        for (int i = 1; i < playersProfile.getDefence(); i++) {
            health *= 1.2;
        }
        return health;
    }

    public Player(float x, float y, WorldManager worldManager, PlayersProfile playersProfile) {
        super(worldManager, worldManager.getTexture("player"), SIZE, SIZE);
        this.playersProfile = playersProfile;
        speed = playersProfile.getSpeed();
        health = healthFromDefence();
        attack = playersProfile.getAttack();
        this.sprite = new Sprite(worldManager.getTexture("player"));
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

    public void takeDamage(float damage, int senderId) {
        if (worldManager.getServer() != null && isDamaged) {
            return;
        }
        float multiplier = 1f;
        Entity sender = worldManager.getEntity(senderId);
        if (sender instanceof Enemy) {
            multiplier = ((Enemy) sender).getAttack();
        }
        if (health == 0) {
            return;
        }
        float finalDamage = damage * multiplier;
        health = (int) max(0f, health - finalDamage);
        if (health == 0) {
            onDeath();
        } else {
            isDamaged = true;
            damageTime = DAMAGE_DISPLAY_DURATION;
            sprite.setColor(Color.RED);
        }
        if (worldManager.getServer() != null) {
            Network.TakeDamage takeDamage = new Network.TakeDamage();
            takeDamage.senderId = senderId;
            takeDamage.receiverId = id;
            takeDamage.damage = damage;
            worldManager.getServer().packToSend(takeDamage);
        }
    }

    private void onDeath() {
        if (worldManager.getServer() != null) {
            destroy();
        }
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
        playersProfile.setWeaponId(weaponId);
        if (weaponId == 1) {
            setWeapon(new Pistol(this, worldManager.getProjectileManager(), new Texture("pistol.png"),
                    0.6F * SCALE, 0.4F * SCALE, playersProfile.getWeaponLevel()));
        } else if (weaponId == 2) {
            setWeapon(new Railgun(this, worldManager.getProjectileManager(), new Texture("laser.png"),
                    0.6F * SCALE, 0.4F * SCALE, playersProfile.getWeaponLevel()));
        }
        if (worldManager.getServer() != null) {
            WorldEvents.EquipWeapon equipWeapon = new WorldEvents.EquipWeapon();
            equipWeapon.playerId = id;
            equipWeapon.weaponId = weaponId;
            worldManager.getServer().packToSend(equipWeapon);
        }
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public String getWeapon() {
        return weapon.getClass().getSimpleName();
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public float getDamageTime() {
        return damageTime;
    }

    public void heal(float i) {
        health += i;
        if (health > healthFromDefence()) {
            health = healthFromDefence();
        }
        if (worldManager.getServer() != null) {
            Network.Heal heal = new Network.Heal();
            heal.playerId = id;
            heal.value = i;
            worldManager.getServer().packToSend(heal);
        }
    }
}
