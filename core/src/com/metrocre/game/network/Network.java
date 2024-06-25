package com.metrocre.game.network;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.metrocre.game.PlayersProfile;
import com.metrocre.game.Upgrades;
import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.weapons.Rail;
import com.metrocre.game.world.EntityData;
import com.metrocre.game.world.EntityType;

import java.util.HashMap;

import states.PlayerStat;

public class Network {
    static public final int PORT = 13337;

    public static void register(EndPoint endpoint) {
        Kryo kryo = endpoint.getKryo();

        kryo.register(HashMap.class);
        kryo.register(PlayerStat.class);

        kryo.register(SendMapSeed.class);
        kryo.register(UpdateEntityPosition.class);
        kryo.register(PlayerMove.class);
        kryo.register(PlayerAttack.class);
        kryo.register(SetLocalPlayer.class);
        kryo.register(DestroyEntity.class);
        kryo.register(EndGame.class);
        kryo.register(Buy.class);
        kryo.register(CompleteLevel.class);
        kryo.register(NextLevel.class);
        kryo.register(AbleToFinish.class);
        kryo.register(PlayerReady.class);
        kryo.register(PlayerJoined.class);
        kryo.register(Upgrades.class);
        kryo.register(TakeDamage.class);
        kryo.register(Heal.class);

        kryo.register(WorldEvents.AddEntity.class);
        kryo.register(EntityType.class);
        kryo.register(EntityData.ProjectileData.class);
        kryo.register(EntityData.EnemyData.class);
        kryo.register(EntityData.PlayerData.class);
        kryo.register(EntityData.TrainData.class);
        kryo.register(EntityData.TowerData.class);
        kryo.register(PlayersProfile.class);
        kryo.register(Vector2.class);
        kryo.register(EntityData.WormData.class);

        kryo.register(WorldEvents.EquipWeapon.class);

        kryo.register(Rail.class);
    }

    public static class SendMapSeed {
        public int seed;
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

    public static class EndGame {
        public boolean isVictory;
    }

    public static class Buy {
        public Upgrades upgrades;
        public int playerId;
        public String playerName;
    }

    public static class CompleteLevel {

    }

    public static class NextLevel {

    }

    public static class AbleToFinish {

    }

    public static class PlayerReady {
        public int cnt;
    }

    public static class PlayerJoined {
        public int cnt;
    }

    public static class TakeDamage {
        public int senderId;
        public int receiverId;
        public float damage;
    }

    public static class Heal {
        public int playerId;
        public float value;
    }
}
