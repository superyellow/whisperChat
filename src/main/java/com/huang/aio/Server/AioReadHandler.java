package com.huang.aio.Server;

import com.huang.aio.util.Calculator;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author simple_huang@foxmail.com on 2018/2/6.
 */
public class AioReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;

    public AioReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();
        byte[] message = new byte[buffer.remaining()];
        buffer.get(message);
        try {
            String expression = new String(message, "UTF-8");
            System.out.println("服务器收到消息: " + expression);
            String calResult = null;
            try {
                calResult = Calculator.cal(expression).toString();
            } catch (ScriptException e) {
                calResult = "计算错误: " + e.getMessage();
                e.printStackTrace();
            }

            doWrite(calResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String result) {
        byte[] bytes = result.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();

        channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining())
                    channel.write(buffer, buffer, this);
                else {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    channel.read(readBuffer, readBuffer, new AioReadHandler(channel));
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buffer) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
