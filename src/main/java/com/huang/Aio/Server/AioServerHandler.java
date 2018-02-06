package com.huang.Aio.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author simple_huang@foxmail.com on 2018/2/6.
 */
public class AioServerHandler implements Runnable {
    public CountDownLatch latch;
    public AsynchronousServerSocketChannel serverChannel;

    public AioServerHandler(int port) {
        try {
            serverChannel = AsynchronousServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            System.out.println("服务器已启动，端口号：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        serverChannel.accept(this, new AcceptHandler());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
