package com.huang.nioFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author simple_huang@foxmail.com on 2018/2/9.
 */
public class FileOperation {
    public static void main(String[] args) throws IOException {
        //testFileBuffer();
        testChannelTransfer();
    }

    public static void testFileBuffer() throws IOException {
        RandomAccessFile raFile = new RandomAccessFile("README.md", "rw");
        FileChannel inChannel = raFile.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(48);
        int byteRead = inChannel.read(buffer);//从channel中读
        while (byteRead != -1) {
            System.out.println(byteRead);
            buffer.flip();//
            System.out.println(new String(buffer.array(), "UTF-8"));
            while (buffer.hasRemaining()) {
                System.out.println((char) buffer.get());
            }

            buffer.clear();
            byteRead = inChannel.read(buffer);
            System.out.println(byteRead);
        }
        raFile.close();
    }

    public static void testChannelTransfer() throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("README.md", "rw");
        FileChannel fromChannel = fromFile.getChannel();
        RandomAccessFile toFile = new RandomAccessFile("nio-file.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        int position = 0;
        long count = fromChannel.size();
        toChannel.transferFrom(fromChannel, position, count);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        toChannel.read(buffer);
        System.out.println(new String(buffer.array()));
    }

}
