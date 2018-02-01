package com.huang.chat;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author simple_huang@foxmail.com on 2018/1/11.
 */
public class ChatServer {
    private ServerSocket server;
    private List<PrintWriter> allOut;//保存所有客户端输出流

    public ChatServer() throws IOException {
        System.out.println("客户端初始化开始  >>>");
        server = new ServerSocket(10000);
        allOut = new ArrayList<PrintWriter>();
        System.out.println("客户端初始化结束  >>>");
    }

    public void start() {
        try {
            for (;;) {
                System.out.println("等待客户端连接...");
                Socket socket = server.accept();
                Runnable handler = new ClientHandler(socket);
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addOut(PrintWriter pw) {
        allOut.add(pw);
    }

    public synchronized void removeOut(PrintWriter pw) {
        allOut.remove(pw);
    }

    public synchronized void sendMessage(String message) {
        for (PrintWriter pw: allOut) {
            pw.println(message);
        }
    }

    class ClientHandler implements Runnable{
        private Socket socket;
        private InetAddress address;
        private String ip;
        private int port;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            address = socket.getInetAddress();
            ip = address.getHostAddress();
            port = socket.getPort();
        }

        public void run() {
            PrintWriter pw = null;
            try {
                OutputStream out = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
                pw = new PrintWriter(osw, true);//加入true参数, 设置自动行刷新
                addOut(pw);
                sendMessage(ip + ":" + port + " 上线了");
                System.out.println("当前在线人数: " + allOut.size());
                InputStream in = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(isr);
                String message;
                while ((message=br.readLine())!=null) {
                    System.out.println(ip + ":" + port + " 客户端说: " + message);
                    sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                removeOut(pw);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendMessage(ip + ":" + port + " 下线了");
                System.out.println(ip+" "+port+" 下线了");
                System.out.println("当前在线人数:"+allOut.size());
            }
        }
    }
}
