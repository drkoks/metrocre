package com.metrocre.game.map;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room extends GraphEntity {
    private final static int minRoomSize = 25;
    private final static int maxRoomSize = 50;

    int floorType;

    public static Room initialRoom(int mapWidth, int mapHeight) {
        Room init = new Room();
        init.height = mapHeight;
        init.width = 11;
        init.center_x = 5;
        init.center_y = mapHeight / 2;
        init.index = 0;
        return init;
    }

    Room() {
        super();
    }
    Room(Random random, int mapWidth, int mapHeight, int index) {
        width = random.nextInt(maxRoomSize - minRoomSize + 1) + minRoomSize;
        width = width * 2 + 1;
        height = random.nextInt(maxRoomSize - minRoomSize + 1) + minRoomSize;
        height = height * 2 + 1;
        floorType = random.nextInt(2) % 2;
        x = random.nextInt(mapWidth - width - 1) + 1;
        y = random.nextInt(mapHeight - height - 1) + 1;
        center_x = x + (width / 2);
        center_y = y + (height / 2);
        this.index = index;
    }

    int[] rayIntersection(int x, int y) {
        if (x == this.center_x) {
            if (y < this.center_y) {
                return new int[]{x, this.y};
            } else {
                return new int[]{x, this.y + this.height};
            }
        } else {
            if (x < this.center_x) {
                return new int[]{this.x, y};
            }
        }
        return new int[]{this.x + this.width, y};
    }

    float[] corners() {
        float xR = x + width;
        float yR = y + height;
        return new float[]{x, y, xR, yR};
    }

    public boolean intersect(Room anotherRoom) {
        float[] firstCorners = corners();
        float[] secondCorners = anotherRoom.corners();
        return firstCorners[0] <= secondCorners[2] &&
                secondCorners[0] <= firstCorners[2] &&
                firstCorners[1] <= secondCorners[3] &&
                secondCorners[1] <= firstCorners[3];
    }

    CustomGameMapData drawRoom(CustomGameMapData data) {
        for (int row = x; row < x + width; row++) {
            for (int col = y; col < y + height; col++) {
                if (floorType == 0) {
                    data.map[1][data.map[0].length - row - 1][col] = TileType.FLOOR1.getId();
                } else {
                    data.map[1][data.map[0].length - row - 1][col] = TileType.FLOOR2.getId();
                }
            }
        }
        return data;
    }

    static CustomGameMapData drawCorridor(CustomGameMapData data, int x_st, int y_st, int x_end, int y_end) {
        for (int row = x_st; row < x_end; row++) {
            for (int col = y_st; col < y_end; col++) {
                data.map[1][data.map[0].length - row - 1][col] = TileType.FLOOR3.getId();
            }
        }
        return data;
    }
}
