package com.huang.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author simple_huang@foxmail.com on 2018/1/5.
 */
public class Server2 {
    public void start() {
        try {
            DatagramSocket socket = new DatagramSocket(8055);
            byte[] bytes = new byte[20];
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            socket.receive(packet);
            byte[] data = packet.getData();
            int dataLength = packet.getLength();
            String str = new String(data, 0, dataLength, "UTF-8");
            System.out.println(str);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server2 server2 = new Server2();
        server2.start();
    }
}
