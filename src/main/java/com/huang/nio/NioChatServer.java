package com.huang.nio;

/**
 * @author simple_huang@foxmail.com on 2018/1/29.
 */
public class NioChatServer {
    private static int DEFAULT_PORT = 12345;
    private static ServerHandler serverHandler;

    public static synchronized void start(int port) {
        if (serverHandler != null) {
            serverHandler.stop();
        }
        serverHandler = new ServerHandler(port);
        new Thread(serverHandler, "Server").start();
    }

    public static void start() {
        start(DEFAULT_PORT);
    }

    public static void main(String[] args) {
        start();
    }
}
