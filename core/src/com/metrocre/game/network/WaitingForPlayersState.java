package com.metrocre.game.network;

public class WaitingForPlayersState extends GameState {
    private float countDown;
    private GameServer server;

    WaitingForPlayersState(GameServer server) {
        countDown = 10f;
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
