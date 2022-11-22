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

    public ServerThread(Socket socket1, InetAddress inetAddress1, Socket socket2, InetAddress inetAddress2) {
        this.socket1 = socket1;
        this.inetAddress1 = inetAddress1;
        this.socket2 = socket2;
        this.inetAddress2 = inetAddress2;
    }

    @Override
    public void run() {
        boolean flag = true;
        while (true) {
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
                System.out.println("cnt=" + cnt);
                String msg = "";
                if (cnt == 0) {
                    cnt++;
//                    System.out.println(1);
                    inputStream = socket1.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    //循环读取客户端信息
//                    System.out.println(2);
//                    while ((info = bufferedReader.readLine()) != null) {
//                        //获取客户端的ip地址及发送数据
//                        msg.append(info);
//                        System.out.println(info);
//                    }
                    msg = bufferedReader.readLine();
//                    System.out.println(3);
                    System.out.println("test: " + msg);

                    inputStream = socket2.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    //循环读取客户端信息
//                    while ((info = bufferedReader.readLine()) != null) {
//                        //获取客户端的ip地址及发送数据
//                        msg.append(info);
//                    }
                    msg = bufferedReader.readLine();
                    System.out.println("test: " + msg);
                    continue;
                }
                if (cnt % 2 == 1) {
                    inputStream = socket1.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    //循环读取客户端信息
//                    while ((info = bufferedReader.readLine()) != null) {
//                        //获取客户端的ip地址及发送数据
//                        msg.append(info);
//                    }
                    msg = bufferedReader.readLine();
                    if (msg.length() < 1) {
                        continue;
                    }
                    System.out.println("server receives from client1: " + msg);

                    String[] message = msg.split(",");
                    if (message.length == 4) {
                        String win_msg;
                        inputStream = socket1.getInputStream();
                        inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                        bufferedReader = new BufferedReader(inputStreamReader);
                        win_msg = bufferedReader.readLine();
//                        socket1.shutdownInput();//关闭输入流
                        System.out.println(win_msg);
                        if (win_msg.equals("Player1 win")) {
                            System.out.println("Player2 lose");
                        } else if (win_msg.equals("Player1 lose")) {
                            System.out.println("Player2 win");
                        }
                        outputStream = socket2.getOutputStream();
                        writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                        writer.write(msg + "\n");
                        writer.flush();//清空缓冲区数据
                        flag = false;
                        break;
                    } else {
                        //响应客户端请求
                        outputStream = socket2.getOutputStream();
                        writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                        writer.write(msg + "\n");
                        System.out.println("server sends to client2: " + msg);
                        writer.flush();//清空缓冲区数据
//                socket2.shutdownOutput();
                    }
                } else {
                    inputStream = socket2.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    //循环读取客户端信息
//                    while ((info = bufferedReader.readLine()) != null) {
//                        //获取客户端的ip地址及发送数据
//                        msg.append(info);
//                    }
                    msg = bufferedReader.readLine();
                    if (msg.length() < 1) {
                        continue;
                    }
                    System.out.println("server receives from client2: " + msg);
//                    socket2.shutdownInput();//关闭输入流

//                    //响应客户端请求
//                    outputStream = socket2.getOutputStream();
//                    writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
//                    writer.write(msg + "\n");
//                    System.out.println("server sends to client2: " + msg);
//                    writer.flush();//清空缓冲区数据
////                socket2.shutdownOutput();

                    //响应客户端请求
                    outputStream = socket1.getOutputStream();
                    writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                    writer.write(msg + "\n");
                    System.out.println("server sends to client1: " + msg);
                    writer.flush();//清空缓冲区数据
//                socket1.shutdownOutput();
                }
                cnt++;
            } catch (IOException e) {
//                e.printStackTrace();
                if (e.getMessage().equals("Connection reset") && flag) {
                    System.out.println("Error: Clients stop!");
                    if (socket1 != null) {
                        try {
                            outputStream = socket1.getOutputStream();
                            writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                            writer.write("End Because Of Exception" + "\n");
//                        System.out.println("server sends to client1: " + msg);
                            writer.flush();//清空缓冲区数据
                        } catch (IOException ignored) {

                        }
                    }
                    if (socket2 != null) {
                        try {
                            outputStream = socket2.getOutputStream();
                            writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                            writer.write("End Because Of Exception" + "\n");
//                        System.out.println("server sends to client1: " + msg);
                            writer.flush();//清空缓冲区数据
                        } catch (IOException ignored) {

                        }
                    }
                    return;
                }
            }
//            finally {
////                关闭资源
//                try {
//                    if (writer != null) {
//                        writer.close();
//                    }
//                    if (outputStream != null) {
//                        outputStream.close();
//                    }
//                    if (bufferedReader != null) {
//                        bufferedReader.close();
//                    }
//                    if (inputStreamReader != null) {
//                        inputStreamReader.close();
//                    }
//                    if (inputStream != null) {
//                        inputStream.close();
//                    }
//                    if (socket1 != null) {
//                        socket1.close();
//                    }
//                    if (socket2 != null) {
//                        socket2.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

        }
    }
}
