package com.metrocre.game.map;
import java.util.HashMap;

public enum TileType {

    LEFT(1, true, "Left boarder"),
    BOTTOM(2, true, "Bottom boarder"),
    RIGHT(3, true, "Right boarder"),
    TOP(4, true, "Top bottom"),
    TL_C(5, true, "Top left corner"),
    TR_C(6, true, "Top right corner"),
    BL_C(7, true, "Bottom left corner"),
    BR_C(8, true, "Bottom right corner"),
    HOLE(9, true, "Hole"),
    FLOOR1(10, false, "Floor 1 "),
    FLOOR2(11, false, "Floor 2"),
    FLOOR3(12, false, "Floor 3"),
    RAIL(16, false, "Rail");

    public static final int TILE_SIZE = 48;

    private int id;
    private boolean collidable;
    private String name;
    private float damage;

    private TileType(int id, boolean collidable, String name) {
        this(id, collidable, name, 0);
    }

    private TileType(int id, boolean collidable, String name, float damage) {
        this.id = id;
        this.collidable = collidable;
        this.name= name;
        this.damage = damage;
    }

    public int getId() {
        return id;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public String getName() {
        return name;
    }

    public float getDamage() {
        return damage;
    }

    private static HashMap<Integer, TileType> tileMap;

    static {
        tileMap = new HashMap<Integer, TileType>();
        for (TileType tileType : TileType.values()) {
            tileMap.put(tileType.getId(), tileType);
        }
    }

    public static TileType getTileTypeById(int id) {
        return tileMap.get(id);
    }

}