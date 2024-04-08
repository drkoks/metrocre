package com.metrocre.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class Map {
    public static final int TILE_SIZE = 32;
    public static final TextureRegion[][] SPLIT_TILES = TextureRegion.split(new Texture("tiles.png"), TILE_SIZE, TILE_SIZE);
    private final int width;
    private final int height;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

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
}
