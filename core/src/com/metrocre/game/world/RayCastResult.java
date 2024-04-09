package com.metrocre.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import java.util.ArrayList;
import java.util.List;

public class RayCastResult {
    public List<Entity> hitted = new ArrayList<>();
    public List<Float> fractions = new ArrayList<>();
    public Vector2 hitPoint;
    public float hitPointFraction = 1;

    public RayCastResult(Vector2 endPoint) {
        this.hitPoint = endPoint;
    }
}
