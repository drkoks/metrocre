package com.metrocre.game;

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
import com.metrocre.game.world.WorldManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Map {
    public static final int TILE_SIZE = 32;
    public static final int TILE_SIZE_KATE = 48;
    public static final TextureRegion[][] SPLIT_TILES = TextureRegion.split(new Texture("tiles.png"), TILE_SIZE, TILE_SIZE);
    public static final TextureRegion[][] SPLIT_TILES_KATE = TextureRegion.split(new Texture("tileM.png"), TILE_SIZE_KATE, TILE_SIZE_KATE);
    public static final StaticTiledMapTile emptyTile = new StaticTiledMapTile(SPLIT_TILES_KATE[0][8]);
    private  int height;
    private  int width;
    private final TiledMap map;
    private static final Set<Integer> emptyCellsID =
            new HashSet<>(Arrays.asList(310, 695, 756, 740, 741, 757));
    private final OrthogonalTiledMapRenderer renderer;

    static boolean isEmptyTile(Cell cell) {
        return emptyCellsID.contains(cell.getTile().getId());
    }
    public Map(WorldManager worldManager) {
        map = new TmxMapLoader().load("tilesAtribute/mapmap.tmx");
        MapLayers layers = map.getLayers();
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        width = layer.getWidth();
        height = layer.getHeight();
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                System.out.println(layer.getCell(x, y).getTile().getId());
                if (isEmptyTile(layer.getCell(x, y))) {
                    worldManager.createRectangleBody(x, y, 1, 1, layer.getCell(x, y));
                }
            }
        }
        System.out.println("<<<<<" + emptyTile.getId() + ">>>>");
        layers.add(layer);
        renderer = new OrthogonalTiledMapRenderer(map, 1f / TILE_SIZE_KATE);
    }

    public Map(int[][] mapData, WorldManager worldManager) {
        height = mapData.length;
        width = mapData[0].length;

        map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, TILE_SIZE, TILE_SIZE);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = createCell(x, y, mapData, worldManager);
                layer.setCell(x, y, cell);
            }
        }
        layers.add(layer);

        renderer = new OrthogonalTiledMapRenderer(map, 1f / TILE_SIZE);
    }

    public void draw(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    private Cell createCell(int x, int y, int[][] mapData, WorldManager worldManager) {
        Cell cell = new Cell();
        if (mapData[x][y] == 1) {
            worldManager.createRectangleBody(x, y, 1, 1, cell);
        }
        cell.setTile(new StaticTiledMapTile(SPLIT_TILES[mapData[x][y] / SPLIT_TILES[0].length][mapData[x][y] % SPLIT_TILES[0].length]));
        return cell;
    }

    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
}
