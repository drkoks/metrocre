package com.metrocre.game.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.world.Entity;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Rail {
    public static final float LIFE_TIME = 1;

    private Vector2 p1;
    private Vector2 p2;
    private float clock = 0;
    private float damage;
    private int senderId;

    public Rail() {}

    public Rail(Vector2 p1, Vector2 p2, float damage, int senderId) {
        this.damage = damage;
        this.p1 = p1;
        this.p2 = p2;
        this.senderId = senderId;
    }

    public void update(float delta) {
        clock += delta;
    }

    public void draw(ShapeDrawer shapeDrawer) {
        shapeDrawer.line(p1.x, p1.y, p2.x, p2.y, new Color(1, 0, 0, 1 - clock / LIFE_TIME));
    }

    public float getDamage() {
        return damage;
    }

    public boolean isExpired() {
        return clock >= LIFE_TIME;
    }

    public int getSenderId() {
        return senderId;
    }
}
