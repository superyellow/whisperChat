package com.huang.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author simple_huang@foxmail.com on 2018/2/2.
 */
public class ClientHandler implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean started;

    public ClientHandler(String ip, int port) {
        this.host = ip;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        started = false;
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //循环遍历selector
        while (started) {
            try {
                //无论是否有读写事件发生，selector每隔1s被唤醒一次
                selector.select(1000);
                //阻塞,只有当至少一个注册的事件发生的时候才会继续.
                //selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if (selector != null)
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) ;
                else System.exit(1);
            }
            if (key.isReadable()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(buffer);

                if (readBytes > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String message = new String(bytes, "UTF-8");
                    System.out.println("客户端收到消息: " + message);
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host, port))) ;
        else socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    public void sendMsg(String msg) throws IOException {
        socketChannel.register(selector, SelectionKey.OP_READ);
        doWrite(socketChannel, msg);
    }

    private void doWrite(SocketChannel channel, String request) throws IOException {
        byte[] bytes = request.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        channel.write(writeBuffer);
    }
}
