package com.metrocre.game.network;

import com.metrocre.game.PlayersProfile;
import com.metrocre.game.world.Player;

public class GameView {
    PlayersProfile playersProfile;
    Player player;

    public void init(String name) {
        playersProfile = new PlayersProfile(name, 1, 0, 0, 1, 1, 1);
    }
}
