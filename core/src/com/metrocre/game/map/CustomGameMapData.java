package com.metrocre.game.pathAi;

import java.util.Random;

public class CustomGameMapData {
    public final static int minRooms = 5;
    public final static int maxRooms = 15;
    public String id;
    public String name;
    public int[][][] map;
    Random random;
    public LevelGraph graph;

}