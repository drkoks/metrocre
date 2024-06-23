package com.metrocre.game.network;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageStock {
    private ConcurrentLinkedQueue<Object> received = new ConcurrentLinkedQueue<>();
    private Queue<Object> sended = new ArrayDeque<>();

    public void receive(Object object) {
        received.add(object);
    }

    public void packToSend(Object object) {
        sended.add(object);
    }

    public Queue<Object> getReceived() {
        Queue<Object> queue = new ArrayDeque<>();
        while (!received.isEmpty()) {
            queue.add(received.remove());
        }
        return queue;
    }

    public Queue<Object> getSended() {
        return sended;
    }
}
