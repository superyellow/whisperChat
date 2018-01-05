package com.huang.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 服务端
 */
public class Server {
    public void start(){//服务端的启动方法
        try{
			/*
			 * 接收包的步骤：
			 * 1.创建socket（一次）
			 * 2.创建一个合适大小的包
			 * 3.通过socket接收数据到包中
			 * 4.拆包取数据
			 */
            DatagramSocket socket=new DatagramSocket(8088);
            byte[] bytes=new byte[20];
            DatagramPacket packet=new DatagramPacket(bytes,bytes.length);

            socket.receive(packet);//接收数据到包中，receive是阻塞方法
            byte[] d=packet.getData();//拆包拿数据
            int dlen=packet.getLength();//获取有效字节的长度

            String str=new String(d,0,dlen,"UTF-8");
            System.out.println(str);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        Server server=new Server();
        server.start();
    }
}