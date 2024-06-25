package com.metrocre.game.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class GameServer {
    private static final Random random = new Random();

    private int levelCounter = 1;
    private int lastLevel = 4;
    private int connectionCnt = 0;
    private Server server;
    private ServerState gameState;
    private int trainHealth = 200;

    public void start() throws IOException {
        server = new Server(){
            protected Connection newConnection() {
                return new GameViewConnection();
            }
        };
        Network.register(server);
        server.addListener(new Listener() {
            public void connected(Connection c) {
                if (!(gameState instanceof WaitingForPlayersState)) {
                    c.close();
                }
                GameViewConnection connection = (GameViewConnection) c;
                connectionCnt++;
                connection.gameView.init("Player" + connectionCnt);
            }

            public void received(Connection c, Object object) {
                GameViewConnection connection = (GameViewConnection) c;
                connection.messageStock.receive(object);
            }
        });
        server.bind(Network.PORT);
        server.start();

        setState(new WaitingForPlayersState(this));
    }

    public void update(float deltaTime) {
        gameState.update(deltaTime);
    }

    public void setState(ServerState state) {
        gameState = state;
    }

    public List<GameViewConnection> getConnections() {
        List<GameViewConnection> connections = new ArrayList<>();
        for (Connection connection : server.getConnections()) {
            connections.add((GameViewConnection) connection);
        }
        return connections;
    }

    public void sendToAll(Object o) {
        for (Connection connection : server.getConnections()) {
            connection.sendTCP(o);
        }
    }

    public static class GameViewConnection extends Connection {
        public GameView gameView = new GameView();
        public MessageStock messageStock = new MessageStock();
    }

    public void dispose() {
        server.stop();
        server.close();
    }

    public void packToSend(Object object) {
        for (Connection connection : server.getConnections()) {
            GameViewConnection gameViewConnection = (GameViewConnection) connection;
            gameViewConnection.messageStock.packToSend(object);
        }
    }

    public void sendAll() {
        for (Connection connection : server.getConnections()) {
            GameViewConnection gameViewConnection = (GameViewConnection) connection;
            Queue<Object> queue = gameViewConnection.messageStock.getSended();
            while (!queue.isEmpty()) {
                gameViewConnection.sendTCP(queue.remove());
            }
        }
    }

    public void incrementLevelCnt() {
        levelCounter++;
    }

    public int getLevelCnt() {
        return levelCounter;
    }

    public Random getRandom() {
        return random;
    }

    public boolean isWin(){
        return levelCounter > lastLevel;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public int getTrainHealth() {
        return trainHealth;
    }

    public void setTrainHealth(int value) {
        trainHealth = value;
    }
}
