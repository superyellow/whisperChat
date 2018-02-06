package com.huang.Aio.Server;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author simple_huang@foxmail.com on 2018/2/6.
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel result, AioServerHandler attachment) {

    }

    @Override
    public void failed(Throwable exc, AioServerHandler attachment) {

    }
}
