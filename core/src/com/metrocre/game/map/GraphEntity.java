package com.metrocre.game.map;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public abstract class GraphEntity {
    List<HallWay> neighbours;
    public int x;
    public int y;
    public int index;
    public int width;
    public int height;

    public int center_x;
    public int center_y;

    GraphEntity() {
        neighbours = new ArrayList<>();
    }

    float dist(GraphEntity another) {
        return Vector2.dst(this.center_x, this.center_y, this.center_x, this.center_y);
    }
    public int getIndex(){
        return index;
    }
}
