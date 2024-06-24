package com.metrocre.game.network;

import com.metrocre.game.PlayersProfile;

import java.util.HashSet;
import java.util.Queue;

public class TradeState extends GameState {
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
                processPlayerEvent(connection.gameView.playersProfile, event);
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

    private void processPlayerEvent(PlayersProfile playersProfile, Object event) {
        if (event instanceof Network.Buy) {
            Network.Buy buy = (Network.Buy) event;
            playersProfile.buyItem(buy.upgrades, null, -1);
        } else if (event instanceof Network.PlayerReady) {
            isReady.add(playersProfile.getName());
        }
    }
}
