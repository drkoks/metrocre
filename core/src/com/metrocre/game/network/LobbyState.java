package com.metrocre.game.network;

import java.util.HashSet;
import java.util.Queue;

public class LobbyState extends ServerState {
    private GameServer server;

    private HashSet<String> isReady = new HashSet<>();

    public LobbyState(GameServer server) {
        this.server = server;
    }

    @Override
    public void update(float deltaTime) {
        boolean done = true;
        for (GameServer.GameViewConnection connection : server.getConnections()) {
            Queue<Object> playerEvents = connection.messageStock.getReceived();
            for (Object event : playerEvents) {
                if (event instanceof Network.PlayerReady) {
                    isReady.add(connection.gameView.playersProfile.getName());
                }
            }
            if (!isReady.contains(connection.gameView.playersProfile.getName())) {
                done = false;
            }
        }

        Network.PlayerReady playerReady = new Network.PlayerReady();
        playerReady.cnt = isReady.size();
        server.packToSend(playerReady);

        if (done) {
            server.packToSend(new Network.NextLevel());
            server.setState(new LevelState(server));
        }

        server.sendAll();
    }
}
