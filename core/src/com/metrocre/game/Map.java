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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Map {
    public static final int TILE_SIZE = 32;
    public static final TextureRegion[][] SPLIT_TILES = TextureRegion.split(new Texture("tiles.png"), TILE_SIZE, TILE_SIZE);
    private int width;
    private int height;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public Map(int[][] mapData, World world) {
        height = mapData.length;
        width = mapData[0].length;

        map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, TILE_SIZE, TILE_SIZE);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = createCell(x, y, mapData, world);
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

    private Cell createCell(int x, int y, int[][] mapData, World world) {
        Cell cell = new Cell();
        if (mapData[x][y] == 1) {
            createCellBody(x, y, world);
        }
        cell.setTile(new StaticTiledMapTile(SPLIT_TILES[mapData[x][y] / SPLIT_TILES[0].length][mapData[x][y] % SPLIT_TILES[0].length]));
        return cell;
    }

    private Body createCellBody(int x, int y, World world) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.fixedRotation = true;
        bd.position.set(new Vector2(x, y));
        Body body = world.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.set(new float[]{0, 0, 0, 1, 1, 1, 1, 0});

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.friction = 0;

        Fixture fixture = body.createFixture(fd);

        shape.dispose();

        return body;
    }
}
