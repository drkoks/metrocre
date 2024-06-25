package com.metrocre.game.network;

public class WaitingForPlayersState extends ServerState {
    private float countDown;
    private GameServer server;

    WaitingForPlayersState(GameServer server) {
        countDown = 0f;
        this.server = server;
    }

    @Override
    public void update(float deltaTime) {
        countDown -= deltaTime;
        if (countDown < 0) {
            server.setState(new LevelState(server));
        }
    }
}
