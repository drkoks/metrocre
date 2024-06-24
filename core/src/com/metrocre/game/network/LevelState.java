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

import java.util.List;
import java.util.Queue;
import java.util.Random;

public class LevelState extends ServerState {
    private GameServer server;
    private WorldManager worldManager;
    private Train train;
    private Map map;
    private boolean isAbleToFinishLevel = false;

    public LevelState(GameServer server) {
        this.server = server;
        worldManager = new WorldManager(new World(new Vector2(0, 0), false), server);
        List<GameServer.GameViewConnection> connections = server.getConnections();
        int cnt = 1;
        for (GameServer.GameViewConnection connection : connections) {
            EntityData.PlayerData playerData = new EntityData.PlayerData();
            playerData.x = 6 * SCALE;
            playerData.y = (cnt++) * SCALE;
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
            equipWeapon.weaponId = connection.gameView.playersProfile.getWeaponId();
            worldManager.getMessageDispatcher().dispatchMessage(WorldEvents.EquipWeapon.ID, equipWeapon);
        }
        Random rand = server.getRandom();
        int randomNumber = rand.nextInt(5) + 1;
        map = new Map(worldManager, randomNumber, true);
        Network.SendMapSeed sendMapSeed = new Network.SendMapSeed();
        sendMapSeed.seed = randomNumber;
        server.packToSend(sendMapSeed);

        EntityData.TrainData trainData = new EntityData.TrainData();
        trainData.x = SCALE;
        trainData.y = 0;
        trainData.width = 3 * SCALE;
        trainData.height = map.getHeight();
        WorldEvents.AddEntity addEntity = new WorldEvents.AddEntity();
        addEntity.type = EntityType.Train;
        addEntity.data = trainData;
        worldManager.getMessageDispatcher().dispatchMessage(WorldEvents.AddEntity.ID, addEntity);
    }

    private void processPlayerEvent(GameServer.GameViewConnection connection, Object event) {
        Player player = connection.gameView.player;
        if (player != null && event instanceof Network.PlayerMove) {
            Network.PlayerMove playerMoveEvent = (Network.PlayerMove) event;
            player.move(playerMoveEvent.direction);
        } else if (player != null && event instanceof Network.PlayerAttack) {
            Network.PlayerAttack playerAttackEvent = (Network.PlayerAttack) event;
            player.shoot(playerAttackEvent.direction);
        } else if (player != null && event instanceof Network.Buy) {
            player.getPlayersProfile().buyItem(((Network.Buy) event).upgrades, worldManager, connection, player.getId());
        } else if (event instanceof Network.CompleteLevel) {
            server.incrementLevelCnt();
            if (server.isWin()) {
                Network.EndGame endGame = new Network.EndGame();
                endGame.isVictory = true;
                server.packToSend(endGame);
                server.setState(new GameOverState(server));
            } else {
                server.setState(new TradeState(server));
                server.packToSend(event);
            }
        }
    }

    public void update(float deltaTime) {
        for (GameServer.GameViewConnection connection : server.getConnections()) {
            Queue<Object> events = connection.messageStock.getReceived();
            for (Object event : events) {
                processPlayerEvent(connection, event);
            }
        }

        worldManager.getWorld().step(deltaTime, 6, 2);
        worldManager.update(deltaTime);

        isAbleToFinishLevel = worldManager.getEnemies().isEmpty() && worldManager.spawnersAreDone();
        if (isAbleToFinishLevel) {
            server.packToSend(new Network.AbleToFinish());
        }

        int playersDead = 0;
        for (GameServer.GameViewConnection connection : server.getConnections()) {
            if (connection.gameView.player.isDestroyed()) {
                playersDead++;
            }
        }
        if (playersDead >= server.getConnections().size()) {
            Network.EndGame endGame = new Network.EndGame();
            endGame.isVictory = false;
            server.packToSend(endGame);
            server.setState(new GameOverState(server));
        }

        for (Entity entity : worldManager.getEntities()) {
            Network.UpdateEntityPosition updateEntityPosition = new Network.UpdateEntityPosition();
            updateEntityPosition.entityId = entity.getId();
            updateEntityPosition.x = entity.getX();
            updateEntityPosition.y = entity.getY();
            server.packToSend(updateEntityPosition);
        }

        server.sendAll();
    }
}
