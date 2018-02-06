package com.huang.Aio.Server;

/**
 * @author simple_huang@foxmail.com on 2018/2/6.
 */
public class AioChatServer {
    private static String DEFAULT_HOST = "127.0.0.1";
    private static int DEFAULT_PORT = 15000;
    private static AioServerHandler serverHandler;
    public volatile static long clientCount = 0;

    public static void main(String[] args) {

    }
}
