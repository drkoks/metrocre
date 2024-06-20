package com.metrocre.game;

import com.badlogic.gdx.graphics.Texture;
import com.metrocre.game.world.enemies.Enemy;

import states.PlayerStat;

public class PlayersProfile {
    private final String name;

    private int level;
    private int experience;
    private int money;
    private int speedLevel;
    private int defenceLevel;
    private int attackLevel;
    private int weaponId;
    private int weaponLevel = 1;

    private int healTowerCounter = 0;
    private int gunTowerCounter = 0;

    private PlayerStat statistics = new PlayerStat();

    public PlayersProfile(String name, int level, int experience, int money, int speedLevel, int defenceLevel, int attackLevel) {
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.money = money;
        this.speedLevel = speedLevel;
        this.defenceLevel = defenceLevel;
        this.attackLevel = attackLevel;
        weaponId = 1;
    }
    public int getWeaponId() {
        return weaponId;
    }
    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public String getWeaponName() {
        switch (weaponId) {
            case 1:
                return "Pistol";
            case 2:
                return "Railgun";
        }
        return "Unknown";
    }
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getMoney() {
        return money;
    }

    public int getSpeed() {
        return speedLevel;
    }

    public int getDefence() {
        return defenceLevel;
    }

    public int getAttack() {
        return attackLevel;
    }

    public int getWeaponLevel() {
        return weaponLevel;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setSpeed(int speedLevel) {
        this.speedLevel = speedLevel;
    }

    public void setDefence(int defenceLevel) {
        this.defenceLevel = defenceLevel;
    }

    public boolean canBuyItem(Upgrades item) {
        int price = getSelectedItemCost(item);
        return getMoney() >= price;
    }

    public boolean buyItem(Upgrades item) {
        if (!canBuyItem(item)) {
            return false;
        }
        int price = getSelectedItemCost(item);
        setMoney(getMoney() - price);
        switch (item) {
            case Speed:
                setSpeed(getSpeed() + 1);
                break;
            case Defence:
                setDefence(getDefence() + 1);
                break;
            case Attack:
                setAttack(getAttack() + 1);
                break;
            case Pistol:
                if (weaponId == 1){
                    weaponLevel++;
                } else {
                    setWeaponId(1);
                }
                break;
            case Railgun:
                if (weaponId == 2){
                    weaponLevel++;
                } else {
                    setWeaponId(2);
                }
                break;
            case HealTower:
                healTowerCounter++;
            case GunTower:
                gunTowerCounter++;
        }
        return true;
    }
    public int getSelectedItemCost(Upgrades item){
        switch (item) {
            case Speed:
                return getSpeed()*100;
            case Defence:
                return getDefence()*100;
            case Attack:
                return getAttack()*100;
            case Pistol:
                return 100;
            case Railgun:
                return 200;
            case HealTower:
                return 150;
            case GunTower:
                return 100;
        }
        return 0;
    }

    public void setAttack(int attackLevel) {
        this.attackLevel = attackLevel;
    }

    public void resetAfterLevel(){
        healTowerCounter = 0;
        gunTowerCounter = 0;
    }

    public void reportKill(Enemy enemy) {
        if (enemy == null) {
            return;
        }
        statistics.addKill(enemy.getCoolName());
    }

    public PlayerStat getStatistics() {
        return statistics;
    }

    public int getHealTowers() {
        return healTowerCounter;
    }

    public int getGunTowers() {
        return gunTowerCounter;
    }
}
