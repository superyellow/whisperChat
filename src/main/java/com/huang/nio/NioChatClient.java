package com.huang.nio;

/**
 * @author simple_huang@foxmail.com on 2018/1/31.
 */
public class NioChatClient {
    private static String DEFAULT_HOST = "127.0.0.1";
    private static int DEFAULT_PORT = 12345;
    private static ClientHandler clientHandler;

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        start(DEFAULT_HOST, DEFAULT_PORT);
    }

    public static synchronized void start(String ip, int port) {
        if (clientHandler != null) {
            clientHandler.stop();
        }
        clientHandler = new ClientHandler(ip, port);
        new Thread(clientHandler, "Server").start();
    }

    public static boolean sendMsg(String msg) throws Exception {
        if(msg.equals("q")) return false;
        clientHandler.sendMsg(msg);
        return true;
    }
}
