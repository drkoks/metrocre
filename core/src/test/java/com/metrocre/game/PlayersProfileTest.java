package com.metrocre.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayersProfileTest {

    @Test
    void testBuyItem() {
        PlayersProfile playerProfile = new PlayersProfile("Player1", 1, 0, 1000, 1, 1, 1);

        assertTrue(playerProfile.buyItem("Speed"));
        assertEquals(2, playerProfile.getSpeed());
        assertEquals(900, playerProfile.getMoney());

        playerProfile.setMoney(50);
        assertFalse(playerProfile.buyItem("Defence"));
        assertEquals(1, playerProfile.getDefence());
        assertEquals(50, playerProfile.getMoney());

        assertFalse(playerProfile.buyItem("NonExistentItem"));
    }
}