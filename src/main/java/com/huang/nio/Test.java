package com.huang.nio;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author simple_huang@foxmail.com on 2018/1/30.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        NioChatServer.start();
        Thread.sleep(100);
        NioChatClient.start();
        while (NioChatClient.sendMsg(new Scanner(System.in).nextLine()));
    }
}
