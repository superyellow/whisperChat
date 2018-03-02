package com.huang.nioFile;

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
        RandomAccessFile aFile = new RandomAccessFile("nio-file.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(48);

        int byteRead = inChannel.read(buffer);
        while (byteRead != -1) {
            System.out.println("read " + byteRead);
            buffer.flip();

//            System.out.println(new String(buffer.array()));

            while (buffer.hasRemaining()) {
                System.out.println((char) buffer.get());
            }

            buffer.clear();
            byteRead = inChannel.read(buffer);
        }

        aFile.close();

    }

}
