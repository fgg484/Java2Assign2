package application;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        while (true) {
            try {
                ServerSocket serverSocket1 = new ServerSocket(6666);//创建绑定到特定端口的服务器Socket。
                ServerSocket serverSocket2 = new ServerSocket(8888);//创建绑定到特定端口的服务器Socket。
                Socket socket1 = null;//需要接收的客户端Socket
                Socket socket2 = null;//需要接收的客户端Socket
                int count = 0;//记录客户端数量
                System.out.println("服务器启动");
                //定义一个死循环，不停的接收客户端连接
                while (true) {
                    socket1 = serverSocket1.accept();//侦听并接受到此套接字的连接
                    InetAddress inetAddress1 = socket1.getInetAddress();//获取客户端的连接
                    socket2 = serverSocket2.accept();//侦听并接受到此套接字的连接
                    InetAddress inetAddress2 = socket2.getInetAddress();//获取客户端的连接
                    ServerThread thread = new ServerThread(socket1, inetAddress1, socket2, inetAddress2, count / 2);//自己创建的线程类
                    thread.start();//启动线程
//                    count += 2;//如果正确建立连接
//                    System.out.println("客户端数量：" + count);//打印客户端数量
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}