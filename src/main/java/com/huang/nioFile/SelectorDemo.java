package com.huang.nioFile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author simple_huang@foxmail.com on 2018/3/15.
 */
public class SelectorDemo {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("localhost", 8082));
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
        channel.configureBlocking(true);

        String msg = "lasdjfasjflasjf";
        ByteBuffer buffer = ByteBuffer.allocate(15);
        buffer.clear();
        buffer.put(msg.getBytes());

        buffer.flip();
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }
}
