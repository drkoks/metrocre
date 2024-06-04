package com.metrocre.game.map;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.List;

public class LevelGraph implements IndexedGraph<GraphEntity> {
    public class MediumPoint extends GraphEntity {
        MediumPoint(int x, int y, GraphEntity parent) {
            center_x = x;
            center_y = y;
            width = 1;
            height = 1;
            addEntity(this);
            connectEntities(parent, this);
        }
    }

    GraphHeuristic cityHeuristic = new GraphHeuristic();
    List<GraphEntity> entities = new ArrayList<>();
    List<HallWay> hallways = new ArrayList<>();

    ObjectMap<GraphEntity, Array<Connection<GraphEntity>>> hallWayMap = new ObjectMap<>();

    private int lastNodeIndex = 0;

    public void addEntity(GraphEntity entity){
        entity.index = lastNodeIndex;
        lastNodeIndex++;
        entities.add(entity);
    }

    public int connectRooms(Room from, Room to) {
        int[] fromCorners = from.rayIntersection(to.center_x, to.center_y);
        int[] toCorners = to.rayIntersection(from.center_x, from.center_y);
        MediumPoint fromCorner = new MediumPoint(fromCorners[0], fromCorners[1], from);
        MediumPoint toCorner = new MediumPoint(toCorners[0], toCorners[1], to);
        if (from.center_x == to.center_x || from.center_y == to.center_y) {
            connectEntities(fromCorner, toCorner);
            return 2;
        } else {
            MediumPoint medium = new MediumPoint(from.center_x, to.center_y, fromCorner);
            connectEntities(medium, toCorner);
            return 3;
        }
    }
    public void connectEntities(GraphEntity from, GraphEntity to) {
        if(!hallWayMap.containsKey(from)) {
            hallWayMap.put(from, new Array<>());
        }
        if (!hallWayMap.containsKey(to)) {
            hallWayMap.put(to, new Array<>());
        }
        HallWay c1 = new HallWay(from, to, from.dist(to));
        HallWay c2 = new HallWay(to, from, from.dist(to));
        hallWayMap.get(from).add(c1);
        hallWayMap.get(to).add(c2);
        hallways.add(c1);
        hallways.add(c2);
    }

    public GraphPath<GraphEntity> findPath(GraphEntity start, GraphEntity goal){
        GraphPath<GraphEntity> path = new DefaultGraphPath<>();
        new IndexedAStarPathFinder<>(this).searchNodePath(start, goal, cityHeuristic, path);
        return path;
    }

    @Override
    public int getIndex(GraphEntity node) {
        return node.index;
    }

    @Override
    public int getNodeCount() {
        return lastNodeIndex;
    }

    @Override
    public Array<Connection<GraphEntity>> getConnections(GraphEntity fromNode) {
        if(hallWayMap.containsKey(fromNode)){
            return hallWayMap.get(fromNode);
        }
        return new Array<>(0);
    }
}