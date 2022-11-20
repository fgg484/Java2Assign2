package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

public class Controller1 implements Initializable {
    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;
    private DatagramSocket client;
    private DatagramSocket server;
    public static String ip = "";
    private int server_port = 8888;
    private int client_port = 6666;

    @FXML
    private Pane base_square;

    @FXML
    private Rectangle game_panel;

    private static boolean TURN = false;

    private static final int[][] chessBoard = new int[3][3];
    private static final boolean[][] flag = new boolean[3][3];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game_panel.setOnMouseClicked(event -> {
            int x = (int) (event.getX() / BOUND);
            int y = (int) (event.getY() / BOUND);
            if (refreshBoard(x, y)) {
//                try {
//                    //1.创建服务端+端口
//                    DatagramSocket client = new DatagramSocket(client_port);
//                    //2.准备接受容器
//                    byte[] container = new byte[1024];
//                    //3.封装成包
//                    DatagramPacket packet = new DatagramPacket(container, container.length);
//                    //4.接受数据
//                    client.receive(packet);
//                    //5.分析数据
//                    byte[] data = packet.getData();
//                    int len = packet.getLength();
//                    String message = new String(data, 0, len);
//                    TURN = message.split(",")[2].equals("false");
//                    //6.释放
//                    client.close();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
                TURN = !TURN;
            }
        });
    }

    private boolean refreshBoard (int x, int y) {
        if (chessBoard[x][y] == EMPTY) {
            chessBoard[x][y] = TURN ? PLAY_1 : PLAY_2;
            //TODO: send msg to the server
//            try {
//                client = new DatagramSocket(client_port);
//                String msg;
//                msg = x + "," + y + "," + TURN;
//                byte[] data = msg.getBytes();
//                DatagramPacket packet = new DatagramPacket(data, data.length, new InetSocketAddress("localhost", server_port));
//                client.send(packet);
////                System.out.println("send2=" + msg);
//                client.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
            drawChess();
            return true;
        }
        return false;
    }

    private void drawChess () {
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[0].length; j++) {
                if (flag[i][j]) {
                    // This square has been drawing, ignore.
                    continue;
                }
                switch (chessBoard[i][j]) {
                    case PLAY_1:
                        drawCircle(i, j);
                        break;
                    case PLAY_2:
                        drawLine(i, j);
                        break;
                    case EMPTY:
                        // do nothing
                        break;
                    default:
                        System.err.println("Invalid value!");
                }
            }
        }
    }

    private void drawCircle (int i, int j) {
        Circle circle = new Circle();
        base_square.getChildren().add(circle);
        circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
        circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
        circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
        flag[i][j] = true;
    }

    private void drawLine (int i, int j) {
        Line line_a = new Line();
        Line line_b = new Line();
        base_square.getChildren().add(line_a);
        base_square.getChildren().add(line_b);
        line_a.setStartX(i * BOUND + OFFSET * 1.5);
        line_a.setStartY(j * BOUND + OFFSET * 1.5);
        line_a.setEndX((i + 1) * BOUND + OFFSET * 0.5);
        line_a.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_a.setStroke(Color.BLUE);

        line_b.setStartX((i + 1) * BOUND + OFFSET * 0.5);
        line_b.setStartY(j * BOUND + OFFSET * 1.5);
        line_b.setEndX(i * BOUND + OFFSET * 1.5);
        line_b.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_b.setStroke(Color.BLUE);
        flag[i][j] = true;
    }
}
