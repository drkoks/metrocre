package com.metrocre.game.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.Queue;

public class GameClient {
    private Client client;
    private MessageStock messageStock = new MessageStock();

    public void start() {
        client = new Client();
        client.start();
        Network.register(client);
        client.addListener(new Listener() {
            public void connected (Connection connection) {

            }

            public void received (Connection connection, Object object) {
                messageStock.receive(object);
            }

            public void disconnected (Connection connection) {
                System.exit(0);
            }
        });
        try {
            client.connect(5000, "localhost", Network.PORT);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        client.sendTCP("normalno");
    }

    public Queue<Object> getRemoteEvents() {
        return messageStock.getReceived();
    }

    public Object getRemoteEvent() {
        return messageStock.getNextRecieved();
    }

    public void packToSend(Object object) {
        messageStock.packToSend(object);
    }

    public void sendAll() {
        Queue<Object> queue = messageStock.getSended();
        while (!queue.isEmpty()) {
            client.sendTCP(queue.remove());
        }
    }
}
