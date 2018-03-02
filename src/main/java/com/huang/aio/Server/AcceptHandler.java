package com.huang.aio.Server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author simple_huang@foxmail.com on 2018/2/6.
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel channel, AioServerHandler serverHandler) {
        AioChatServer.clientCount++;
        System.out.println("连接的客户端数: " + AioChatServer.clientCount);
        serverHandler.serverChannel.accept(serverHandler, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer, buffer, new AioReadHandler(channel));
    }

    @Override
    public void failed(Throwable exc, AioServerHandler serverHandler) {
        exc.printStackTrace();
        serverHandler.latch.countDown();
    }
}
