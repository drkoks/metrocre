package com.metrocre.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.TimeUtils;
import com.metrocre.game.network.GameClient;
import com.metrocre.game.network.GameServer;
import com.metrocre.game.screens.GameScreen;
import com.metrocre.game.screens.ShopScreen;

import java.io.IOException;
import java.util.Scanner;

public class MyGame extends Game {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final int SCALE = 15;
    public static final float UNIT_SCALE = SCALE / 32f;
    static final long FPS = 60;
    static final long NANOS_PER_FRAME = 1_000_000_000 / FPS;
    private float volume = 1.0f;
    private long prevRenderTime = 0;
    //private MessageDispatcher messageDispatcher = new MessageDispatcher();
    //private BuyEventHandler buyEventHandler = new BuyEventHandler(playersProfile);
    private GameServer server = null;
    private GameClient client = null;
    public PlayersProfile localPlayerProfile = null;
    private ShopScreen shopScreen;

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    //public MessageDispatcher getMessageDispatcher() {
        //return messageDispatcher;
    //}

    @Override
    public void create() {
        //messageDispatcher.addListener(buyEventHandler, TradeEvents.BUY);
        //setScreen(new MainMenuScreen(this));
        System.out.println("s/c: ");
        Scanner scanner = new Scanner(System.in);
        if (scanner.nextLine().equals("s")) {
            server = new GameServer();
            try {
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        client = new GameClient();
        client.start();
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        long currentTime = TimeUtils.nanoTime();
        if (prevRenderTime == 0) {
            prevRenderTime = currentTime;
        }
        while (prevRenderTime <= currentTime) {
            if (server != null) {
                server.update(1f / FPS);
            }
            screen.render(1f / FPS);
            if (shopScreen != null) {
                shopScreen.render(1f / FPS);
            }
            prevRenderTime += NANOS_PER_FRAME;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (server != null) {
            server.dispose();
        }
    }

    public GameClient getClient() {
        return client;
    }

    public GameServer getServer() {
        return server;
    }

    public void setShopScreen(ShopScreen shopScreen) {
        this.shopScreen = shopScreen;
    }
}
