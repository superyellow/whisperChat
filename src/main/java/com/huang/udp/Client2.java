package com.huang.udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * @author simple_huang@foxmail.com on 2018/1/5.
 */
public class Client2 {
    public void start() {
        try {
            DatagramSocket socket = new DatagramSocket();
            String str = "hello server";
            byte[] bytes = str.getBytes("UTF-8");
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, 8055);
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client2 client2 = new Client2();
        client2.start();
    }
}
