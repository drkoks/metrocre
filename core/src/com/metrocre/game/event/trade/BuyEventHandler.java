package com.metrocre.game.event.trade;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.metrocre.game.Player;
import com.metrocre.game.PlayersProfile;
import com.metrocre.game.Upgrades;

public class BuyEventHandler implements Telegraph {
    private PlayersProfile playersProfile;

    public BuyEventHandler(PlayersProfile playersProfile) {
        this.playersProfile = playersProfile;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        BuyEventData buyEventData = (BuyEventData) msg.extraInfo;
        Upgrades upgrade = buyEventData.upgrade;
        playersProfile.buyItem(upgrade);
        return true;
    }
}
