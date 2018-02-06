package com.huang.nio;

import com.huang.nio.util.Calculator;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author simple_huang@foxmail.com on 2018/2/2.
 */
public class ServerHandler implements Runnable{
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean started;

    public ServerHandler(int port) {
        try {
            //获得一个选择器
            selector = Selector.open();
            //打开监听通道
            serverChannel = ServerSocketChannel.open();
            //监听通道设置非阻塞模式
            serverChannel.configureBlocking(false);
            //绑定端口号 backlog设置1024
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            //监听客户端连接请求
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            //标记服务器已开启
            started = true;
            System.out.println("服务端已启动, 端口号: " + port);
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
        //循环遍历selector
        while (started) {
            try {
                //无论是否有读写事件发生, selector每隔1s被唤醒一次
                selector.select(1000);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handlerInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlerInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(buffer);
                if (readBytes > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String expression = new String(bytes, "UTF-8");
                    System.out.println("服务器收到消息: " + expression);
                    String result;
                    try {
                        result = Calculator.cal(expression).toString();
                    } catch (ScriptException e) {
                        result = "计算错误: " + e.getMessage();
                    }
                    doWrite(sc, result);
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException {
        //将消息编码为字节数组
        byte[] bytes = response.getBytes();
        //根据数组容量创建ByteBuffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到缓冲区
        writeBuffer.put(bytes);
        //flip()
        writeBuffer.flip();
        //将缓冲区的字节数组写出
        channel.write(writeBuffer);
    }
}
