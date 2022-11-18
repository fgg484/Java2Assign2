package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        //1.创建客户端+端口
        DatagramSocket client = new DatagramSocket(5555);
        //2.准备数据
        Scanner sc = new Scanner(System.in);
        System.out.println("input send msg:");
        String msg = sc.nextLine();
        byte[] data = msg.getBytes();
        //3.打包（发送的地点及端口）
        DatagramPacket packet = new DatagramPacket(data, data.length,new InetSocketAddress("localhost",7777));
        //4.发送
        client.send(packet);
        //5.释放
        client.close();

    }
}