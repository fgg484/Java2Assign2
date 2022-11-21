package application;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket1 = new ServerSocket(6666);//创建绑定到特定端口的服务器Socket。
            ServerSocket serverSocket2 = new ServerSocket(8888);//创建绑定到特定端口的服务器Socket。
            Socket socket1 = null;//需要接收的客户端Socket
            Socket socket2 = null;//需要接收的客户端Socket
            int count = 0;//记录客户端数量
            System.out.println("服务器启动");
            //定义一个死循环，不停的接收客户端连接
            while (true) {
                //一局游戏有两个玩家，一个线程
                socket1 = serverSocket1.accept();//侦听并接受到此套接字的连接
                InetAddress inetAddress1 = socket1.getInetAddress();//获取客户端的连接
                socket1.setSoTimeout(500000);
                socket2 = serverSocket2.accept();//侦听并接受到此套接字的连接
                InetAddress inetAddress2 = socket2.getInetAddress();//获取客户端的连接
                socket2.setSoTimeout(500000);
                ServerThread thread = new ServerThread(socket1, inetAddress1, socket2, inetAddress2);//自己创建的线程类
                thread.start();//启动线程
                count += 2;//如果正确建立连接
                System.out.println("客户端数量：" + count);//打印客户端数量

//                InputStream inputStream = null;//字节输入流
//                InputStreamReader inputStreamReader = null;//将一个字节流中的字节解码成字符
//                BufferedReader bufferedReader = null;//为输入流添加缓冲
//                OutputStream outputStream = null;//字节输出流
//                OutputStreamWriter writer = null;//将写入的字符编码成字节后写入一个字节流
//                outputStream = socket2.getOutputStream();
//                writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
//                writer.write("test,test,player1\n");
//                System.out.println("server sends to client2: " + "test");
//                writer.flush();//清空缓冲区数据
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}