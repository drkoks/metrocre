package com.metrocre.game.network;

import java.util.HashSet;
import java.util.Queue;

public class TradeState extends ServerState {
    private GameServer server;

    private HashSet<String> isReady = new HashSet<>();

    public TradeState(GameServer server) {
        this.server = server;
    }

    @Override
    public void update(float deltaTime) {
        boolean done = true;
        for (GameServer.GameViewConnection connection : server.getConnections()) {
            Queue<Object> playerEvents = connection.messageStock.getReceived();
            for (Object event : playerEvents) {
                if (event instanceof Network.Buy) {
                    Network.Buy buy = (Network.Buy) event;
                    connection.gameView.playersProfile.buyItem(buy.upgrades, null, connection, -1);
                } else if (event instanceof Network.PlayerReady) {
                    isReady.add(connection.gameView.playersProfile.getName());
                }
            }
            if (!isReady.contains(connection.gameView.playersProfile.getName())) {
                done = false;
            }
        }

        if (done) {
            server.packToSend(new Network.NextLevel());
            server.setState(new LevelState(server));
        }

        server.sendAll();
    }
}
