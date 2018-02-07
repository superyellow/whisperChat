package com.huang.Aio.Server;

import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * @author simple_huang@foxmail.com on 2018/2/6.
 */
public class AioChatServer {
    private static int DEFAULT_PORT = 15000;
    private static AioServerHandler serverHandler;
    public volatile static long clientCount = 0;

    public static void start() {
        start(DEFAULT_PORT);
    }

    public static synchronized void start(int port) {
        if (serverHandler != null) return;
        serverHandler = new AioServerHandler(port);
        new Thread(serverHandler, "Server").start();
    }

    public static void main(String[] args) {
        start();
    }
}
