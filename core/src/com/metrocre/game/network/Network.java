package com.metrocre.game.network;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.metrocre.game.PlayersProfile;
import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.world.EntityData;
import com.metrocre.game.world.EntityType;

public class Network {
    static public final int PORT = 13337;

    public static void register(EndPoint endpoint) {
        Kryo kryo = endpoint.getKryo();

        kryo.register(SendMapSeed.class);
        kryo.register(UpdateEntityPosition.class);
        kryo.register(PlayerMove.class);
        kryo.register(PlayerAttack.class);
        kryo.register(SetLocalPlayer.class);
        kryo.register(DestroyEntity.class);

        kryo.register(WorldEvents.AddEntity.class);
        kryo.register(EntityType.class);
        kryo.register(EntityData.ProjectileData.class);
        kryo.register(EntityData.EnemyData.class);
        kryo.register(EntityData.PlayerData.class);
        kryo.register(PlayersProfile.class);
        kryo.register(Vector2.class);

        kryo.register(WorldEvents.EquipWeapon.class);
    }

    public static class SendMapSeed {

    }

    public static class UpdateEntityPosition {
        public int entityId;
        public float x;
        public float y;
    }

    public static class PlayerMove {
        public Vector2 direction;
    }

    public static class PlayerAttack {
        public Vector2 direction;
    }

    public static class SetLocalPlayer {
        public int id;
    }

    public static class DestroyEntity {
        public int id;
    }
}