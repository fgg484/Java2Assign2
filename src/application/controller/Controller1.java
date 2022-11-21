package application.controller;

import application.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

public class Controller1 implements Initializable {
    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;
    private int client_port = 6666;
    private Client client = new Client(client_port);

    @FXML
    private Pane base_square;

    @FXML
    private Rectangle game_panel;

    private static boolean TURN = true;

    private static final int[][] chessBoard = new int[3][3];
    private static final boolean[][] flag = new boolean[3][3];

    public Controller1() throws IOException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client.send("Player1 is ready");
        if (!TURN) {
            String message = client.receive();
            System.out.println(message);
            int x = message.split(",")[0].charAt(0) - '0';
            int y = message.split(",")[1].charAt(0) - '0';
            TURN = message.split(",")[2].equals("player2");
            System.out.println(TURN);
            if (TURN) {
                chessBoard[x][y] = PLAY_2;
                drawChess();
            }
        }
        game_panel.setOnMouseClicked(event -> {
            int x = (int) (event.getX() / BOUND);
            int y = (int) (event.getY() / BOUND);
            if (refreshBoard(x, y)) {
                TURN = false;
                drawChess();
            }
        });
        EventHandler<ActionEvent> eventHandler = e -> {
            drawChess();
        };
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(10), eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private boolean refreshBoard (int x, int y) {
        if (chessBoard[x][y] == EMPTY && TURN) {
            chessBoard[x][y] = PLAY_1;
            client.send(x + "," + y + ",player1\n");
            new Thread(() -> {
                String message = client.receive();
                System.out.println(message);
                int xx = message.split(",")[0].charAt(0) - '0';
                int yy = message.split(",")[1].charAt(0) - '0';
                TURN = message.split(",")[2].equals("player2");
                System.out.println(TURN);
                if (TURN) {
                    chessBoard[xx][yy] = PLAY_2;
                }
            }).start();
//            drawChess();
            return true;
        }
        return false;
    }

    public void drawChess () {
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
