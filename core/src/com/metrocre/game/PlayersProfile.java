package com.metrocre.game;

import com.badlogic.gdx.graphics.Texture;

public class PlayersProfile {
    private final String name;

    private int level;
    private int experience;
    private int money;
    private int speedLevel;
    private int defenceLevel;
    private int attackLevel;
    private int weaponId;

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
                setWeaponId(1);
                break;
            case Railgun:
                setWeaponId(2);
                break;
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
        }
        return 0;
    }

    public void setAttack(int attackLevel) {
        this.attackLevel = attackLevel;
    }
}
