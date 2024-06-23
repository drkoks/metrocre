package com.metrocre.game.network;

import static com.metrocre.game.MyGame.SCALE;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.metrocre.game.Map;
import com.metrocre.game.event.world.WorldEvents;
import com.metrocre.game.world.Entity;
import com.metrocre.game.world.EntityData;
import com.metrocre.game.world.EntityType;
import com.metrocre.game.world.Player;
import com.metrocre.game.world.Train;
import com.metrocre.game.world.WorldManager;
import com.metrocre.game.world.enemies.Enemy;

import java.util.List;
import java.util.Queue;

public class LevelState extends GameState {
    private GameServer server;
    private WorldManager worldManager;
    private Train train;
    private Map map;

    public LevelState(GameServer server) {
        this.server = server;
        worldManager = new WorldManager(new World(new Vector2(0, 0), false), server);
        List<GameServer.GameViewConnection> connections = server.getConnections();
        int cnt = 0;
        for (GameServer.GameViewConnection connection : connections) {
            EntityData.PlayerData playerData = new EntityData.PlayerData();
            playerData.x = (6 - (cnt++)) * SCALE;
            playerData.y = SCALE;
            playerData.profile = connection.gameView.playersProfile;
            WorldEvents.AddEntity addEntity = new WorldEvents.AddEntity();
            addEntity.type = EntityType.Player;
            addEntity.data = playerData;
            worldManager.getMessageDispatcher().dispatchMessage(
                WorldEvents.AddEntity.ID,
                addEntity
            );
            connection.gameView.player = (Player) worldManager.getLastAddedEntity();
            Network.SetLocalPlayer setLocalPlayer = new Network.SetLocalPlayer();
            setLocalPlayer.id = connection.gameView.player.getId();
            connection.messageStock.packToSend(setLocalPlayer);
            WorldEvents.EquipWeapon equipWeapon = new WorldEvents.EquipWeapon();
            equipWeapon.playerId = connection.gameView.player.getId();
            equipWeapon.weaponId = 1;
            worldManager.getMessageDispatcher().dispatchMessage(WorldEvents.EquipWeapon.ID, equipWeapon);
        }
        map = new Map(worldManager);
        Network.SendMapSeed sendMapSeed = new Network.SendMapSeed();
        server.packToSend(sendMapSeed);
    }

    private void processPlayerEvent(Player player, Object event) {
        if (event instanceof Network.PlayerMove) {
//            System.out.println("PlayerMove processed");
            Network.PlayerMove playerMoveEvent = (Network.PlayerMove) event;
            player.move(playerMoveEvent.direction);
        } else if (event instanceof Network.PlayerAttack) {
            Network.PlayerAttack playerAttackEvent = (Network.PlayerAttack) event;
            player.shoot(playerAttackEvent.direction);
        }
    }

    public void update(float deltaTime) {
        for (GameServer.GameViewConnection connection : server.getConnections()) {
            Queue<Object> events = connection.messageStock.getReceived();
            for (Object event : events) {
                processPlayerEvent(connection.gameView.player, event);
            }
        }

        worldManager.getWorld().step(deltaTime, 6, 2);
        worldManager.update(deltaTime);

        for (Entity entity : worldManager.getEntities()) {
            Network.UpdateEntityPosition updateEntityPosition = new Network.UpdateEntityPosition();
            updateEntityPosition.entityId = entity.getId();
            updateEntityPosition.x = entity.getX();
            updateEntityPosition.y = entity.getY();
            server.packToSend(updateEntityPosition);
        }

        server.sendAll();
    }

    private boolean isAbleToFinishLevel() {
        for (Enemy enemy : worldManager.getEnemies()){
            if (!enemy.isDestroyed()) {
                return false;
            }
        }
        return true;
    }
}
