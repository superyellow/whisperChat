package com.huang.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 客户端的启动方法
 * @author Administrator
 *
 */
public class Client {
    public void start(){
        try{
			/*
			 * 向服务端发送数据的步骤
			 * 1.创建好socket（一次就可以），准备数据
			 * 2.创建数据 包
			 * 3.将数据包存入包中（2、3步同步完成的）
			 * 4.将数据包通过socket发送给服务端
			 */
            DatagramSocket socket=new DatagramSocket();//创建socket

            String str="你好，服务端";
            byte[] bytes=str.getBytes("UTF-8");

            InetAddress address=InetAddress.getByName("localhost");//创建地址
            int port=8088;

            DatagramPacket sendPacket=new DatagramPacket(
                    bytes,bytes.length,address,port);//创建发送包
            socket.send(sendPacket);//投递
        }catch(Exception e){

        }
    }
    public static void main(String[] args) {
        Client client=new Client();
        client.start();
    }

}
