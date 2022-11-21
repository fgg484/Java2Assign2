package application;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerThread extends Thread {
    private int cnt = 0;
    Socket socket1;
    InetAddress inetAddress1;//接收客户端的连接
    Socket socket2;
    InetAddress inetAddress2;//接收客户端的连接
    public ServerThread(Socket socket1, InetAddress inetAddress1, Socket socket2, InetAddress inetAddress2, int cnt) {
        this.socket1 = socket1;
        this.inetAddress1 = inetAddress1;
        this.socket2 = socket2;
        this.inetAddress2 = inetAddress2;
        this.cnt = cnt;
    }

    @Override
    public void run() {
        InputStream inputStream = null;//字节输入流
        InputStreamReader inputStreamReader = null;//将一个字节流中的字节解码成字符
        BufferedReader bufferedReader = null;//为输入流添加缓冲
        OutputStream outputStream = null;//字节输出流
        OutputStreamWriter writer = null;//将写入的字符编码成字节后写入一个字节流
        String info;//临时
        try {
            //TODO:
            // 1.接受client发送的消息
            // 2.将消息转发给另一个client
            boolean flag = true;
                System.out.println("cnt=" + cnt);
                StringBuilder msg = new StringBuilder();
                if (cnt % 2 == 1) {
                    inputStream = socket1.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    //循环读取客户端信息
                    while ((info = bufferedReader.readLine()) != null) {
                        //获取客户端的ip地址及发送数据
                        msg.append(info);
                    }
                    System.out.println("server receives from client1: " + msg);
                    socket1.shutdownInput();//关闭输入流

                    //响应客户端请求
                    outputStream = socket1.getOutputStream();
                    writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                    writer.write(msg + "\n");
                    System.out.println("server sends to client1: " + msg);
                    writer.flush();//清空缓冲区数据
                    socket1.shutdownOutput();

                    //响应客户端请求
                    outputStream = socket2.getOutputStream();
                    writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                    writer.write(msg + "\n");
                    System.out.println("server sends to client2: " + msg);
                    writer.flush();//清空缓冲区数据
                    socket2.shutdownOutput();
                }
                else {
                    inputStream = socket2.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    //循环读取客户端信息
                    while ((info = bufferedReader.readLine()) != null) {
                        //获取客户端的ip地址及发送数据
                        msg.append(info);
                    }
                    System.out.println("server receives from client2: " + msg);
                    socket2.shutdownInput();//关闭输入流

                    //响应客户端请求
                    outputStream = socket2.getOutputStream();
                    writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                    writer.write(msg + "\n");
                    System.out.println("server sends to client2: " + msg);
                    writer.flush();//清空缓冲区数据
                    socket2.shutdownOutput();

                    //响应客户端请求
                    outputStream = socket1.getOutputStream();
                    writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                    writer.write(msg + "\n");
                    System.out.println("server sends to client1: " + msg);
                    writer.flush();//清空缓冲区数据
                    socket1.shutdownOutput();
                }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (writer != null) {
                    writer.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (socket1 != null) {
                    socket1.close();
                }
                if (socket2 != null) {
                    socket2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}