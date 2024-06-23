package com.metrocre.game;

public class PlayersProfile {
    private String name;

    private int level;
    private int experience;
    private int money;
    private int speedLevel;
    private int defenceLevel;
    private int attackLevel;

    public PlayersProfile() {}

    public PlayersProfile(String name, int level, int experience, int money, int speedLevel, int defenceLevel, int attackLevel) {
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.money = money;
        this.speedLevel = speedLevel;
        this.defenceLevel = defenceLevel;
        this.attackLevel = attackLevel;
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
        int price = getSelectedItem(item) * 100;
        return getMoney() >= price;
    }

    public boolean buyItem(Upgrades item) {
        if (!canBuyItem(item)) {
            return false;
        }
        int price = getSelectedItem(item) * 100;
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
        }
        return true;
    }
    public int getSelectedItem(Upgrades item){
        switch (item) {
            case Speed:
                return getSpeed();
            case Defence:
                return getDefence();
            case Attack:
                return getAttack();
        }
        return 0;
    }

    public void setAttack(int attackLevel) {
        this.attackLevel = attackLevel;
    }
}
