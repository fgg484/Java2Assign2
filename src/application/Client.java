package application;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    private final int port;
    public Client(int num) {
        this.port = num;
    }
    public void send(String msg) {
        try {
            Socket socket = new Socket("localhost", port);
            OutputStream outputStream = socket.getOutputStream();//得到一个输出流，用于向服务器发送数据
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);//将写入的字符编码成字节后写入一个字节流
            System.out.println(msg);
            writer.write(msg);
            writer.flush();//刷新缓冲
            socket.shutdownOutput();//只关闭输出流而不关闭连接

            //关闭资源
            writer.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive() {
        StringBuilder msg = new StringBuilder();
        try {
            Socket socket = new Socket("localhost", port);
            InputStream inputStream = socket.getInputStream();//得到一个输入流，用于接收服务器响应的数据
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);//将一个字节流中的字节解码成字符
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//为输入流添加缓冲
            String info;
            System.out.println("客户端IP地址:" + socket.getInetAddress().getHostAddress());
            //输出服务器端响应数据
            while ((info = bufferedReader.readLine()) != null) {
                System.out.println("客户端接收：" + info);
                msg.append(info);
            }
            //关闭资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg.toString();
    }
}