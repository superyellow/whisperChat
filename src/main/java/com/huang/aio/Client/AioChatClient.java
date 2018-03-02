package com.huang.aio.Client;

import java.util.Scanner;

/**
 * @author simple_huang@foxmail.com on 2018/2/6.
 */
public class AioChatClient {
    private static String DEFAULT_HOST = "127.0.0.1";
    private static int DEFAULT_PORT = 15000;
    private static AioClientHandler clientHandler;

    public static void start() {
        start(DEFAULT_HOST, DEFAULT_PORT);
    }

    public static synchronized void start(String ip, int port) {
        if (clientHandler != null) return;
        clientHandler = new AioClientHandler(ip, port);
        new Thread(clientHandler, "client").start();
    }

    public static boolean sendMsg(String msg) {
        clientHandler.sendMsg(msg);
        return true;
    }

    public static void main(String[] args) {
        start();
        System.out.println("请输入请求信息: ");
        Scanner scanner = new Scanner(System.in);
        while (sendMsg(scanner.nextLine()));
    }
}

