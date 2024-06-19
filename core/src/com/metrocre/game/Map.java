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
import com.metrocre.game.world.WorldManager;
import com.metrocre.game.world.enemies.Enemy1;
import com.metrocre.game.world.enemies.Enemy2;

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
    private int id;

    private final TiledMap map;
    private static final Set<Integer> emptyCellsID =
            new HashSet<>(Arrays.asList(310, 695, 756, 740, 741, 757, 153));
    private final OrthogonalTiledMapRenderer renderer;

    static boolean isEmptyTile(Cell cell) {
        return emptyCellsID.contains(cell.getTile().getId());
    }
    public Map(WorldManager worldManager, int seedId, boolean isNew) {
        generateDefault(worldManager, seedId);
        if (isNew) {
            generateEntities(worldManager, seedId);
        }
        id = seedId;
        map = new TmxMapLoader().load("tilesAtribute/maps/map" + id + ".tmx");
        TiledMapTileLayer blockLayer = (TiledMapTileLayer) map.getLayers().get(1);
        width = blockLayer.getWidth()*SCALE;
        height = blockLayer.getHeight()*SCALE;
        for (int x = 0; x < blockLayer.getWidth(); x++) {
            for (int y = 0; y < blockLayer.getHeight(); y++) {
                //System.out.println(layer.getCell(x, y).getTile().getId());
                if (isEmptyTile(blockLayer.getCell(x, y))) {
                    worldManager.createRectangleBody(x*SCALE, y*SCALE, SCALE, SCALE, blockLayer.getCell(x, y));
                }
            }
        }
        //System.out.println("<<<<<" + emptyTile.getId() + ">>>>");
        renderer = new OrthogonalTiledMapRenderer(map, (float) SCALE / TILE_SIZE_KATE);
    }

    private void generateDefault(WorldManager worldManager, int seedId) {
        worldManager.addEntity(new GunTower(6.5f * SCALE, 5.9f * SCALE, 5,
                10 * SCALE, worldManager, worldManager.getPlayer(), "gunTower"));

        worldManager.addEntity(new HealTower(6.5f * SCALE, 8f * SCALE, 5,
                2 * SCALE, worldManager, worldManager.getPlayer(), "healTower"));
    }

    private void generateEntities(WorldManager worldManager, int seedId) {
        if (seedId == 1) {
            worldManager.addEntity(new Enemy1(12 * SCALE, 6 * SCALE, worldManager));
            worldManager.addEntity(new Enemy2(14 * SCALE, 6 * SCALE, worldManager));
            worldManager.addEntity(new Enemy1(14 * SCALE, 7 * SCALE, worldManager));
        } else if (seedId == 2){
            worldManager.addEntity(new Enemy1(12 * SCALE, 11 * SCALE, worldManager));
            worldManager.addEntity(new Enemy1(12 * SCALE, 3 * SCALE, worldManager));
            worldManager.addEntity(new Enemy2(22 * SCALE, 12 * SCALE, worldManager));
            worldManager.addEntity(new Enemy2(20 * SCALE, 6 * SCALE, worldManager));
        } else if (seedId == 3){
            worldManager.addEntity(new Enemy1(16 * SCALE, 3 * SCALE, worldManager));
            worldManager.addEntity(new Enemy1(16 * SCALE, 12 * SCALE, worldManager));
            worldManager.addEntity(new Enemy2(29 * SCALE, 7 * SCALE, worldManager));
            worldManager.addEntity(new Enemy2(23 * SCALE, 12 * SCALE, worldManager));
        } else if (seedId == 4){
            worldManager.addEntity(new Enemy1(12 * SCALE, 11 * SCALE, worldManager));
            worldManager.addEntity(new Enemy1(10 * SCALE, 2 * SCALE, worldManager));
            worldManager.addEntity(new Enemy2(21 * SCALE, 11 * SCALE, worldManager));
            worldManager.addEntity(new Enemy2(29 * SCALE, 11 * SCALE, worldManager));
        } else if (seedId == 5){
            worldManager.addEntity(new Enemy1(12 * SCALE, 11 * SCALE, worldManager));
            worldManager.addEntity(new Enemy1(10 * SCALE, 2 * SCALE, worldManager));
            worldManager.addEntity(new Enemy2(21 * SCALE, 11 * SCALE, worldManager));
            worldManager.addEntity(new Enemy2(29 * SCALE, 11 * SCALE, worldManager));

        }

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

    public int getMapID() {
        return id;
    }
}
