package com.metrocre.game.map;
import com.badlogic.gdx.ai.pfa.Connection;
import com.metrocre.game.Map;
import com.badlogic.gdx.math.Vector2;

public class HallWay implements Connection<GraphEntity> {
    GraphEntity from;
    GraphEntity to;
    float cost;

    public HallWay(GraphEntity from, GraphEntity to, float dist) {
        this.from = from;
        this.to = to;
        this.cost = dist;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public GraphEntity getFromNode() {
        return from;
    }

    @Override
    public GraphEntity getToNode() {
        return to;
    }
}
