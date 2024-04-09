package com.metrocre.game.map;


import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import static com.metrocre.game.map.Room.drawCorridor;
import static com.metrocre.game.map.Room.initialRoom;

public class CustomGameMapLoader {
    private static final Json json = new Json();

    public static CustomGameMapData loadMap(String id, String name, int mapWidth, int mapHeight) {
        Gdx.files.local("maps/").file().mkdirs();
        FileHandle file = Gdx.files.local("maps/" + id + ".map");
        if (file.exists()) {
            CustomGameMapData data = json.fromJson(CustomGameMapData.class, file.readString());
            return data;
        } else {
            CustomGameMapData data = generateBasicMap(id, name, mapWidth, mapHeight);
            saveMap(data.id, data.name, data.map);
            return data;
        }

    }

    public static void saveMap(String id, String name, int[][][] map) {
        CustomGameMapData data = new CustomGameMapData();
        data.id = id;
        data.name = name;
        data.map = map;

        Gdx.files.local("maps/").file().mkdirs();
        FileHandle file = Gdx.files.local("maps/" + id + ".map");
        file.writeString(json.prettyPrint(data), false);
    }

    public static CustomGameMapData generateBasicMap (String id, String name, int mapWidth, int mapHeight) {
        CustomGameMapData mapData = new CustomGameMapData();
        mapData.id = id;
        mapData.name = id;
        mapData.map = new int[2][mapHeight][mapWidth];
        mapData.random = new Random();

        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                mapData.map[0][row][col] = TileType.HOLE.getId();
                if (col == 0) {
                    mapData.map[1][row][col] = TileType.RAIL.getId();
                } else if (col == 1) {
                    mapData.map[1][row][col] = TileType.FLOOR3.getId();
                } else if (col < 4) {
                    mapData.map[1][row][col] = TileType.FLOOR1.getId();
                } else if (col == 4) {
                    mapData.map[1][row][col] = TileType.LEFT.getId();
                }
            }
        }
        return mapData;
    }

    public static CustomGameMapData generateRandomMap(String id, String name, int mapWidth, int mapHeight) {
        CustomGameMapData mapData = generateBasicMap(id, name, mapWidth, mapHeight);
        int roomCounter = mapData.random.nextInt(CustomGameMapData.maxRooms - CustomGameMapData.minRooms + 1) + CustomGameMapData.minRooms;
        Room[] rooms = new Room[roomCounter];
        rooms[0] = initialRoom(mapWidth, mapHeight);
        mapData.graph.addEntity(rooms[0]);
        int currInd = 1;
        while (currInd < roomCounter) {
            Room nextRoom = new Room(mapData.random, mapWidth, mapHeight, currInd);
            boolean noIntersection = true;
            for (int prev = 0; prev < currInd; prev++) {
                if (nextRoom.intersect(rooms[prev])) {
                    noIntersection = false;
                }
            }
            if (noIntersection) {
                rooms[currInd++] = nextRoom;
                mapData = nextRoom.drawRoom(mapData);
                mapData.graph.addEntity(nextRoom);
                int crs = mapData.graph.connectRooms(rooms[currInd - 2], rooms[currInd - 1]);
                int totalSizeEntities = mapData.graph.entities.size();
                for (int ind = totalSizeEntities - crs; ind < totalSizeEntities; ind++) {
                    GraphEntity next = mapData.graph.entities.get(ind);
                    mapData = Room.drawCorridor(mapData, next.x, next.y, next.x + next.width, next.y + next.height);
                }
            }
        }
        return mapData;
    }
}