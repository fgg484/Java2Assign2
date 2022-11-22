package application;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Client {
    public Socket socket;
    public Client(Socket socket) throws IOException {
//        this.port = num;
        this.socket = socket;
//        this.socket.close();
    }
    public void send(String msg) {
        if (msg.length() <= 1) {
            return;
        }
        try {
//            Socket socket = new Socket("localhost", port);
            socket.setSoTimeout(500000);
            OutputStream outputStream = socket.getOutputStream();//得到一个输出流，用于向服务器发送数据
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);//将写入的字符编码成字节后写入一个字节流
//            System.out.println("sent " + msg);
            writer.write(msg + "\n");
            writer.flush();//刷新缓冲
//            socket.shutdownOutput();//只关闭输出流而不关闭连接

            //关闭资源
//            writer.close();
//            outputStream.close();
//            socket.close();
        } catch (IOException e) {
//            e.printStackTrace();
            if (e.getMessage().equals("Connection reset by peer")) {
                System.out.println("Error: Server Stops!");
            }
        }
    }

    public String receive() {
        String msg = "";
        try {
//            Socket socket = new Socket("localhost", port);
            socket.setSoTimeout(500000);
            InputStream inputStream = socket.getInputStream();//得到一个输入流，用于接收服务器响应的数据
//            DataInputStream in = new DataInputStream(inputStream);
//            in.readUTF();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);//将一个字节流中的字节解码成字符
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//为输入流添加缓冲
            String info;
//            System.out.println("客户端IP地址:" + socket.getInetAddress().getHostAddress());
            //输出服务器端响应数据
//            System.out.println(1);
//            System.out.println(bufferedReader.readLine());
            while ((info = bufferedReader.readLine()) != null) {
//                System.out.println(2);
//                System.out.println("have recevied: " + info);
                msg = info;
                if (msg.length() > 0) {
                    break;
                }
            }
//            System.out.println(3);
            //关闭资源
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();
//            socket.close();
        } catch (IOException e) {
//            e.printStackTrace();
            if (e.getMessage().equals("Connection reset")) {
                System.out.println("Error: Server stops!");
            }
            return "End Because Of Exception";
        }
        return msg;
    }
}