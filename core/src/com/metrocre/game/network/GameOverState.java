package com.metrocre.game.network;

import java.util.Queue;

public class GameOverState extends ServerState {
    GameServer server;

    public GameOverState(GameServer server) {
        this.server = server;
    }

    @Override
    public void update(float deltaTime) {
        for (GameServer.GameViewConnection connection : server.getConnections()) {
            Queue<Object> playerEvents = connection.messageStock.getReceived();
            for (Object event : playerEvents) {
                if (event instanceof Network.PlayerReady) {
                    server.dispose();
                    return;
                }
            }
        }
    }
}
