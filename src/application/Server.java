package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
        public static void main(String[] args) throws IOException {
                //1.创建服务端+端口
                DatagramSocket server = new DatagramSocket(7777);
                //2.准备接受容器
                byte[] container = new byte[1024];
                //3.封装成包
                DatagramPacket packet = new DatagramPacket(container, container.length);
                //4.接受数据
                server.receive(packet);
                //5.分析数据
                byte[] data = packet.getData();
                int len = packet.getLength();
                System.out.println("server receive:"+new String(data,0,len));
                //6.释放
                server.close();
        }
}