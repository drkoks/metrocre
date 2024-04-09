package com.metrocre.game.pathAi;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

public class GraphHeuristic implements Heuristic<GraphEntity> {
    @Override
    public float estimate(GraphEntity curr, GraphEntity goal) {
        return Vector2.dst(curr.center_x, curr.center_y, goal.center_x, goal.center_y);
    }
}