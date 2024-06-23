package com.metrocre.game.event.world;

import com.metrocre.game.world.EntityType;

public class WorldEvents {
    public static final int PROJECTILE_HIT = 0;
    public static final int RAIL_HIT = 1;

    public static class AddEntity {
        public static final int ID = 2;

        public EntityType type;
        public Object data;
    }

    public static class EquipWeapon {
        public static final int ID = 3;

        public int playerId;
        public int weaponId;
    }
}
