package com.metrocre.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.metrocre.game.PlayersProfile;

public class EntityData {
    public static class PlayerData {
        public float x;
        public float y;
        public PlayersProfile profile;
    }

    public static class EnemyData {
        public float x;
        public float y;
        public int type;
    }

    public static class ProjectileData {
        public Vector2 position;
        public Vector2 direction;
        public float damage;
        public float speed;
        public int senderId;
        public boolean isHeal;
    }

    public static class TrainData {
        public float x;
        public float y;
        public float width;
        public float height;
    }

    public static class TowerData {
        public float x;
        public float y;
        public int type;
        public int playerId;
    }
}
