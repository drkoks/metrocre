package com.metrocre.game.event.world;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.metrocre.game.Enemy;
import com.metrocre.game.wepons.Rail;

public class RailHitEventHandler implements Telegraph {
    @Override
    public boolean handleMessage(Telegram msg) {
        RailHitEventData data = (RailHitEventData) msg.extraInfo;
        Rail rail = data.rail;
        if (data.hittedObject instanceof Enemy) {
            Enemy enemy = (Enemy) data.hittedObject;
            enemy.takeDamage(rail.getDamage(), rail.getPlayer());
        }
        return true;
    }
}
