package application.controller;

import application.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller2 implements Initializable {
    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;
    public Button OK_Button;
    private int client_port = 8888;
    Socket socket = new Socket("localhost", client_port);
    private Client client = new Client(socket);

    @FXML
    private Pane base_square;

    @FXML
    private Rectangle game_panel;

    private static boolean TURN = false;
    private static boolean begin = true;

    private static final int[][] chessBoard = new int[3][3];
    private static final boolean[][] flag = new boolean[3][3];

    public Controller2() throws IOException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client.send("Player2 is ready\n");
        game_panel.setOnMouseClicked(event -> {
            int x = (int) (event.getX() / BOUND);
            int y = (int) (event.getY() / BOUND);
            if (refreshBoard(x, y)) {
                TURN = false;
                drawChess();
            }
        });
        OK_Button.setOnMouseClicked(event -> {
            OK_Button.setVisible(false);
            OK_Button.setManaged(false);
            if (!TURN && begin) {
                begin = false;
                new Thread(() -> {
                    String message = client.receive();
//                    System.out.println(message);
                    if (message.equals("End Because Of Exception")) {
                        Platform.exit();
//                        System.out.println(message);
                        message = "0,0,shutdown";
                    }
                    int xx = message.split(",")[0].charAt(0) - '0';
                    int yy = message.split(",")[1].charAt(0) - '0';
                    TURN = message.split(",")[2].equals("player1");
//                    System.out.println(TURN);
                    if (TURN) {
                        chessBoard[xx][yy] = PLAY_1;
                    }
                }).start();
            }
            drawChess();
        });
        EventHandler<ActionEvent> eventHandler = e -> {
            drawChess();
            if (win() && !lose()) {
                System.out.println("You won the game!");
                System.exit(0);
            }
            else if (!win() && lose()) {
                System.out.println("You lost the game!");
                System.exit(0);
            }
            else if (!win() && !lose() && full() ) {
                System.out.println("The game is a draw.");
                System.exit(0);
            }
        };
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(10), eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private boolean refreshBoard (int x, int y) {
        if (chessBoard[x][y] == EMPTY && TURN) {
            chessBoard[x][y] = PLAY_2;
            client.send(x + "," + y + ",player2\n");
            new Thread(() -> {
                String message = client.receive();
//                System.out.println(message);
                if (message.equals("End Because Of Exception")) {
//                    System.out.println(message);
                    Platform.exit();
                    message = "0,0,shutdown";
                }
                int xx = message.split(",")[0].charAt(0) - '0';
                int yy = message.split(",")[1].charAt(0) - '0';
                TURN = message.split(",")[2].equals("player1");
//                System.out.println(TURN);
                if (TURN) {
                    chessBoard[xx][yy] = PLAY_1;
                }
            }).start();
//            drawChess();
            return true;
        }
        return false;
    }

    private boolean lose() {
        boolean flag = true;
        int[] h = {0, 1, 2};
        for (int i = 0; i < 3; i++) {
            flag = true;
            for (int j = 0; j < 3; j++) {
                if (chessBoard[i][h[j]] == PLAY_2 || chessBoard[i][h[j]] == EMPTY) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return flag;
            }
        }//横
        for (int i = 0; i < 3; i++) {
            flag = true;
            for (int j = 0; j < 3; j++) {
                if (chessBoard[h[j]][i] == PLAY_2 || chessBoard[h[j]][i] == EMPTY) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return flag;
            }
        }//竖
        flag = true;
        for (int i = 0; i < 3; i++) {
            if (chessBoard[i][i] == PLAY_2 || chessBoard[i][i] == EMPTY) {
                flag = false;
                break;
            }
        }
        if (flag) {
            return flag;
        }//正对角线
        flag = true;
        for (int i = 0; i < 3; i++) {
            if (chessBoard[i][2 - i] == PLAY_2 || chessBoard[i][2 - i] == EMPTY) {
                flag = false;
                break;
            }
        }
        if (flag) {
            return flag;
        }//反对角线

        return false;
    }

    private boolean win() {
        boolean flag = true;
        int[] h = {0, 1, 2};
        for (int i = 0; i < 3; i++) {
            flag = true;
            for (int j = 0; j < 3; j++) {
                if (chessBoard[i][h[j]] == PLAY_1 || chessBoard[i][h[j]] == EMPTY) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return flag;
            }
        }//横
        for (int i = 0; i < 3; i++) {
            flag = true;
            for (int j = 0; j < 3; j++) {
                if (chessBoard[h[j]][i] == PLAY_1 || chessBoard[h[j]][i] == EMPTY) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return flag;
            }
        }//竖
        flag = true;
        for (int i = 0; i < 3; i++) {
            if (chessBoard[i][i] == PLAY_1 || chessBoard[i][i] == EMPTY) {
                flag = false;
                break;
            }
        }
        if (flag) {
            return flag;
        }//正对角线
        flag = true;
        for (int i = 0; i < 3; i++) {
            if (chessBoard[i][2 - i] == PLAY_1 || chessBoard[i][2 - i] == EMPTY) {
                flag = false;
                break;
            }
        }
        if (flag) {
            return flag;
        }//反对角线

        return false;
    }

    private boolean full() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (chessBoard[i][j] == EMPTY)
                    return false;
        return true;
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
