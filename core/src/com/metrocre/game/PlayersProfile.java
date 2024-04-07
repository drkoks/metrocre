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

    public boolean buyItem(String ItemName){
        int price = getSelectedItem(ItemName) * 100;
        if (getMoney() < price){
            return false;
        }
        setMoney(getMoney() - price);
        if(ItemName == "Speed"){
            setSpeed(getSpeed() + 1);
        }
        else if(ItemName == "Defence"){
            setDefence(getDefence() + 1);
        }
        else if(ItemName == "Attack"){
            setAttack(getAttack() + 1);
        }
        return true;
    }
    public int getSelectedItem(String ItemName){
        if(ItemName == "Speed"){
            return getSpeed();
        }
        else if(ItemName == "Defence"){
            return getDefence();
        }
        else if(ItemName == "Attack"){
           return getAttack();
        } else {
            return 0;
        }
    }

    public void setAttack(int attackLevel) {
        this.attackLevel = attackLevel;
    }
}
