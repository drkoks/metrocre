package com.metrocre.game;

import static com.metrocre.game.MyGame.SCALE;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.metrocre.game.towers.GunTower;
import com.metrocre.game.towers.HealTower;
import com.metrocre.game.towers.TowerPlace;
import com.metrocre.game.world.WorldManager;
import com.metrocre.game.world.enemies.Enemy1;
import com.metrocre.game.world.enemies.Enemy2;
import com.metrocre.game.world.enemies.EnemySpawner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Map {
    public static final int TILE_SIZE = 48;
    private int height;
    private int width;
    private int id;

    private final TiledMap map;
    private static final Set<Integer> emptyCellsID =
            new HashSet<>(Arrays.asList(310, 695, 756, 740, 741, 757, 153));
    private final OrthogonalTiledMapRenderer renderer;

    static boolean isEmptyTile(Cell cell) {
        return emptyCellsID.contains(cell.getTile().getId());
    }
    public Map(WorldManager worldManager, int seedId, boolean isNew) {
        id = seedId;
        map = new TmxMapLoader().load("tilesAtribute/maps/map" + id + ".tmx");
        TiledMapTileLayer blockLayer = (TiledMapTileLayer) map.getLayers().get(1);
        width = blockLayer.getWidth()*SCALE;
        height = blockLayer.getHeight()*SCALE;
        for (int x = 0; x < blockLayer.getWidth(); x++) {
            for (int y = 0; y < blockLayer.getHeight(); y++) {
                setUpCell(blockLayer.getCell(x, y), worldManager, x, y, isNew);
            }
        }
        renderer = new OrthogonalTiledMapRenderer(map, (float) SCALE / TILE_SIZE);
    }

    private void setUpCell(Cell cell, WorldManager worldManager, int x, int y, boolean isNew) {
        if (isEmptyTile(cell)) {
            worldManager.createRectangleBody(x*SCALE, y*SCALE, SCALE, SCALE, cell);
        }
        if (!isNew) {
            return;
        }
        int type = cell.getTile().getId();
        if (type == 101){
            worldManager.addSpawner(new EnemySpawner(worldManager, x*SCALE, y*SCALE, 1, 2, 0));
        } else if (type == 103) {
            worldManager.addSpawner(new EnemySpawner(worldManager, x * SCALE, y * SCALE, 2, 2, 0));
        } else if (type == 7) {
            worldManager.addTowerPlace(new TowerPlace(x * SCALE, y * SCALE, 2));
        } else if (type == 5) {
            worldManager.addTowerPlace(new TowerPlace(x * SCALE, y * SCALE, 1));
        }

    }

    public void draw(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    public int getMapID() {
        return id;
    }
}
